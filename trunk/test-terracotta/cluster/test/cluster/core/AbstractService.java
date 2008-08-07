package test.cluster.core;

public abstract class AbstractService<T> implements Service {
	
	private int mode;
	private Processor<T> processor;

	public AbstractService(int mode, Processor<T> processor) {
		this.mode = mode;
		this.processor = processor;
	}
	
	public int getMode() {
		return mode;
	}
	
	public Processor<T> getProcessor() {
		return processor;
	}

	public String startup() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "OK";
	}

	public String shutdown() {
		close();
		return "OK";
	}

	public abstract void init() throws Exception;
	public abstract void close();

	public void process(T t) {
		processor.masterProcess(t);
	}

}
