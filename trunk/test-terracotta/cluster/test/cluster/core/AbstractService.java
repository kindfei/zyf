package test.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import test.cluster.core.tc.ClusterShareRoot;
import test.cluster.core.tc.Task;

/**
 * Basic service operation control
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractService<T> implements Service {
	
	private int serviceMode;
	private int executorSize;
	private boolean acceptTask;
	private Processor<T> processor;
	private String procName;

	private ClusterShareRoot tcRoot = ClusterShareRoot.instance;
	
	private List<TaskExecutor> executorList;
	private ExecutorService threadPool;
	
	/**
	 * Create Service
	 * @param serviceMode service mode 
	 * @param executorSize how many thread to take the task and process it
	 * @param acceptTask whether take the task from the share queue
	 * @param processor business implementation
	 */
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
	
	/**
	 * Startup the service 
	 * @throws Exception 
	 */
	public void startup() throws Exception {
		
		if (acceptTask) {
			if (executorList == null) executorList = new ArrayList<TaskExecutor>();
			for (int i = 0; i < executorSize; i++) {
				TaskExecutor executor = new TaskExecutor();
				executor.setName(procName + "-TaskExecutor" + i);
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
		
		init();
	}

	public abstract void init() throws Exception;
	
	/**
	 * Shutdown the service 
	 */
	public void shutdown() {
		close();
		
		tcRoot.releaseMutex(procName);
		
		if (executorList != null) {
			for (TaskExecutor executor : executorList) {
				executor.end();
			}
		}
		
		if (threadPool != null) {
			threadPool.shutdown();
		}
	}
	
	public abstract void close();
	
	/**
	 * Process entry for service trigger
	 * @param t
	 */
	public synchronized void process(T t) {
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
	
	/**
	 * Take task from share queue for worker
	 * @author zhangyf
	 *
	 */
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
	
	/**
	 * Local invoke worker process
	 * @param task
	 */
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
	
	/**
	 * Add task to share queue
	 * @param task
	 */
	private void addTask(Task task) {
		tcRoot.addTask(procName, task);
	}
	
	/**
	 * Distributed invoke worker process
	 * @param task
	 */
	private void dmiTask(Task task) {
		tcRoot.dmiTask(procName, task);
	}

}
