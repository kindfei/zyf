package test.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.cluster.core.tc.ClusterShareRoot;
import test.cluster.core.tc.Task;

/**
 * Basic service operation control
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractService<T> implements Service {
	
	private static final Log log = LogFactory.getLog(AbstractService.class);
	
	private ServiceMode serviceMode;
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
	public AbstractService(ServiceMode serviceMode, int executorSize, boolean acceptTask, Processor<T> processor) {
		this.serviceMode = serviceMode;
		this.executorSize = executorSize;
		this.acceptTask = acceptTask;
		this.processor = processor;
		this.procName = processor.getClass().getName();
	}
	
	public ServiceMode getServiceMode() {
		return serviceMode;
	}
	
	public Processor<T> getProcessor() {
		return processor;
	}
	
	/**
	 * Startup the service 
	 */
	public void startup() {
		new Thread(new Runnable() {

			public void run() {
				try {
					runStartup();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	/**
	 * Run startup operation.
	 * @throws Exception
	 */
	private void runStartup() throws Exception {
		log.info("[" + procName + "] starting.");
		
		if (acceptTask) {
			log.info(procName + " start executor. executorSize=" + executorSize);
			
			if (executorList == null) executorList = new ArrayList<TaskExecutor>();
			for (int i = 0; i < executorSize; i++) {
				TaskExecutor executor = new TaskExecutor();
				executor.setName(procName + "-TaskExecutor" + i);
				executorList.add(executor);
				executor.start();
			}
		}
		
		switch (serviceMode) {
		case ACTIVE_STANDBY:
			log.info("[" + procName + "] acquiring mutex...");
			tcRoot.acquireMutex(procName);
			log.info("[" + procName + "] acquired mutex...");
			break;

		case ALL_ACTIVE:
			log.info("[" + procName + "] service mode is all active.");
			break;
		}
		
		init();
		
		log.info("[" + procName + "] successfully started.");
	}

	public abstract void init() throws Exception;
	
	/**
	 * Shutdown the service 
	 */
	public void shutdown() {
		close();

		log.info("[" + procName + "] releasing mutex...");
		tcRoot.releaseMutex(procName);
		log.info("[" + procName + "] released mutex...");
		
		if (executorList != null) {
			for (TaskExecutor executor : executorList) {
				executor.end();
			}
		}
		
		if (threadPool != null) {
			threadPool.shutdown();
		}
		
		log.info("[" + procName + "] successfully stoped.");
	}
	
	public abstract void close();
	
	/**
	 * Process entry for service trigger
	 * @param t
	 */
	public synchronized void process(T t) {
		log.debug("[" + procName + "] begin processing.");
		
		List<Task> tasks = processor.masterProcess(t);
		
		if (tasks == null) {
			return;
		}
		
		for (Task task : tasks) {
			ExecuteMode executeMode = task.getExecuteMode();
			switch (executeMode) {
			case LOCAL_INVOKE:
				exeTask(task);
				break;

			case TASK_QUEUE:
				addTask(task);
				break;

			case ALL_INVOKE:
				dmiTask(task);
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
					log.debug("[" + procName + "] the task has been taken from queue. task: " + task.toString());
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
		int size = tcRoot.addTask(procName, task);
		log.debug("[" + procName + "] the task queue size=" + size);
	}
	
	/**
	 * Distributed invoke worker process
	 * @param task
	 */
	private void dmiTask(Task task) {
		tcRoot.dmiTask(procName, task);
	}

}
