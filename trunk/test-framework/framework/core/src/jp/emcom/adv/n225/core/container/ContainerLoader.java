package jp.emcom.adv.n225.core.container;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jp.emcom.adv.n225.core.start.Startup;
import jp.emcom.adv.n225.core.start.StartupLoader;

/**
 * 
 * @author zhangyf
 * 
 */
public class ContainerLoader implements StartupLoader {

	private String configFile;

	private List<Container> loadedContainers = new LinkedList<Container>();

	public void load(Startup.Config config) throws Exception {
		this.configFile = config.containerConfig;

		Collection<ContainerConfig.Container> containers = ContainerConfig.getContainers(configFile);

		if (containers != null) {
			for (ContainerConfig.Container containerCfg : containers) {
				loadedContainers.add(loadContainer(containerCfg));
			}
		}
	}

	private Container loadContainer(ContainerConfig.Container containerCfg) throws Exception {
		// load the container class
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}

		Class<?> containerClass = loader.loadClass(containerCfg.className);

		if (containerClass == null) {
			throw new Exception("Component container class not loaded");
		}

		// create a new instance of the container object
		Container containerObj = (Container) containerClass.newInstance();

		if (containerObj == null) {
			throw new Exception("Unable to create instance of component container");
		}

		// initialize the container object
		containerObj.init(configFile);

		return containerObj;
	}

	public void start() throws Exception {
		// start each container object
		for (Container container : loadedContainers) {
			container.start();
		}
	}

	public void unload() throws Exception {
		// shutting down in reverse order
		for (int i = loadedContainers.size(); i > 0; i--) {
			Container container = loadedContainers.get(i - 1);
			try {
				container.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
