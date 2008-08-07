package test.cluster.core;

public class TimerDrivenService extends AbstractService<Object> {

	public TimerDrivenService(int mode, Processor<Object> processor, int delay, int period) {
		super(mode, processor);
	}

	public void init() {
	}

	public void close() {
	}

}
