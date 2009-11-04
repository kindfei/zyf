package incubation.app.component;

import incubation.app.util.UtilURL;
import incubation.app.util.UtilXml;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author zhangyf
 * 
 */
public class ComponentLoaderConfig {
	public static final String COMPONENT_LOAD_XML_FILENAME = "component-load.xml";

	public static final int SINGLE_COMPONENT = 0;
	public static final int COMPONENT_DIRECTORY = 1;

	protected static List<ComponentDef> componentsToLoad = null;

	public static List<ComponentDef> getRootComponents(String configFile) throws Exception {
		if (componentsToLoad == null) {
			synchronized (ComponentLoaderConfig.class) {
				if (componentsToLoad == null) {
					if (configFile == null) {
						configFile = COMPONENT_LOAD_XML_FILENAME;
					}
					URL xmlUrl = UtilURL.fromResource(configFile); // TODO
					ComponentLoaderConfig.componentsToLoad = ComponentLoaderConfig.getComponentsFromConfig(xmlUrl);
				}
			}
		}
		return componentsToLoad;
	}

	public static List<ComponentDef> getComponentsFromConfig(URL configUrl) throws Exception {
		if (configUrl == null) {
			throw new Exception("Component config file does not exist: " + configUrl);
		}

		Document document = UtilXml.readXmlDocument(configUrl);
		Element root = document.getDocumentElement();
		List<? extends Element> toLoad = UtilXml.childElementList(root);

		List<ComponentDef> componentsFromConfig = null;

		if (toLoad != null && toLoad.size() > 0) {
			componentsFromConfig = new LinkedList<ComponentDef>();
			for (Element element : toLoad) {
				componentsFromConfig.add(new ComponentDef(element));
			}
		}
		return componentsFromConfig;
	}

	public static class ComponentDef {
		public String name;
		public String location;
		public int type = -1;

		public ComponentDef(Element element) {
			if ("load-component".equals(element.getNodeName())) {
				name = element.getAttribute("component-name");
				location = element.getAttribute("component-location"); // TODO
				type = SINGLE_COMPONENT;
			} else if ("load-components".equals(element.getNodeName())) {
				name = null;
				location = element.getAttribute("parent-directory"); // TODO
				type = COMPONENT_DIRECTORY;
			}
		}
	}
}
