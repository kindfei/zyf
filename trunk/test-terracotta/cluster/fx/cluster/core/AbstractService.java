package fx.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Basic service operation control
 * @author zhangyf
 *
 * @param <T>
 */
public abstract class AbstractService<T> implements Service {
	
	private static final Log log = LogFactory.getLog(AbstractService.class);
	
	private ServiceMode serviceMode;
	private int takerSize;
	private boolean takerExecute;
	private boolean fairTake;
	private Processor<T> processor;
	private String procName;

	private ClusterHandler handler;
	
	private Thread startupThread;

	private ExecutorService threadPool;
	private List<TaskTaker> takerList = new ArrayList<TaskTaker>();
	
	/**
	 * Create Service
	 * @param serviceMode service mode 
	 * @param takerSize how many thread to take the task
	 * @param takerExecute whether execute inside of the taker thread
	 * @param fairTake whether use fair mode when take task from the queue
	 * @param processor business implementation
	 */
	protected AbstractService(ServiceMode serviceMode, int takerSize, boolean takerExecute, boolean fairTake, Processor<T> processor) {
		this.serviceMode = serviceMode;
		this.takerSize = takerSize;
		this.takerExecute = takerExecute;
		this.fairTake = fairTake;
		this.processor = processor;
		this.procName = processor.getClass().getName();
		
		handler = ClusterHandlerFactory.instance.getClusterHandler(procName);
	}
	
	Processor<T> getProcessor() {
		return processor;
	}
	
	/**
	 * Startup the service 
	 */
	public synchronized void startup() {
		if (startupThread != null && startupThread.isAlive()) {
			log.info("The service is already started. processor=" + procName);
			return;
		}
		
		startupThread = new Thread() {

			public void run() {
				try {
					runStartup();
					
					synchronized (this) {
						this.wait();
					}
					
				}catch (InterruptedException e) {
					log.info("Interrupted startup thread for shutdown. Name=" + getName());
				} catch (Throwable e) {
					log.error("Startup error. processor=" + procName, e);
				}
				
				try {
					runShutdown();
				} catch (Throwable e) {
					log.error("Shutdown error. processor=" + procName, e);
				}
				
				startupThread = null;
			}
			
		};
		
		startupThread.setName(procName + "-StartupThread");
		startupThread.start();
	}

	/**
	 * Shutdown the service 
	 */
	public synchronized void shutdown() {
		if (startupThread == null) {
			log.info("The service have never started. processor=" + procName);
		}
		startupThread.interrupt();
	}
	
	/**
	 * Run startup operation.
	 * @throws Exception
	 */
	private void runStartup() throws Exception {
		threadPool = new ThreadPoolExecutor(0
				, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS
				, new SynchronousQueue<Runnable>());
		
		TaskTaker taker = null;
		for (int i = 0; i < takerSize; i++) {
			taker = new TaskTaker();
			taker.setName(procName + "-TaskTaker" + i);
			takerList.add(taker);
			taker.start();
		}
		
		switch (serviceMode) {
		case ACTIVE_STANDBY:
			log.info("Acquiring mutex... processor=" + procName);
			handler.acquireMutex();
			log.info("Acquired mutex... processor=" + procName);
			break;

		case ALL_ACTIVE:
			log.info("Service mode is all active. processor=" + procName);
			break;
		}
		
		init();
		
		log.info("Successfully started. processor=" + procName);
	}
	
	/**
	 * Run shutdown operation.
	 */
	private void runShutdown() {
		try {
			close();
		} catch (Throwable e) {
		}

		log.info("Releasing mutex... processor=" + procName);
		try {
			handler.releaseMutex();
		} catch (Throwable e) {
		}
		log.info("Released mutex... processor=" + procName);
		
		for (TaskTaker taker : takerList) {
			taker.end();
		}
		
		threadPool.shutdown();
		
		log.info("Successfully stoped. processor=" + procName);
	}
	
	/**
	 * Initialize the service.
	 * @throws Exception
	 */
	protected abstract void init() throws Exception;
	
	/**
	 * Close the service.
	 */
	protected abstract void close();
	
	/**
	 * Process entry for service trigger
	 * @param t
	 */
	protected void process(T t) {
		try {
			List<Task> tasks = processor.masterProcess(t);
			
			if (tasks == null) {
				return;
			}
			
			List<Task> list = new ArrayList<Task>();
			for (Task task : tasks) {
				ExecuteMode executeMode = task.getExecuteMode();
				switch (executeMode) {
				case LOCAL_INVOKE:
					exeTask(task);
					break;

				case TASK_QUEUE:
					list.add(task);
					break;

				case ALL_INVOKE:
					dmiTask(task);
					break;
				}
			}
			
			if (list.size() != 0) {
				addTask(list);
			}
		} catch (Throwable e) {
			log.error("Master process error. processor=" + procName, e);
		}
	}
	
	/**
	 * Take task from share queue for worker process.
	 * @author zhangyf
	 *
	 */
	private class TaskTaker extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while (isActive) {
					Task task = handler.takeTask(fairTake);
					
					if (takerExecute) {
						execute(task);
					} else {
						exeTask(task);
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
	 * Execute worker process with the task.
	 * @param task
	 */
	private void execute(Task task) {
		try {
			ReentrantLock groupLock = task.getGroupLock();
			Condition finish = task.getFinish();
			if (groupLock != null && finish != null) {
				groupLock.lock();
				try {
					processor.workerProcess(task);
				} finally {
					finish.signalAll();
					groupLock.unlock();
				}
			} else {
				processor.workerProcess(task);
			}
		} catch (Throwable e) {
			log.error("Worker process error. processor=" + procName, e);
		}
	}
	
	/**
	 * Local invoke worker process
	 * @param task
	 */
	private synchronized void exeTask(final Task task) {
		threadPool.execute(new Runnable() {
			public void run() {
				execute(task);
			}
		});
	}
	
	/**
	 * Add task to task queue
	 * @param task
	 */
	private void addTask(List<Task> tasks) {
		handler.addTask(tasks);
	}
	
	/**
	 * Distributed invoke worker process
	 * @param task
	 */
	private synchronized void dmiTask(Task task) {
		handler.dmiTask(task);
	}

}
