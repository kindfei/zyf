package fx.cluster.core;

/**
 * Service
 * @author zhangyf
 *
 */
public interface Service {

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
