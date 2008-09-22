package fx.cluster.core;

import java.util.concurrent.ConcurrentHashMap;

public class ClusterHandler {
	private ClusterLock lock = new ClusterLock();
	private TaskQueue queue = new TaskQueue();
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
	
	void addTask(Task task) {
		queue.addTask(task);
	}
	
	Task takeTask(boolean fairTake) throws InterruptedException {
		Task task = null;
		if (fairTake) {
			task = queue.fairTake();
		} else {
			task = queue.take();
		}
		
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
		}
	}
}
