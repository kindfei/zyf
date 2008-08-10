package test.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.cluster.core.tc.ClusterShareRoot;
import test.cluster.core.tc.Task;

public abstract class AbstractService<T> implements Service {
	
	private int serviceMode;
	private int executorSize;
	private boolean acceptTask;
	private Processor<T> processor;
	private String procName;

	private ClusterShareRoot tcRoot = ClusterShareRoot.instance;
	
	private List<TaskExecutor> executorList;
	private ExecutorService threadPool;

	public AbstractService(int serviceMode, int executorSize, boolean acceptTask, Processor<T> processor) {
		this.serviceMode = serviceMode;
		this.executorSize = executorSize;
		this.acceptTask = acceptTask;
		this.processor = processor;
		this.procName = processor.getClass().getName();
	}
	
	public Processor<T> getProcessor() {
		return processor;
	}

	public String startup() {
		
		if (acceptTask) {
			if (executorList == null) executorList = new ArrayList<TaskExecutor>();
			for (int i = 0; i < executorSize; i++) {
				TaskExecutor executor = new TaskExecutor();
				executorList.add(executor);
				executor.start();
			}
		}
		
		switch (serviceMode) {
		case Service.SERVICE_MODE_HA:
			tcRoot.acquireMutex(procName);
			break;

		case Service.SERVICE_MODE_HA_LB:
			
			break;
			
		default:
			break;
		}
		
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return "OK";
	}

	public abstract void init() throws Exception;

	public String shutdown() {
		close();
		
		if (executorList != null) {
			for (TaskExecutor executor : executorList) {
				executor.end();
			}
		}
		
		if (threadPool != null) {
			threadPool.shutdown();
		}
		
		return "OK";
	}
	
	public abstract void close();

	public void process(T t) {
		List<Task> tasks = processor.masterProcess(t);
		
		if (tasks == null) {
			return;
		}
		
		for (Task task : tasks) {
			int mode = task.getExecuteMode();
			switch (mode) {
			case Service.EXECUTE_MODE_INVOKE:
				exeTask(task);
				break;

			case Service.EXECUTE_MODE_P2P:
				addTask(task);
				break;

			case Service.EXECUTE_MODE_PUB:
				dmiTask(task);
				break;

			default:
				break;
			}
		}
	}
	
	private class TaskExecutor extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while (isActive) {
					Task task = tcRoot.takeTask(procName);
					try {
						processor.workerProcess(task);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void end() {
			isActive = false;
			interrupt();
		}
	}
	
	private void exeTask(final Task task) {
		if (threadPool == null) {
			threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
		}
		
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					processor.workerProcess(task);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void addTask(Task task) {
		tcRoot.addTask(procName, task);
	}
	
	private void dmiTask(Task task) {
		tcRoot.dmiTask(procName, task);
	}

}
