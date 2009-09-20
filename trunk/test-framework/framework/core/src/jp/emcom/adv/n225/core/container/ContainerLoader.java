package jp.emcom.adv.n225.core.container;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jp.emcom.adv.n225.core.start.Startup;
import jp.emcom.adv.n225.core.start.StartupException;
import jp.emcom.adv.n225.core.start.StartupLoader;

public class ContainerLoader implements StartupLoader {
	
	private String configFile;
	
	private List<Container> loadedContainers = new LinkedList<Container>();

	public void load(Startup.Config config) throws StartupException {
		this.configFile = config.containerConfig;
		
		Collection<ContainerConfig.Container> containers = null;
        try {
            containers = ContainerConfig.getContainers(configFile);
        } catch (ContainerException e) {
            throw new StartupException(e);
        }
        
        if (containers != null) {
            for (ContainerConfig.Container containerCfg: containers) {
                Container tmpContainer = loadContainer(containerCfg);
                loadedContainers.add(tmpContainer);
            }
        }
	}
	
    private Container loadContainer(ContainerConfig.Container containerCfg) throws StartupException {
        // load the container class
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        Class<?> containerClass = null;
        try {
            containerClass = loader.loadClass(containerCfg.className);
        } catch (ClassNotFoundException e) {
            throw new StartupException("Cannot locate container class", e);
        }
        if (containerClass == null) {
            throw new StartupException("Component container class not loaded");
        }

        // create a new instance of the container object
        Container containerObj = null;
        try {
            containerObj = (Container) containerClass.newInstance();
        } catch (InstantiationException e) {
            throw new StartupException("Cannot create " + containerCfg.name, e);
        } catch (IllegalAccessException e) {
            throw new StartupException("Cannot create " + containerCfg.name, e);
        } catch (ClassCastException e) {
            throw new StartupException("Cannot create " + containerCfg.name, e);
        }

        if (containerObj == null) {
            throw new StartupException("Unable to create instance of component container");
        }

        // initialize the container object
        try {
            containerObj.init(configFile);
        } catch (ContainerException e) {
            throw new StartupException("Cannot init() " + containerCfg.name, e);
        } catch (java.lang.AbstractMethodError e) {
            throw new StartupException("Cannot init() " + containerCfg.name, e);
        }

        return containerObj;
    }

	public void start() throws StartupException {
		// start each container object
        for (Container container: loadedContainers) {
            try {
                container.start();
            } catch (ContainerException e) {
                throw new StartupException("Cannot start() " + container.getClass().getName(), e);
            } catch (java.lang.AbstractMethodError e) {
                throw new StartupException("Cannot start() " + container.getClass().getName(), e);
            }
        }
	}

	public void unload() throws StartupException {
		// shutting down in reverse order
        for (int i = loadedContainers.size(); i > 0; i--) {
            Container container = loadedContainers.get(i-1);
            try {
                container.stop();
            } catch (ContainerException e) {
            	//TODO do not throw
            }
        }
	}

}
