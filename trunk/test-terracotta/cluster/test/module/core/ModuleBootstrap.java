package test.module.core;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModuleBootstrap {
	private static final String fileName = "module-definition.xml";
	
	private void read(String name) throws Exception {
		
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
				
				if (module.getAttribute("name").equals(name)) {
					String strClass = module.getAttribute("class");
					String strIp = module.getAttribute("ip");
					String strPort = module.getAttribute("port");
					StringBuffer arg = new StringBuffer();
					
					children = module.getChildNodes();
					for (int j = 0; j < children.getLength(); j++) {
						child = children.item(i);
						if (child.getNodeType() == Document.ELEMENT_NODE) {
							Element jvmarg = (Element) child;
							arg.append(jvmarg.getAttribute("value")).append(" ");
						}
					}
					
					break;
				}
			}
		}
		
	}
}
