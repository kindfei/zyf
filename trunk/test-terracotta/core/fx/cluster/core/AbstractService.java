package fx.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
	private Processor<T> processor;
	private String procName;

	private ClusterLock lock;
	private ClusterTaskQueue queue;
	private ClusterExecutor executor;
	
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
		this.processor = processor;
		this.procName = processor.getClass().getName();
		
		lock = ClusterHandlerFactory.getClusterLock(procName);
		queue = ClusterHandlerFactory.getClusterTaskQueue(procName, fairTake);
		executor = ClusterHandlerFactory.getClusterExecutor(procName);
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
					
				} catch (InterruptedException e) {
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
	 * WorkThreadFactory
	 * @author zhangyf
	 *
	 */
    private static class WorkThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        WorkThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                                 Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + name + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
    
	/**
	 * Run startup operation.
	 * @throws Exception
	 */
	private void runStartup() throws Exception {
		threadPool = new ThreadPoolExecutor(0
				, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS
				, new SynchronousQueue<Runnable>(), new WorkThreadFactory(procName));
		
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
			lock.acquireMutex();
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
			lock.releaseMutex();
		} catch (Throwable e) {
		}
		log.info("Released mutex... processor=" + procName);
		
		for (TaskTaker taker : takerList) {
			taker.end();
		}
		
//		threadPool.shutdown();
		
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
			List<ClusterTask> tasks = processor.masterProcess(t);
			
			if (tasks == null) {
				return;
			}
			
			for (ClusterTask task : tasks) {
				ExecuteMode executeMode = task.getExecuteMode();
				switch (executeMode) {
				case LOCAL_INVOKE:
					exeTask(task);
					break;

				case TASK_QUEUE:
					putTask(task);
					break;

				case ALL_INVOKE:
					dmiTask(task);
					break;
				}
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
		
		public TaskTaker() {
			this.setDaemon(true);
		}
		
		public void run() {
			try {
				while (isActive) {
					ClusterTask task = queue.takeTask();
					
					if (takerExecute) {
						execute(task);
					} else {
						exeTask(task);
					}
				}
			} catch (InterruptedException e) {
				log.info("Interrupted TaskTaker for shutdown. Name=" + getName());
			} catch (Throwable e) {
				log.error("TaskTaker error. Name=" + getName(), e);
			}
		}
		
		public void end() {
			isActive = false;
		}
	}
	
	/**
	 * Execute worker process with the task.
	 * @param task
	 */
	void execute(ClusterTask task) {
		try {
			processor.workerProcess(task);
		} catch (Throwable e) {
			log.error("Worker process error. processor=" + procName, e);
		}
	}
	
	/**
	 * Local invoke worker process
	 * @param task
	 */
	private synchronized void exeTask(final ClusterTask task) {
		threadPool.execute(new Runnable() {
			public void run() {
				execute(task);
			}
		});
	}
	
	/**
	 * Add task to task queue
	 * @param task
	 * @throws InterruptedException 
	 */
	private void putTask(ClusterTask task) throws InterruptedException {
		queue.putTask(task);
	}
	
	/**
	 * Distributed invoke worker process
	 * @param task
	 */
	private synchronized void dmiTask(ClusterTask task) {
		executor.execute(task);
	}

}
