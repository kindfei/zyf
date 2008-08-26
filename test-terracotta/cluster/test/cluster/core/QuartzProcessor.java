package test.cluster.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * QuartzProcessor
 * @author Zhangyf
 *
 */
public abstract class QuartzProcessor extends AbstractProcessor<JobExecutionContext> {

	public final void execute(JobExecutionContext context) throws JobExecutionException {
		QuartzDrivenService service = (QuartzDrivenService) ServiceFactory.getService(procName);
		service.process(context);
	}

}