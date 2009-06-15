package test.basic.quartz;

import java.text.DateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleJob implements Job {
	
	public void execute(JobExecutionContext context) 
			throws JobExecutionException {
		
		String str = (String)context.getJobDetail().getJobDataMap().get("append");
		
		System.out.println("\n_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/" 
					+ "\n" + "_/_/_/ " + str + " SimpleJob is running at " + DateFormat.getTimeInstance().format(new Date())
					+ "\n" + "_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/");
	}
	
	public static void main(String[] args) throws Exception {
		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		System.out.println("\n*******************************"
					+ "\n" + "*   Initialization Complete   *"
					+ "\n" + "*******************************");
		
		sched.start();
		
		System.out.println("\n*******************************"
					+ "\n" + "*       Started Scheduler     *"
					+ "\n" + "*******************************");
	}
}
