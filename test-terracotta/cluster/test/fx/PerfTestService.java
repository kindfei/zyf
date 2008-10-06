package test.fx;

import java.io.IOException;
import java.util.Properties;

import fx.cluster.core.Service;
import fx.cluster.core.ServiceFactory;
import fx.cluster.core.ServiceMode;
import fx.service.core.ServiceEntry;

public class PerfTestService extends ServiceEntry {
	private Service service;

	public PerfTestService(int listenPort) {
		super(listenPort);
	}

	@Override
	public String shutdown() throws Exception {
		service.shutdown();
		return "Shutdown OK";
	}

	@Override
	public String startup() throws Exception {
		boolean isFair = false;
		int takerSize = 0;
		try {
			Properties prop = new Properties();
			prop.load(ClassLoader.getSystemResourceAsStream("TestPerfProc.properties"));
			isFair = "true".equalsIgnoreCase(prop.getProperty("IS_FAIR"));
			takerSize = Integer.parseInt(prop.getProperty("TAKER_SIZE"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (isFair) {
			service = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new PerfTimerProcessor(true, 1), 1000 * 10);
		} else {
			service = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, takerSize, new PerfTimerProcessor(false, takerSize), 1000 * 10);
		}
		
		service.startup();
		return "Startup OK";
	}
	
	public static void main(String[] args) {
		PerfTestService inst = new PerfTestService(Integer.parseInt(args[1]));
		inst.process(args);
	}
}
