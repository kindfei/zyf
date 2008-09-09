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
	
	private CommandInfo cmdInfo;
	private ModuleInfo moduleInfo;
	
	private Class<?> clazz;
	
	public ModuleBootstrap(String[] args) throws Exception {
		cmdInfo = new CommandInfo(args);
		moduleInfo = new ModuleInfo(cmdInfo.getModuleName());
		moduleInfo.load();
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
