package core.cluster;

import java.util.Properties;

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

	private Properties props;
	private Scheduler scheduler;

	QuartzDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute
			, boolean fairTake, QuartzProcessor processor, Properties props) {
		super(serviceMode, takerSize, takerExecute, fairTake, processor);
		this.props = props;
	}

	protected void init() throws Exception {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(props);
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
