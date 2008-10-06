package test.fx;

import fx.cluster.core.Service;
import fx.cluster.core.ServiceFactory;
import fx.cluster.core.ServiceMode;
import fx.service.core.ServiceEntry;

public class JMSTestService extends ServiceEntry {
	private Service service;

	public JMSTestService(int listenPort) {
		super(listenPort);
	}

	@Override
	public String shutdown() throws Exception {
		service.shutdown();
		return "Shutdown OK!";
	}

	@Override
	public String startup() throws Exception {
		service = ServiceFactory.getMessageDrivenServiceWithoutWorker(ServiceMode.ALL_ACTIVE, new JMSTestProcessor(), "queue/TestQueue", 10);
		service.startup();
		
		return "Startup OK!";
	}
	
	public static void main(String[] args) {
		JMSTestService inst = new JMSTestService(Integer.parseInt(args[1]));
		inst.process(args[0]);
	}
}
