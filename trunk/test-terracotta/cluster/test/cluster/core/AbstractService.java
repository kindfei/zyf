package test.cluster.core;

import java.util.List;

import test.cluster.core.tc.ClusterShareRoot;
import test.cluster.core.tc.Task;

public abstract class AbstractService<T> implements Service {
	
	private int mode;
	private Processor<T> processor;
	private ClusterShareRoot root = ClusterShareRoot.instance;

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
		root.acquireMutex("");
		
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "OK";
	}

	public abstract void init() throws Exception;

	public String shutdown() {
		close();
		return "OK";
	}
	
	public abstract void close();

	public void process(T t) {
		List<Task> tasks = processor.masterProcess(t);
		
	}
	
	private void addTask(List<Task> tasks) {
		
	}
	
	private void exeTask(List<Task> tasks) {
		for (Task task : tasks) {
			processor.workerProcess(task);
		}
	}

}
