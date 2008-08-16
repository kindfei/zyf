package test.cluster.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer Driven Service
 * @author zhangyf
 *
 */
public class TimerDrivenService extends AbstractService<Object> {
	
	private int period;
	
	private Timer timer;

	public TimerDrivenService(ServiceMode serviceMode, int executorSize, TimerProcessor processor, int period) {
		super(serviceMode, executorSize, processor);
		this.period = period;
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
		}, 0, period);
	}

	public void close() {
		timer.cancel();
	}

}
