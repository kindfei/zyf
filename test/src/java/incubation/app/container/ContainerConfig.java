package incubation.app.container;

import incubation.app.util.UtilURL;
import incubation.app.util.UtilXml;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author zhangyf
 * 
 */
public class ContainerConfig {

	protected static Map<String, Container> containers = new LinkedHashMap<String, Container>();

	public static Container getContainer(String containerName, String configFile) throws Exception {
		Container container = containers.get(containerName);
		if (container == null) {
			synchronized (ContainerConfig.class) {
				container = containers.get(containerName);
				if (container == null) {
					if (configFile == null) {
						throw new Exception("Container config file cannot be null");
					}
					new ContainerConfig(configFile);
					container = containers.get(containerName);
				}
			}
			if (container == null) {
				throw new Exception("No container found with the name: " + containerName);
			}
		}
		return container;
	}

	public static Collection<Container> getContainers(String configFile) throws Exception {
		if (containers.size() == 0) {
			synchronized (ContainerConfig.class) {
				if (containers.size() == 0) {
					if (configFile == null) {
						throw new Exception("Container config file cannot be null");
					}
					new ContainerConfig(configFile);
				}
			}
			if (containers.size() == 0) {
				throw new Exception("No containers loaded, problem with configuration");
			}
		}
		return containers.values();
	}

	protected ContainerConfig(String configFileLocation) throws Exception {
		// load the config file
		URL xmlUrl = UtilURL.fromResource(configFileLocation);
		if (xmlUrl == null) {
			throw new Exception("Could not find " + configFileLocation + " master container configuration");
		}

		// read the document
		Document containerDocument = UtilXml.readXmlDocument(xmlUrl);

		// root element
		Element root = containerDocument.getDocumentElement();

		// containers
		for (Element curElement : UtilXml.childElementList(root, "container")) {
			Container container = new Container(curElement);
			containers.put(container.name, container);
		}
	}

	public static class Container {
		public String name;
		public String className;
		public Map<String, Property> properties;

		public Container(Element element) {
			this.name = element.getAttribute("name");
			this.className = element.getAttribute("class");

			properties = new LinkedHashMap<String, Property>();
			for (Element curElement : UtilXml.childElementList(element, "property")) {
				Property property = new Property(curElement);
				properties.put(property.name, property);
			}
		}

		public Property getProperty(String name) {
			return properties.get(name);
		}

		public static class Property {
			public String name;
			public String value;
			public Map<String, Property> properties;

			public Property(Element element) {
				this.name = element.getAttribute("name");
				this.value = element.getAttribute("value");
				if (value == null || value.length() == 0) {
					this.value = UtilXml.childElementValue(element, "property-value");
				}

				properties = new LinkedHashMap<String, Property>();
				for (Element curElement : UtilXml.childElementList(element, "property")) {
					Property property = new Property(curElement);
					properties.put(property.name, property);
				}
			}

			public Property getProperty(String name) {
				return properties.get(name);
			}
		}
	}
}
