package test.terracotta.all;

import org.zyf.cluster.Service;
import org.zyf.cluster.ServiceFactory;
import org.zyf.cluster.ServiceMode;
import org.zyf.entry.ServiceEntry;
import org.zyf.jmx.OPERATION;

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
