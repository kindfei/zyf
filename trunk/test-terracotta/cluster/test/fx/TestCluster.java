package test.fx;

import fx.cluster.core.Service;
import fx.cluster.core.ServiceFactory;
import fx.cluster.core.ServiceMode;
import fx.service.core.ServiceEntry;

public class TestCluster extends ServiceEntry {
	private Service service1;

	public TestCluster(int listenPort) {
		super(listenPort);
	}

	@Override
	public String shutdown() throws Exception {
		service1.shutdown();
		return "Shutdown OK";
	}

	@Override
	public String startup() throws Exception {
//		service1 = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestPerformanceProcessor(), 1000 * 30);
		service1 = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, 10, new TestPerformanceProcessor(), 1000 * 30);
		service1.startup();
		return "Startup OK";
	}
	
	public static void main(String[] args) {
		TestCluster inst = new TestCluster(Integer.parseInt(args[1]));
		inst.process(args);
	}
}
