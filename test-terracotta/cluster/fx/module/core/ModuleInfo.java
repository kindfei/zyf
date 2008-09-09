package fx.module.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModuleInfo {
	
	private static final String fileName = "module-definition.xml";
	
	private String moduleName;
	
	private String className;
	private String ipAddress;
	private int listenPort;
	
	private Object instance;
	
	private List<CommandInfo> commands = new ArrayList<CommandInfo>();
	
	ModuleInfo(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public void load() throws Exception {
		loadModule();
		loadCommands();
	}
	
	private void loadModule() throws Exception {
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
					className = module.getAttribute("class");
					ipAddress = module.getAttribute("ip");
					listenPort = Integer.parseInt(module.getAttribute("port"));
					
					return;
				}
			}
		}
		
		throw new IllegalArgumentException("Have no name matched.");
	}
	
	private void loadCommands() throws Exception {
		Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			Command cmd = method.getAnnotation(Command.class);
			if (cmd != null) {
				commands.add(new CommandInfo(cmd.value(), cmd.type(), cmd.description(), method));
			}
		}
		Constructor<?> constructor = clazz.getConstructor(new Class<?>[] {});
		instance = constructor.newInstance(new Object[] {});
	}
	
	String invoke(String strCmd) throws Exception {
		Method method = clazz.getMethod(methodName, new Class[] {});
		String result = (String) method.invoke(instance, new Object[] {});
		
		return result;
	}
	
	private class CommandInfo {
		private String value;
		private CommandType type;
		private String desc;
		private Method method;
		
		public CommandInfo(String value, CommandType type, String desc, Method method) {
			super();
			this.value = value;
			this.type = type;
			this.desc = desc;
			this.method = method;
		}
		
		public String getValue() {
			return value;
		}
		public CommandType getType() {
			return type;
		}
		public String getDesc() {
			return desc;
		}
		public Method getMethod() {
			return method;
		}
	}
}
