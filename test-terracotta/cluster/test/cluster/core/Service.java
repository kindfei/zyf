package test.cluster.core;

/**
 * Service
 * @author zhangyf
 *
 */
public interface Service {

	public static final int MODE_SERVICE_ACTIVE_STANDBY = 0;
	public static final int MODE_SERVICE_ALL_ACTIVE = 1;
	
	public static final int MODE_EXECUTE_LOCAL_INVOKE = 0;
	public static final int MODE_EXECUTE_TASK_QUEUE = 1;
	public static final int MODE_EXECUTE_ALL_INVOKE = 2;
	
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
