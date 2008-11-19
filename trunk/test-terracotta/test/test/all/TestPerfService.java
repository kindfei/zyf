package test.all;

import test.core.cluster.Service;
import test.core.cluster.ServiceFactory;
import test.core.cluster.ServiceMode;
import test.core.entry.ServiceEntry;

public class TestPerfService extends ServiceEntry {
	private Service service;
	private String[] params;

	public TestPerfService(int listenPort, String[] params) {
		super(listenPort);
		this.params = params;
	}

	@Override
	public String shutdown() throws Exception {
		service.shutdown();
		return "Shutdown OK";
	}

	@Override
	public String startup() throws Exception {
		int takerSize = Integer.parseInt(params[2]);
		int msgCount = Integer.parseInt(params[3]);
		int msgSize = Integer.parseInt(params[4]);
		
		if (takerSize > 0) {
			service = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, takerSize, new TestPerfTimerProcessor(takerSize, msgCount, msgSize), 1000 * 10);
		} else {
			service = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestPerfTimerProcessor(takerSize, msgCount, msgSize), 1000 * 10);
		}
		
		service.startup();
		return "Startup OK";
	}
	
	public static void main(String[] args) {
		TestPerfService inst = new TestPerfService(Integer.parseInt(args[1]), args);
		inst.process(args[0]);
	}
}
