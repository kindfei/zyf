package test.quartz;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class TestPlugin {

	public static void main(String[] args) throws Exception {
		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		System.out.println("*******************************"
				  + "\n" + "*   Initialization Complete   *"
				  + "\n" + "*******************************");
		
		sched.start();
		
		System.out.println("*******************************"
				  + "\n" + "*       Started Scheduler     *"
				  + "\n" + "*******************************");
	}

}
