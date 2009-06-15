package test.basic.quartz;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class TestCronTrigger {
	
	public static void main(String[] args) {
	}
	
	void tset() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		JobDetail job = new JobDetail();
		CronTrigger trigger = new CronTrigger("trigger1", "group1", "job1", "group1",
				"0 0/2 8-17 * * ?");
		
		sched.addJob(job, true);
		Date ft = sched.scheduleJob(trigger);
		System.out.println(job.getFullName() + " has been scheduled to run at: " + ft
				+ " and repeat based on expression: "
				+ trigger.getCronExpression());
	}

}
