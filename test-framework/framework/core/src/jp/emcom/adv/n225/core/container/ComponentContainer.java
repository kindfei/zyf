package jp.emcom.adv.n225.core.container;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.emcom.adv.n225.core.component.ComponentConfig;
import jp.emcom.adv.n225.core.component.ComponentException;
import jp.emcom.adv.n225.core.component.ComponentLoaderConfig;
import jp.emcom.adv.n225.core.start.Classpath;

/**
 * 
 * @author zhangyf
 * 
 */
public class ComponentContainer implements Container {
	private static Logger log = LoggerFactory.getLogger(ComponentContainer.class);

	private Classpath classPath = new Classpath(System.getProperty("java.class.path"));
	private boolean loaded = false;

	public void init(String configFile) throws Exception {
		// get the config for this container
		ContainerConfig.Container cc = ContainerConfig.getContainer("component-container", configFile);

		// check for an override loader config
		String loaderConfig = null;
		if (cc.getProperty("loader-config") != null) {
			loaderConfig = cc.getProperty("loader-config").value;
		}

		// check for en override update classpath
		boolean updateClassPath = true;
		if (cc.getProperty("update-classpath") != null) {
			updateClassPath = "true".equalsIgnoreCase(cc.getProperty("update-classpath").value);
		}

		// load the components
		loadComponents(loaderConfig, updateClassPath);
	}

	public boolean start() throws Exception {
		return false;
	}

	public void stop() throws Exception {

	}

	private synchronized void loadComponents(String loaderConfig, boolean updateClasspath) throws Exception {
		if (!loaded) {
			loaded = true;
		} else {
			throw new Exception("Components already loaded, cannot start");
		}

		// get the components to load
		List<ComponentLoaderConfig.ComponentDef> components = ComponentLoaderConfig.getRootComponents(loaderConfig);

		String parentPath = System.getProperty("application.home"); // TODO

		// load each component
		if (components != null) {
			for (ComponentLoaderConfig.ComponentDef def : components) {
				this.loadComponentFromConfig(parentPath, def);
			}
		}

		// set the new classloader/classpath on the current thread
		if (updateClasspath) {
			System.setProperty("java.class.path", classPath.toString());
			ClassLoader cl = classPath.getClassLoader();
			Thread.currentThread().setContextClassLoader(cl);
		}

		log.info("All components loaded");
	}

	private void loadComponentFromConfig(String parentPath, ComponentLoaderConfig.ComponentDef def) {
		String location;
		if (def.location.startsWith("/")) {
			location = def.location;
		} else {
			location = parentPath + "/" + def.location;
		}
		if (def.type == ComponentLoaderConfig.SINGLE_COMPONENT) {
			ComponentConfig config = null;
			try {
				config = ComponentConfig.getComponentConfig(def.name, location);
				if (def.name == null || def.name.length() == 0) {
					def.name = config.getGlobalName();
				}
			} catch (Exception e) {
				log.error("Cannot load component : " + def.name + " @ " + def.location + " : " + e.getMessage());
			}
			if (config == null) {
				log.error("Cannot load component : " + def.name + " @ " + def.location);
			} else {
				this.loadComponent(config);
			}
		} else if (def.type == ComponentLoaderConfig.COMPONENT_DIRECTORY) {
			this.loadComponentDirectory(location);
		}
	}

	private void loadComponentDirectory(String directoryName) {
		log.info("Auto-Loading component directory : [" + directoryName + "]");
		File parentPath = new File(directoryName);// TODO
		if (!parentPath.exists() || !parentPath.isDirectory()) {
			log.error("Auto-Load Component directory not found : " + directoryName);
		} else {
			File componentLoadConfig = new File(parentPath, "component-load.xml");
			if (componentLoadConfig != null && componentLoadConfig.exists()) {
				URL configUrl = null;
				try {
					configUrl = componentLoadConfig.toURI().toURL();
					List<ComponentLoaderConfig.ComponentDef> componentsToLoad = ComponentLoaderConfig.getComponentsFromConfig(configUrl);
					if (componentsToLoad != null) {
						for (ComponentLoaderConfig.ComponentDef def : componentsToLoad) {
							this.loadComponentFromConfig(parentPath.toString(), def);
						}
					}
				} catch (MalformedURLException e) {
					log.error("Unable to locate URL for component loading file: " + componentLoadConfig.getAbsolutePath(), e);
				} catch (Exception e) {
					log.error("Unable to load components from URL: " + configUrl.toExternalForm(), e);
				}
			} else {
				for (String sub : parentPath.list()) {
					try {
						File componentPath = new File(parentPath.getCanonicalPath() + "/" + sub); // TODO
						if (componentPath.isDirectory()) {
							// make sure we have a component configuraton file
							String componentLocation = componentPath.getCanonicalPath();
							File configFile = new File(componentLocation + "/component.xml");// TODO
							if (configFile.exists()) {
								ComponentConfig config = null;
								try {
									// pass null for the name, will default to
									// the internal component name
									config = ComponentConfig.getComponentConfig(null, componentLocation);
								} catch (ComponentException e) {
									log.error("Cannot load component : " + componentPath.getName() + " @ " + componentLocation + " : " + e.getMessage(), e);
								}
								if (config == null) {
									log.error("Cannot load component : " + componentPath.getName() + " @ " + componentLocation);
								} else {
									loadComponent(config);
								}
							}
						}
					} catch (IOException e) {
						log.error("Load components error.", e);
					}
				}
			}
		}
	}

	private void loadComponent(ComponentConfig config) {
		// make sure the component is enabled
		if (!config.enabled()) {
			log.info("Not Loading component : [" + config.getComponentName() + "] (disabled)");
			return;
		}

		log.info("Loading component : [" + config.getComponentName() + "]");
		List<ComponentConfig.ClasspathInfo> classpathInfos = config.getClasspathInfos();
		String configRoot = config.getRootLocation();
		configRoot = configRoot.replace('\\', '/');
		// set the root to have a trailing slash
		if (!configRoot.endsWith("/")) {
			configRoot = configRoot + "/";
		}
		if (classpathInfos != null) {
			for (ComponentConfig.ClasspathInfo cp : classpathInfos) {
				String location = cp.location.replace('\\', '/');
				// set the location to not have a leading slash
				if (location.startsWith("/")) {
					location = location.substring(1);
				}
				if ("dir".equals(cp.type)) {
					classPath.addComponent(configRoot + location);
				} else if ("jar".equals(cp.type)) {
					String dirLoc = location;
					if (dirLoc.endsWith("/*")) {
						// strip off the slash splat
						dirLoc = location.substring(0, location.length() - 2);
					}
					File path = new File(configRoot + dirLoc);// TODO
					if (path.exists()) {
						if (path.isDirectory()) {
							// load all .jar and .zip files in this directory
							File[] files = path.listFiles();
							for (File file : files) {
								String fileName = file.getName();
								if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
									classPath.addComponent(file);
								}
							}
						} else {
							// add a single file
							classPath.addComponent(configRoot + location);
						}
					} else {
						log.warn("Location '" + configRoot + dirLoc + "' does not exist");
					}
				} else {
					log.error("Classpath type '" + cp.type + "' is not supported; '" + location + "' not loaded");
				}
			}
		}
	}

}
