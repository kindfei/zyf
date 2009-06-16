package test.terracotta.all;

import zyf.cluster.Service;
import zyf.cluster.ServiceFactory;
import zyf.cluster.ServiceMode;
import zyf.entry.ServiceEntry;
import zyf.jmx.OPERATION;

public class TestTimerEntry extends ServiceEntry {
	
	private Service service;

	@Override
	public String shutdown() throws Exception {
		service.shutdown();
		return null;
	}

	@Override
	public String startup() throws Exception {
		service = ServiceFactory.getTimerDrivenService(ServiceMode.ALL_ACTIVE, new TestTimerProcessor(), 1000);
		service.startup();
		return null;
	}
	
	@OPERATION
	public void restart() throws Exception {
		shutdown();
		startup();
	}
}
