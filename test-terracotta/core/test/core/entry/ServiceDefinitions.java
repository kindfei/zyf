package test.core.entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ServiceDefinitions {
	private static final Log log = LogFactory.getLog(ServiceDefinitions.class);
	
	private static final String fileName = "service-definition.xml";
	
	private static Map<String, ServiceInfo> services = new HashMap<String, ServiceInfo>();
	
	static {
		try {
			InputStream in = null;
			String configPath = System.getProperty("configPath");
			if (configPath != null && !configPath.equals("")) {
				in = new FileInputStream(configPath + File.separator + fileName);
            } else {
            	in = ClassLoader.getSystemResourceAsStream(fileName);
            }
			
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName
						, String qName, Attributes attributes) throws SAXException {
					if (!qName.equals("service")) {
						return;
					}

					ServiceInfo info = new ServiceInfo();
					info.setName(attributes.getValue("name"));
					info.setEntry(attributes.getValue("entry"));
					info.setIp(attributes.getValue("ip"));
					info.setPort(Integer.parseInt(attributes.getValue("port")));
					services.put(info.getName(), info);
				}
			};
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(in, handler);
		} catch (Exception e) {
			log.error("ServiceDefinitions init error.", e);
		}
	}
	
	static ServiceInfo getServiceInfo(String name) {
		return services.get(name);
	}
	
	static class ServiceInfo {
		private String name;
		private String entry;
		private String ip;
		private int port;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEntry() {
			return entry;
		}
		public void setEntry(String entry) {
			this.entry = entry;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		
		public String toString() {
		    final String TAB = "    ";
		    
		    String retValue = "";
		    
		    retValue = "ServiceInfo ( "
		        + super.toString() + TAB
		        + "name = " + this.name + TAB
		        + "entry = " + this.entry + TAB
		        + "ip = " + this.ip + TAB
		        + "port = " + this.port + TAB
		        + " )";
		
		    return retValue;
		}
	}
}
