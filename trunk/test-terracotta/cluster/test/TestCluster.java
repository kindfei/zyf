package test;

import test.cluster.core.Service;
import test.cluster.core.ServiceFactory;

public class TestCluster {
	
	private Service service1;
	private Service service2;
	
	public TestCluster() {
		service1 = ServiceFactory.getTimerDrivenService(Service.MODE_SERVICE_ACTIVE_STANDBY, 2, true, new TestTimerProcessor(), 0, 2000);
		service2 = ServiceFactory.getQuartzDrivenService(Service.MODE_SERVICE_ACTIVE_STANDBY, 2, true, new TestQuartzProcessor(), "quartz_test.properties");
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
		TestCluster test = new TestCluster();
		test.startup();
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.shutdown();
	}

}


