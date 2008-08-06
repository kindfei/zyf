package test.cluster.core;

public abstract class AbstractProcessor implements Processor {
	
	private int workerSize;
	
	public AbstractProcessor(int workerSize) {
		this.workerSize = workerSize;
	}
	
	public int getWorkerSize() {
		return workerSize;
	}

}
