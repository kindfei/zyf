package test;

import test.cluster.core.Service;
import test.cluster.core.ServiceFactory;
import test.cluster.core.ServiceMode;

public class TestClusterService {
	
	private Service service1;
	private Service service2;
	
	public TestClusterService() {
		service1 = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, 2, true, new TestTimerProcessor(), 0, 2000);
		service2 = ServiceFactory.getQuartzDrivenService(ServiceMode.ACTIVE_STANDBY, 2, true, new TestQuartzProcessor(), "quartz_test.properties");
	}
	
	public void startup() {
		service1.startup();
		service2.startup();
	}
	
	public void shutdown() {
		service1.shutdown();
		service2.shutdown();
	}
	
	public static void main(String[] args) {
		TestClusterService test = new TestClusterService();
		test.startup();
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.shutdown();
	}

}


