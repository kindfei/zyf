package test.all;

import core.cluster.Service;
import core.cluster.ServiceFactory;
import core.cluster.ServiceMode;
import core.entry.ServiceEntry;
import core.jmx.OPERATION;

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
