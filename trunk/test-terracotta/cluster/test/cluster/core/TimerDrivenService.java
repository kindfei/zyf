package test.cluster.core;

public class TimerDrivenService extends AbstractService<Object> {
	
	private int delay;
	private int period;

	public TimerDrivenService(int mode, Processor<Object> processor, int delay, int period) {
		super(mode, processor);
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
	}

	public void close() {
	}

}
