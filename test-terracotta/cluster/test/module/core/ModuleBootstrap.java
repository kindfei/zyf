package test.module.core;

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
	
	private String moduleName;
	private String moduleClass;
	private String moduleIp;
	private String modulePort;
	private Map<String, String> moduleProps;
	
	public ModuleBootstrap(String[] args) throws Exception {
		parse(args);
		init();
	}
	
	private void parse(String[] args) {
		int len = args.length;
		if (len < 2 || len > 3) {
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
					modulePort = module.getAttribute("port");
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
					
					break;
				}
			}
		}
		
	}
	
	private void execute() {
		
	}
	
	public static void main(String[] args) {
		try {
			ModuleBootstrap instance = new ModuleBootstrap(args);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
