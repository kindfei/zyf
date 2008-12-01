package test.quartz;

import java.util.ArrayList;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DumbJob implements Job {
	public DumbJob() {
		
	}
	
	public void execute(JobExecutionContext context) 
			throws JobExecutionException {
		System.out.println("DumbJob is Executing");
		
		String instName = context.getJobDetail().getName();
		String instGroup = context.getJobDetail().getGroup();
		
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		
		String jobSay = dataMap.getString("jobSay");
		float myFloatValue = dataMap.getFloat("myFloatValue");
		ArrayList state = (ArrayList)dataMap.get("myStateDate");
		state.add(new Date());
		
		System.out.println("Instance: " + instName + " of DumbJob say: " + jobSay);
	}
}
