package test.cluster.core;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;


public class QuartzDrivenService extends AbstractService<JobExecutionContext> {
	
	private String propName;
	private Scheduler scheduler;

	public QuartzDrivenService(int mode, QuartzProcessor processor, String propName) {
		super(mode, processor);
		this.propName = propName;
	}
	
	public String getPropName() {
		return propName;
	}

	public void init() throws Exception {
		QuartzProcessor.setService(this);
		StdSchedulerFactory factory = new StdSchedulerFactory();
		scheduler = factory.getScheduler();
		scheduler.start();
	}

	public void close() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
		}
	}

}
