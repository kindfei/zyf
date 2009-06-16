package zyf.entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ServiceDefinition {
	private static Log log = LogFactory.getLog(ServiceDefinition.class);
	
	private static final String fileName = "service-definitions.xml";
	
	private String serviceName;
	private String className;
	private String ipAddress;
	private int listenPort;
	private boolean startJMX;
	private int registerPort;
	private Map<String, String> props = new ConcurrentHashMap<String, String>();
	
	private static Map<String, ServiceDefinition> definitions = new HashMap<String, ServiceDefinition>();
	
	static {
		DefaultHandler handler = new DefaultHandler() {
			private ServiceDefinition current;
			
			public void startElement(String uri, String localName
					, String qName, Attributes attributes) throws SAXException {
				
				if (qName.equals("service")) {
					String serviceName = attributes.getValue("name");
					String className = attributes.getValue("class");
					String ipAddress = attributes.getValue("ip");
					int listenPort = Integer.parseInt(attributes.getValue("port"));
					boolean startJMX = Boolean.parseBoolean(attributes.getValue("jmx"));
					int registerPort = Integer.parseInt(attributes.getValue("rmi"));
					
					ServiceDefinition definition = new ServiceDefinition(serviceName,
							className, ipAddress, listenPort, startJMX, registerPort);
					
					current = definition;
					definitions.put(serviceName, definition);
				} else if (qName.equals("property")) {
					String key = attributes.getValue("key");
					String value = attributes.getValue("value");
					
					current.setProperty(key, value);
				}
			}
		};
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(ClassLoader.getSystemResourceAsStream(fileName), handler);
		} catch (Exception e) {
			log.error("Load service definitions error.", e);
		}
	}
	
	public static ServiceDefinition getServiceDefinition() {
		return definitions.get(System.getProperty("SERVICE_NAME"));
	}
	
	static ServiceDefinition getDefWithClassName(String className) {
		for (ServiceDefinition definition : definitions.values()) {
			if (className.equals(definition.getClassName())) {
				return definition;
			}
		}
		return null;
	}

	private ServiceDefinition(String serviceName, String className,
			String ipAddress, int listenPort, boolean startJMX, int registerPort) {
		this.serviceName = serviceName;
		this.className = className;
		this.ipAddress = ipAddress;
		this.listenPort = listenPort;
		this.startJMX = startJMX;
		this.registerPort = registerPort;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getClassName() {
		return className;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getListenPort() {
		return listenPort;
	}

	public boolean isStartJMX() {
		return startJMX;
	}

	public int getRegisterPort() {
		return registerPort;
	}
	
	private void setProperty(String key, String value) {
		props.put(key, value);
	}
	
	public String getProperty(String key) {
		return props.get(key);
	}
	
	public Collection<String> propertyValues() {
		return props.values();
	}
	
	public Set<String> perpertyKeySet() {
		return props.keySet();
	}
}
