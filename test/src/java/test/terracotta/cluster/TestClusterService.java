package test.terracotta.cluster;

import incubation.cluster.Service;
import incubation.cluster.ServiceFactory;
import incubation.cluster.ServiceMode;
import incubation.jms.MessageDestination;
import incubation.utils.CmdHelper;

import java.io.IOException;
import java.util.Properties;







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
		Service servive = ServiceFactory.getMessageDrivenService(ServiceMode.ALL_ACTIVE, new TestMessageProcessor(), MessageDestination.testQueue);
		
		servive.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive.shutdown();
	}
	
	public static void testDMI() {
		Service servive1 = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestDmiProcessor(), 1000);
		Service servive2 = ServiceFactory.getTimerDrivenService(ServiceMode.ACTIVE_STANDBY, new TestLiProcessor(), 1000);
		
		servive1.startup();
		servive2.startup();
		
		CmdHelper.pause("Shutdown.........");
		
		servive1.shutdown();
		servive2.shutdown();
	}
	
	public static void main(String[] args) {
		testDMI();
	}
	
}


