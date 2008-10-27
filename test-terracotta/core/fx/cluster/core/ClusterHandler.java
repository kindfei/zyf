package fx.cluster.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Cluster handler
 * @author zhangyf
 *
 */
public class ClusterHandler {
	private ClusterLock lock = new ClusterLock();
	private ClusterTaskQueue queue;
	private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
	private String procName;
	
	ClusterHandler(String procName, boolean fairTake) {
		this.procName = procName;
		queue = new ClusterTaskQueue(fairTake);
	}
	
	void acquireMutex() throws InterruptedException {
		lock.lockInterruptibly();
	}
	
	void releaseMutex() {
		lock.unlock();
	}
	
	boolean isFairTake() {
		return queue.isFairTake();
	}
	
	void putTask(ClusterTask task) throws InterruptedException {
		queue.put(task);
	}
	
	ClusterTask takeTask() throws InterruptedException {
		return queue.take();
	}
	
	Object getCacheValue(String key) {
		return cache.get(key);
	}
	
	void setCacheValue(String key, Object value) {
		cache.put(key, value);
	}
	
	void dmiTask(ClusterTask task) {
		AbstractService<?> service = ServiceFactory.getService(procName);
		if (service == null) {
			return;
		}
		service.execute(task);
	}
}
