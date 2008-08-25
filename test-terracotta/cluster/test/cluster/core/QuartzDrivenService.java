package test.cluster.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	private static final Log log = LogFactory.getLog(QuartzDrivenService.class);

	private String propName;
	private Scheduler scheduler;

	QuartzDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute, QuartzProcessor processor, String propName) {
		super(serviceMode, takerSize, takerExecute, processor);
		this.propName = propName;
	}

	protected void init() throws Exception {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(ClassLoader.getSystemResourceAsStream(propName));
		scheduler = factory.getScheduler();
		scheduler.start();
	}

	protected void close() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error("Quartz Scheduler shutdown error.", e);
		}
	}

}
