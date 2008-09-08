package fx.module.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModuleBootstrap {
	
	private static final String fileName = "module-definition.xml";
	
	private Option option;
	private String methodName;
	private String moduleName;
	
	private String moduleClass;
	private String moduleIp;
	private int modulePort;
	private Map<String, String> moduleProps;
	
	private Class<?> clazz;
	private Module instance;
	
	public ModuleBootstrap(String[] args) throws Exception {
		parse(args);
		init();
	}
	
	private void parse(String[] args) {
		int len = args.length;
		
		if (len == 2) {
			if (args[0].equals(Option.STARTUP.getCommand())) {
				option = Option.STARTUP;
			} else if (args[0].equals(Option.SHUTDOWN.getCommand())) {
				option = Option.SHUTDOWN;
			} else {
				throw new IllegalArgumentException("Error command format.");
			}
			moduleName = args[1];
		} else if (len == 3) {
			if (args[0].equals(Option.REMOTE.getCommand())) {
				option = Option.REMOTE;
			} else if (args[0].equals(Option.LOCAL.getCommand())) {
				option = Option.LOCAL;
			} else {
				throw new IllegalArgumentException("Error command format.");
			}
			methodName = args[1];
			moduleName = args[2];
		} else {
			throw new IllegalArgumentException("Error command format.");
		}
	}
	
	private void init() throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(ClassLoader.getSystemResourceAsStream(fileName));
		
		Element modules = doc.getDocumentElement();
		
		NodeList children = null;
		Node child = null;
		
		children = modules.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			child = children.item(i);
			
			if (child.getNodeType() == Document.ELEMENT_NODE) {
				Element module = (Element) child;
				
				if (module.getAttribute("name").equals(moduleName)) {
					moduleClass = module.getAttribute("class");
					moduleIp = module.getAttribute("ip");
					modulePort = Integer.parseInt(module.getAttribute("port"));
					moduleProps = new HashMap<String, String>();
					
					children = module.getChildNodes();
					for (int j = 0; j < children.getLength(); j++) {
						child = children.item(i);
						if (child.getNodeType() == Document.ELEMENT_NODE) {
							Element arg = (Element) child;
							String key = arg.getAttribute("key");
							String value = arg.getAttribute("value");
							moduleProps.put(key, value);
						}
					}
					
					return;
				}
			}
		}
		
		throw new IllegalArgumentException("Have no name matched.");
	}
	
	private String execute() throws Exception {
		String result = null;
		
		switch (option) {
		
		case STARTUP:
			instance();
			startMonitor();
			result = startupInvoke();
			break;
			
		case SHUTDOWN:
			result = remoteInvoke("shutdown");
			break;
			
		case LOCAL:
			instance();
			result = invoke(methodName);
			break;
			
		case REMOTE:
			result = remoteInvoke(methodName);
			break;
			
		}
		
		return result;
	}
	
	private void startMonitor() throws IOException {
		Thread thread = new Thread(new CommandListener(modulePort, this), "CommandListener");
		thread.setDaemon(true);
		thread.start();		
	}
	
	private void instance() throws Exception {
		clazz = ClassLoader.getSystemClassLoader().loadClass(moduleClass);
		
		Constructor<?> constructor = clazz.getConstructor(new Class<?>[] {});
		instance = (Module) constructor.newInstance(new Object[] {});
	}
	
	private String startupInvoke() throws Exception {
		return instance.startup(moduleProps);
	}
	
	String invoke(String methodName) throws Exception {
		Method method = clazz.getMethod(methodName, new Class[] {});
		String result = (String) method.invoke(instance, new Object[] {});
		
		return result;
	}
	
	private String remoteInvoke(String methodName) throws Exception {
		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		String result = null;
		try {
			socket = new Socket(moduleIp, modulePort);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(methodName);
			writer.flush();
			result = reader.readLine();
		} finally {
			if (writer != null) writer.close();
			if (reader != null) try {reader.close();} catch (IOException e) {};
			try {socket.close();} catch (IOException e) {}
		}
		return result;
	}
	
	public static void main(String[] args) {
		try {
			ModuleBootstrap instance = new ModuleBootstrap(args);
			String result = instance.execute();
			System.out.println(result);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
