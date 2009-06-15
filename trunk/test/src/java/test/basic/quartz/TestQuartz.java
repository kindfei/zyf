package test.basic.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;

public class TestQuartz {
	
	public static void main(String args[]) {
		TestQuartz test = new TestQuartz();
		test.test();
	}
	
	void test() {
		try {
			SchedulerFactory schedFact = new StdSchedulerFactory();
			Scheduler sched= schedFact.getScheduler();
			sched.start();
			
			JobDetail jobDetail = new JobDetail("MyJob", null, DumbJob.class);
			jobDetail.getJobDataMap().put("jobSay", "Hello World!");
			jobDetail.getJobDataMap().put("myFloatValue", 3.14f);
			jobDetail.getJobDataMap().put("myStateDate", new ArrayList());
			
			Trigger trigger = TriggerUtils.makeSecondlyTrigger();
			trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
			trigger.setName("myTrigger");
			
			sched.scheduleJob(jobDetail, trigger);
			sched.isPaused();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	void testCalendar() {
		try {
			SchedulerFactory schedFact = new StdSchedulerFactory();
			Scheduler sched = null;
	
			sched = schedFact.getScheduler();
			sched.start();
			HolidayCalendar cal = new HolidayCalendar();
			cal.addExcludedDate(new Date());

			sched.addCalendar("myHolidays", cal, false, false);

			Trigger trigger = TriggerUtils.makeSecondlyTrigger(); // fire every one hour interval
			trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));  // start on the next even hour
			trigger.setName("myTrigger1");

			trigger.setCalendarName("myHolidays");

//			 .. schedule job with trigger

			Trigger trigger2 = TriggerUtils.makeDailyTrigger(8, 0); // fire every day at 08:00
			trigger.setStartTime(new Date()); // begin immediately
			trigger2.setName("myTrigger2");

			trigger2.setCalendarName("myHolidays");

//			 .. schedule job with trigger2
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	void testSimpleTrigger() {
		
		// Create a trigger that fires exactly once, ten seconds from now
		long startTime = System.currentTimeMillis() + 10000L;
		SimpleTrigger trigger1 = new SimpleTrigger("myTrigger",
													null,
													new Date(startTime),
													null,
													0,
													0L);
		  
		// Create a trigger that fires immediately, then repeats every 60 seconds, forever
		SimpleTrigger trigger2 = new SimpleTrigger("myTrigger",
									                null,
									                new Date(),
									                null,
									                SimpleTrigger.REPEAT_INDEFINITELY,
									                60L * 1000L);
		
		// Create a trigger that fires immediately, then repeats every 10 seconds until 40 seconds from now
		long endTime = System.currentTimeMillis() + 40000L;
		SimpleTrigger trigger3 = new SimpleTrigger("myTrigger",
													"myGroup",
													new Date(),
													new Date(endTime),
													SimpleTrigger.REPEAT_INDEFINITELY,
													10L * 1000L);
		
		// Create a trigger that fires on March 17 of the year 2002 at precisely 10:30 am, 
		// and repeats 5 times (for a total of 6 firings) - with a 30 second delay between each firing
		java.util.Calendar cal = new java.util.GregorianCalendar(2002, Calendar.MARCH, 17);
		cal.set(cal.HOUR, 10);
		cal.set(cal.MINUTE, 30);
		cal.set(cal.SECOND, 0);
		cal.set(cal.MILLISECOND, 0);

		Date startTime2 = cal.getTime();

		SimpleTrigger trigger4 = new SimpleTrigger("myTrigger",
													null,
													startTime2,
													null,
													5,
													30L * 1000L);
	}
	
	class MySchedulerListener implements SchedulerListener {

		public void jobScheduled(Trigger trigger) {
			// TODO Auto-generated method stub
			
		}

		public void jobUnscheduled(String triggerName, String triggerGroup) {
			// TODO Auto-generated method stub
			
		}

		public void triggerFinalized(Trigger trigger) {
			// TODO Auto-generated method stub
			
		}

		public void triggersPaused(String triggerName, String triggerGroup) {
			// TODO Auto-generated method stub
			
		}

		public void triggersResumed(String triggerName, String triggerGroup) {
			// TODO Auto-generated method stub
			
		}

		public void jobsPaused(String jobName, String jobGroup) {
			// TODO Auto-generated method stub
			
		}

		public void jobsResumed(String jobName, String jobGroup) {
			// TODO Auto-generated method stub
			
		}

		public void schedulerError(String msg, SchedulerException cause) {
			// TODO Auto-generated method stub
			
		}

		public void schedulerShutdown() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class MyJobListener implements JobListener {

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void jobToBeExecuted(JobExecutionContext context) {
			// TODO Auto-generated method stub
			
		}

		public void jobExecutionVetoed(JobExecutionContext context) {
			// TODO Auto-generated method stub
			
		}

		public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class MyTriggerListener implements org.quartz.TriggerListener {

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void triggerFired(Trigger trigger, JobExecutionContext context) {
			// TODO Auto-generated method stub
			
		}

		public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
			// TODO Auto-generated method stub
			return false;
		}

		public void triggerMisfired(Trigger trigger) {
			// TODO Auto-generated method stub
			
		}

		public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
