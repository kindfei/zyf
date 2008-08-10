package test.cluster.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class QuartzProcessor extends AbstractProcessor<JobExecutionContext> {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		QuartzDrivenService service = (QuartzDrivenService) ServiceFactory.getService(procName);
		service.process(context);
	}

}