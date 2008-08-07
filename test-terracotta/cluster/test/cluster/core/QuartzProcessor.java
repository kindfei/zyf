package test.cluster.core;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class QuartzProcessor implements Processor<JobExecutionContext>, Job {

	private static QuartzDrivenService service;

	public static void setService(QuartzDrivenService service) {
		QuartzProcessor.service = service;
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		service.process(context);
	}

}