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
	private boolean newExecutor;
	private Processor<T> processor;
	private String procName;

	private ClusterShareRoot tcRoot = ClusterShareRoot.instance;
	
	private Thread startupThread;

	private ExecutorService threadPool;
	private List<Executor> executorList = new ArrayList<Executor>();
	
	/**
	 * Create Service
	 * @param serviceMode service mode 
	 * @param executorSize how many thread to take the task and process it
	 * @param processor business implementation
	 */
	public AbstractService(ServiceMode serviceMode, int executorSize, boolean newExecutor, Processor<T> processor) {
		this.serviceMode = serviceMode;
		this.executorSize = executorSize;
		this.newExecutor = newExecutor;
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
	public synchronized void startup() {
		if (startupThread != null) {
			log.info("The service is already started.");
			return;
		}
		
		startupThread = new Thread() {

			public synchronized void run() {
				try {
					runStartup();
					
					this.wait();
				}catch (InterruptedException e) {
					log.info("Interrupted startup thread for shutdown. processor=" + procName);
				} catch (Throwable e) {
					log.error("Startup error.", e);
				}
				
				try {
					runShutdown();
				} catch (Throwable e) {
					log.error("Shutdown error.", e);
				}
			}
			
		};
		
		startupThread.start();
	}
	
	/**
	 * Run startup operation.
	 * @throws Exception
	 */
	private void runStartup() throws Exception {
		log.info("[" + procName + "] starting.");
		
		threadPool = new ThreadPoolExecutor(0
				, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS
				, new SynchronousQueue<Runnable>());
		
		Executor executor = null;
		for (int i = 0; i < executorSize; i++) {
			executor = new Executor();
			executor.setName(procName + "-Executor" + i);
			executorList.add(executor);
			executor.start();
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
	private void runShutdown() {
		try {
			close();
		} catch (Throwable e) {
		}

		log.info("[" + procName + "] releasing mutex...");
		try {
			tcRoot.releaseMutex(procName);
		} catch (Throwable e) {
		}
		log.info("[" + procName + "] released mutex...");
		
		for (Executor executor : executorList) {
			try {
				executor.end();
			} catch (Throwable e) {
			}
		}
		
		try {
			threadPool.shutdown();
		} catch (Throwable e) {
		}
		
		log.info("[" + procName + "] successfully stoped.");
	}
	
	public synchronized void shutdown() {
		if (startupThread == null) {
			log.info("The service have never started.");
		}
		
		synchronized (startupThread) {
			startupThread.interrupt();
		}
	}
	
	public abstract void close();
	
	/**
	 * Process entry for service trigger
	 * @param t
	 */
	public void process(T t) {
		log.info("[" + procName + "] begin processing.");
		
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
	private class Executor extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while (isActive) {
					Task task = tcRoot.takeTask(procName);
					log.info("[" + procName + "] the task has been taken from queue. task: " + task.toString());
					
					if (newExecutor) {
						exeTask(task);
					} else {
						try {
							processor.workerProcess(task);
						} catch (Throwable e) {
							log.error("Worker process error.", e);
						}
					}
				}
			} catch (InterruptedException e) {
				log.info("Interrupted TaskTaker for shutdown. Name=" + getName());
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
	private synchronized void exeTask(final Task task) {
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					processor.workerProcess(task);
				} catch (Throwable e) {
					log.error("Worker process error.", e);
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
	private synchronized void dmiTask(Task task) {
		try {
			tcRoot.dmiTask(procName, task);
		} catch (Throwable e) {
			log.error("dmiTask error.", e);
		}
	}

}
