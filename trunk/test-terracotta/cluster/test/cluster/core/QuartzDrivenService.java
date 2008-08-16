package test.cluster.core;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * QuartzDrivenService
 * @author zhangyf
 *
 */
public class QuartzDrivenService extends AbstractService<JobExecutionContext> {

	private String propName;
	private Scheduler scheduler;

	public QuartzDrivenService(ServiceMode serviceMode, int executorSize, QuartzProcessor processor, String propName) {
		super(serviceMode, executorSize, processor);
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}

	public void init() throws Exception {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(ClassLoader.getSystemResourceAsStream(propName));
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
