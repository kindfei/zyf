package test.cluster;

import java.io.IOException;
import java.util.Properties;

import fx.cluster.core.Service;
import fx.cluster.core.ServiceFactory;
import fx.cluster.core.ServiceMode;

import zyf.helper.CmdHelper;

public class TestClusterService {
	
	public static void testTimer() {
		Service servive = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestTimerProcessor(), 1000);
		
		servive.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive.shutdown();
	}
	
	public static void testQuartz() throws IOException {
		Properties prop = new Properties();
		prop.load(ClassLoader.getSystemResourceAsStream("quartz_test.properties"));
		Service servive = ServiceFactory.getQuartzDrivenService(ServiceMode.ACTIVE_STANDBY, new TestQuartzProcessor(), prop);
		
		servive.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive.shutdown();
	}
	
	public static void testJMS() {
		Service servive = ServiceFactory.getMessageDrivenService(ServiceMode.ALL_ACTIVE, new TestMessageProcessor(), "queue/TestQueue");
		
		servive.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive.shutdown();
	}
	
	public static void testGroup() {
		Service servive = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestGroupTimerProcessor(), 500);
		
		servive.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive.shutdown();
	}
	
	public static void main(String[] args) {
		testGroup();
	}

}


