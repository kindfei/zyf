package jp.emcom.adv.n225.core.component;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import jp.emcom.adv.n225.core.util.UtilURL;
import jp.emcom.adv.n225.core.util.UtilXml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * @author alex
 * 
 */
public class ComponentLoaderConfig {
	public static final String COMPONENT_LOAD_XML_FILENAME = "component-load.xml";

	public static final int SINGLE_COMPONENT = 0;
	public static final int COMPONENT_DIRECTORY = 1;

	protected static List<ComponentDef> componentsToLoad = null;

	public static List<ComponentDef> getRootComponents(String configFile) throws ComponentException {
		if (componentsToLoad == null) {
			synchronized (ComponentLoaderConfig.class) {
				if (componentsToLoad == null) {
					if (configFile == null) {
						configFile = COMPONENT_LOAD_XML_FILENAME;
					}
					URL xmlUrl = UtilURL.fromResource(configFile);
					ComponentLoaderConfig.componentsToLoad = ComponentLoaderConfig.getComponentsFromConfig(xmlUrl);
				}
			}
		}
		return componentsToLoad;
	}

	public static List<ComponentDef> getComponentsFromConfig(URL configUrl) throws ComponentException {
		if (configUrl == null) {
			throw new ComponentException("Component config file does not exist: " + configUrl);
		}

		List<ComponentDef> componentsFromConfig = null;
		Document document = null;
		try {
			document = UtilXml.readXmlDocument(configUrl);
		} catch (SAXException e) {
			throw new ComponentException("Error reading the component config file: " + configUrl, e);
		} catch (ParserConfigurationException e) {
			throw new ComponentException("Error reading the component config file: " + configUrl, e);
		} catch (IOException e) {
			throw new ComponentException("Error reading the component config file: " + configUrl, e);
		}

		Element root = document.getDocumentElement();
		List<? extends Element> toLoad = UtilXml.childElementList(root);
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
				location = element.getAttribute("component-location"); //TODO
				type = SINGLE_COMPONENT;
			} else if ("load-components".equals(element.getNodeName())) {
				name = null;
				location = element.getAttribute("parent-directory"); // TODO
				type = COMPONENT_DIRECTORY;
			}
		}
	}
}
