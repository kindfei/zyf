package test.cluster.core;

/**
 * Service
 * @author zhangyf
 *
 */
public interface Service {

	public static final int SERVICE_MODE_HA = 0;
	public static final int SERVICE_MODE_HA_LB = 1;
	
	public static final int EXECUTE_MODE_INVOKE = 0;
	public static final int EXECUTE_MODE_P2P = 1;
	public static final int EXECUTE_MODE_PUB = 2;
	
	/**
	 * Startup the service
	 * @throws Exception error when startup
	 */
	public abstract void startup() throws Exception;
	
	/**
	 * Shutdown the service
	 */
	public abstract void shutdown();

}
