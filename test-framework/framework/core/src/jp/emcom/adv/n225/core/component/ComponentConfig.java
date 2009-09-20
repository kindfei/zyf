package jp.emcom.adv.n225.core.component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jp.emcom.adv.n225.core.util.UtilURL;
import jp.emcom.adv.n225.core.util.UtilXml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * @author alex
 *
 */
public class ComponentConfig {
	
	private static Logger log;//TODO

    public static final String COMPONENT_XML_FILENAME = "component.xml";

    protected static Map<String, ComponentConfig> componentConfigs = new HashMap<String, ComponentConfig>();

    public static ComponentConfig getComponentConfig(String globalName) throws ComponentException {
        return getComponentConfig(globalName, null);
    }

    public static ComponentConfig getComponentConfig(String globalName, String rootLocation) throws ComponentException {
        ComponentConfig componentConfig = null;
        if (globalName != null && globalName.length() > 0) {
            componentConfig = componentConfigs.get(globalName);
        }
        if (componentConfig == null) {
            if (rootLocation != null) {
                synchronized (ComponentConfig.class) {
                    if (globalName != null && globalName.length() > 0) {
                        componentConfig = componentConfigs.get(globalName);
                    }
                    if (componentConfig == null) {
                        componentConfig = new ComponentConfig(globalName, rootLocation);
                        if (componentConfig.enabled()) {
                            componentConfigs.put(componentConfig.getGlobalName(), componentConfig);
                        }
                    }
                }
            } else {
                throw new ComponentException("No component found named : " + globalName);
            }
        }
        return componentConfig;
    }

