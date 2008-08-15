package test.cluster.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer Driven Service
 * @author zhangyf
 *
 */
public class TimerDrivenService extends AbstractService<Object> {
	
	private int delay;
	private int period;
	
	private Timer timer;

	public TimerDrivenService(ServiceMode serviceMode, int executorSize, boolean acceptTask, TimerProcessor processor, int delay, int period) {
		super(serviceMode, executorSize, acceptTask, processor);
		this.delay = delay;
		this.period = period;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void init() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				process(null);
			}
		}, delay, period);
	}

	public void close() {
		timer.cancel();
	}

}
