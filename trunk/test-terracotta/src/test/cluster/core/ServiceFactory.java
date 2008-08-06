package test.cluster.core;

/**
 * @uml.dependency   supplier="test.cluster.core.Service"
 */
public class ServiceFactory {

	/**
	 */
	public static Service getMessageDrivenService() {
		return null;
	}

	/**
	 */
	public static Service getTimerDrivenService() {
		return null;
	}

	public static Service getQuartzDrivenService() {
		return null;
	}

}
