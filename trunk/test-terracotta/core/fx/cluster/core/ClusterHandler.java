package fx.cluster.core;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClusterHandler {
	private static final Log log = LogFactory.getLog(ClusterHandler.class);
	
	private ClusterLock lock = new ClusterLock();
	private ClusterTaskQueue queue = new ClusterTaskQueue();
	private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
	private String procName;
	
	ClusterHandler(String procName) {
		this.procName = procName;
	}
	
	void acquireMutex() throws InterruptedException {
		lock.lockInterruptibly();
	}
	
	void releaseMutex() {
		lock.unlock();
	}
	
	void putTask(Task task) throws InterruptedException {
		queue.put(task);
	}
	
	Task takeTask(boolean fairTake) throws InterruptedException {
		Task task = null;
		task = queue.take();
		
		return task;
	}
	
	Object getCacheValue(String key) {
		return cache.get(key);
	}
	
	void setCacheValue(String key, Object value) {
		cache.put(key, value);
	}
	
	void dmiTask(Task task) {
		try {
			Processor<?> processor = ServiceFactory.getProcessor(procName);
			if (processor == null) {
				return;
			}
			processor.workerProcess(task);
		} catch (Throwable e) {
			log.error("Worker process error. processor=" + procName, e);
		}
	}
}
