package org.zyf.cluster;

/**
 * Service
 * @author zhangyf
 *
 */
public interface Service {
	
	public ServiceStatus getStatus();

	/**
	 * Startup the service
	 * @throws Exception error when startup
	 */
	public abstract void startup();
	
	/**
	 * Shutdown the service
	 */
	public abstract void shutdown();

}