    public static Boolean componentExists(String componentName) {
        ComponentConfig componentConfig = componentConfigs.get(componentName);
        if (componentConfig == null) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    
    public static Collection<ComponentConfig> getAllComponents() {
        Collection<ComponentConfig> values = componentConfigs.values();
        if (values != null) {
            return values;
        } else {
            return new ArrayList<ComponentConfig>(0);
        }
    }

    public static List<ClasspathInfo> getAllClasspathInfos() {
        return getAllClasspathInfos(null);
    }

    public static List<ClasspathInfo> getAllClasspathInfos(String componentName) {
        List<ClasspathInfo> classpaths = new ArrayList<ClasspathInfo>();
        for (ComponentConfig cc: getAllComponents()) {
            if (componentName == null || componentName.equals(cc.getComponentName())) {
                classpaths.addAll(cc.getClasspathInfos());
            }
        }
        return classpaths;
    }

    public static List<ServiceResourceInfo> getAllServiceResourceInfos(String type) {
        return getAllServiceResourceInfos(type, null);
    }

    public static List<ServiceResourceInfo> getAllServiceResourceInfos(String type, String componentName) {
        List<ServiceResourceInfo> serviceInfos = new ArrayList<ServiceResourceInfo>();
        for (ComponentConfig cc: getAllComponents()) {
            if (componentName == null || componentName.equals(cc.getComponentName())) {
                List<ServiceResourceInfo> ccServiceInfoList = cc.getServiceResourceInfos();
                if (type == null || type.length() == 0) {
                    serviceInfos.addAll(ccServiceInfoList);
                } else {
                    for (ServiceResourceInfo serviceResourceInfo: ccServiceInfoList) {
                        if (type.equals(serviceResourceInfo.type)) {
                            serviceInfos.add(serviceResourceInfo);
                        }
                    }
                }
            }
        }
        return serviceInfos;
    }

    public static boolean isFileResourceLoader(String componentName, String resourceLoaderName) throws ComponentException {
        ComponentConfig cc = ComponentConfig.getComponentConfig(componentName);
        if (cc == null) {
            throw new ComponentException("Could not find component with name: " + componentName);
        }
        return cc.isFileResourceLoader(resourceLoaderName);
    }

    public static InputStream getStream(String componentName, String resourceLoaderName, String location) throws ComponentException {
        ComponentConfig cc = ComponentConfig.getComponentConfig(componentName);
        if (cc == null) {
            throw new ComponentException("Could not find component with name: " + componentName);
        }
        return cc.getStream(resourceLoaderName, location);
    }

    public static URL getURL(String componentName, String resourceLoaderName, String location) throws ComponentException {
        ComponentConfig cc = ComponentConfig.getComponentConfig(componentName);
        if (cc == null) {
            throw new ComponentException("Could not find component with name: " + componentName);
        }
        return cc.getURL(resourceLoaderName, location);
    }

    public static String getFullLocation(String componentName, String resourceLoaderName, String location) throws ComponentException {
        ComponentConfig cc = ComponentConfig.getComponentConfig(componentName);
        if (cc == null) {
            throw new ComponentException("Could not find component with name: " + componentName);
        }
        return cc.getFullLocation(resourceLoaderName, location);
    }

    public static String getRootLocation(String componentName) throws ComponentException {
        ComponentConfig cc = ComponentConfig.getComponentConfig(componentName);
        if (cc == null) {
            throw new ComponentException("Could not find component with name: " + componentName);
        }
        return cc.getRootLocation();
    }


    // ========== component info fields ==========
    protected String globalName = null;
    protected String rootLocation = null;
    protected String componentName = null;
    protected boolean enabled = true;
    
    protected Map<String, ResourceLoaderInfo> resourceLoaderInfos = new HashMap<String, ResourceLoaderInfo>();
    protected List<ClasspathInfo> classpathInfos = new ArrayList<ClasspathInfo>();
    protected List<ServiceResourceInfo> serviceResourceInfos = new ArrayList<ServiceResourceInfo>();

    protected ComponentConfig() {
    }

    protected ComponentConfig(String globalName, String rootLocation) throws ComponentException {
        this.globalName = globalName;
        if (!rootLocation.endsWith("/")) {
            rootLocation = rootLocation + "/";
        }
        this.rootLocation = rootLocation.replace('\\', '/');

        File rootLocationDir = new File(rootLocation);
        if (rootLocationDir == null) {
            throw new ComponentException("The given component root location is does not exist: " + rootLocation);
        }
        if (!rootLocationDir.isDirectory()) {
            throw new ComponentException("The given component root location is not a directory: " + rootLocation);
        }

        String xmlFilename = rootLocation + "/" + COMPONENT_XML_FILENAME;
        URL xmlUrl = UtilURL.fromFilename(xmlFilename);
        if (xmlUrl == null) {
            throw new ComponentException("Could not find the " + COMPONENT_XML_FILENAME + " configuration file in the component root location: " + rootLocation);
        }

        Document componentDocument = null;
        try {
            componentDocument = UtilXml.readXmlDocument(xmlUrl);
        } catch (SAXException e) {
            throw new ComponentException("Error reading the component config file: " + xmlUrl, e);
        } catch (ParserConfigurationException e) {
            throw new ComponentException("Error reading the component config file: " + xmlUrl, e);
        } catch (IOException e) {
            throw new ComponentException("Error reading the component config file: " + xmlUrl, e);
        }

        Element componentElement = componentDocument.getDocumentElement();
        this.componentName = componentElement.getAttribute("name");
        this.enabled = "true".equalsIgnoreCase(componentElement.getAttribute("enabled"));
        if (globalName == null || globalName.length() == 0) {
            this.globalName = this.componentName;
        }

        // resource-loader - resourceLoaderInfos
        for (Element curElement: UtilXml.childElementList(componentElement, "resource-loader")) {
            ResourceLoaderInfo resourceLoaderInfo = new ResourceLoaderInfo(curElement);
            this.resourceLoaderInfos.put(resourceLoaderInfo.name, resourceLoaderInfo);
        }

        // classpath - classpathInfos
        for (Element curElement: UtilXml.childElementList(componentElement, "classpath")) {
            ClasspathInfo classpathInfo = new ClasspathInfo(this, curElement);
            this.classpathInfos.add(classpathInfo);
        }

        // service-resource - serviceResourceInfos
        for (Element curElement: UtilXml.childElementList(componentElement, "service-resource")) {
            ServiceResourceInfo serviceResourceInfo = new ServiceResourceInfo(this, curElement);
            this.serviceResourceInfos.add(serviceResourceInfo);
        }
    }

    public boolean isFileResourceLoader(String resourceLoaderName) throws ComponentException {
        ResourceLoaderInfo resourceLoaderInfo = resourceLoaderInfos.get(resourceLoaderName);
        if (resourceLoaderInfo == null) {
            throw new ComponentException("Could not find resource-loader named: " + resourceLoaderName);
        }
        return "file".equals(resourceLoaderInfo.type) || "component".equals(resourceLoaderInfo.type);
    }

    public InputStream getStream(String resourceLoaderName, String location) throws ComponentException {
        URL url = getURL(resourceLoaderName, location);
        try {
            return url.openStream();
        } catch (java.io.IOException e) {
            throw new ComponentException("Error opening resource at location [" + url.toExternalForm() + "]", e);
        }
    }

    public URL getURL(String resourceLoaderName, String location) throws ComponentException {
        ResourceLoaderInfo resourceLoaderInfo = resourceLoaderInfos.get(resourceLoaderName);
        if (resourceLoaderInfo == null) {
            throw new ComponentException("Could not find resource-loader named: " + resourceLoaderName);
        }

        if ("component".equals(resourceLoaderInfo.type) || "file".equals(resourceLoaderInfo.type)) {
            String fullLocation = getFullLocation(resourceLoaderName, location);
            URL fileUrl = UtilURL.fromFilename(fullLocation);
            if (fileUrl == null) {
                throw new ComponentException("File Resource not found: " + fullLocation);
            }
            return fileUrl;
        } else if ("classpath".equals(resourceLoaderInfo.type)) {
            String fullLocation = getFullLocation(resourceLoaderName, location);
            URL url = UtilURL.fromResource(fullLocation);
            if (url == null) {
                throw new ComponentException("Classpath Resource not found: " + fullLocation);
            }
            return url;
        } else if ("url".equals(resourceLoaderInfo.type)) {
            String fullLocation = getFullLocation(resourceLoaderName, location);
            URL url = null;
            try {
                url = new URL(location); //TODO
            } catch (java.net.MalformedURLException e) {
                throw new ComponentException("Error with malformed URL while trying to load URL resource at location [" + fullLocation + "]", e);
            }
            if (url == null) {
                throw new ComponentException("URL Resource not found: " + fullLocation);
            }
            return url;
        } else {
            throw new ComponentException("The resource-loader type is not recognized: " + resourceLoaderInfo.type);
        }
    }

    public String getFullLocation(String resourceLoaderName, String location) throws ComponentException {
        ResourceLoaderInfo resourceLoaderInfo = resourceLoaderInfos.get(resourceLoaderName);
        if (resourceLoaderInfo == null) {
            throw new ComponentException("Could not find resource-loader named: " + resourceLoaderName);
        }

        StringBuilder buf = new StringBuilder();

        // pre-pend component root location if this is a type component resource-loader
        if ("component".equals(resourceLoaderInfo.type)) {
            buf.append(rootLocation);
        }

        if (resourceLoaderInfo.prependEnv != null && resourceLoaderInfo.prependEnv.length() > 0) {
            String propValue = System.getProperty(resourceLoaderInfo.prependEnv);
            if (propValue == null) {
                String errMsg = "The Java environment (-Dxxx=yyy) variable with name " + resourceLoaderInfo.prependEnv + " is not set, cannot load resource.";
                log.error(errMsg);
                throw new IllegalArgumentException(errMsg);
            }
            buf.append(propValue);
        }
        if (resourceLoaderInfo.prefix != null && resourceLoaderInfo.prefix.length() > 0) {
            buf.append(resourceLoaderInfo.prefix);
        }
        buf.append(location);
        return buf.toString();
    }
    
    public List<ClasspathInfo> getClasspathInfos() {
        return this.classpathInfos;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public String getGlobalName() {
        return this.globalName;
    }

    public String getRootLocation() {
        return this.rootLocation;
    }

    public List<ServiceResourceInfo> getServiceResourceInfos() {
        return this.serviceResourceInfos;
    }

    public boolean enabled() {
        return this.enabled;
    }
    
    public static class ResourceLoaderInfo {
        public String name;
        public String type;
        public String prependEnv;
        public String prefix;

        public ResourceLoaderInfo(Element element) {
            this.name = element.getAttribute("name");
            this.type = element.getAttribute("type");
            this.prependEnv = element.getAttribute("prepend-env");
            this.prefix = element.getAttribute("prefix");
        }
    }
    
    public static class ResourceInfo {
        public ComponentConfig componentConfig;
        public String loader;
        public String location;

        public ResourceInfo(ComponentConfig componentConfig, Element element) {
            this.componentConfig = componentConfig;
            this.loader = element.getAttribute("loader");
            this.location = element.getAttribute("location");
        }

        public ComponentResourceHandler createResourceHandler() {
            return new ComponentResourceHandler(componentConfig.getGlobalName(), loader, location);
        }

        public String getLocation() {
            return location;
        }
    }

    public static class ClasspathInfo {
        public ComponentConfig componentConfig;
        public String type;
        public String location;

        public ClasspathInfo(ComponentConfig componentConfig, Element element) {
            this.componentConfig = componentConfig;
            this.type = element.getAttribute("type");
            this.location = element.getAttribute("location");
        }
    }

    public static class ServiceResourceInfo extends ResourceInfo {
        public String type;

        public ServiceResourceInfo(ComponentConfig componentConfig, Element element) {
            super(componentConfig, element);
            this.type = element.getAttribute("type");
        }
    }
}
