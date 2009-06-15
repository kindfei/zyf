package core.cluster;

import java.util.Timer;
import java.util.TimerTask;

import core.jmx.ATTRIBUTE;

/**
 * TimerDrivenService
 * @author zhangyf
 *
 */
public class TimerDrivenService extends AbstractService<Object> {
	
	@ATTRIBUTE
	private long period;
	private Timer timer;

	TimerDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute
			, boolean fairTake, TimerProcessor processor, long period) {
		super(serviceMode, takerSize, takerExecute, fairTake, processor);
		this.period = period;
	}
	
	protected void init() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				process(null);
			}
		}, 0, period);
	}

	protected void close() {
		timer.cancel();
	}

}
