package test.cluster.core;

public interface Service {

	public static final int SERVICE_MODE_HA = 0;
	public static final int SERVICE_MODE_HA_LB = 1;
	
	public static final int EXECUTE_MODE_INVOKE = 0;
	public static final int EXECUTE_MODE_P2P = 1;
	public static final int EXECUTE_MODE_PUB = 2;

	public abstract String startup();

	public abstract String shutdown();

}
