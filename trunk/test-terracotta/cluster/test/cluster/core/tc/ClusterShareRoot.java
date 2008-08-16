package test.cluster.core.tc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import test.cluster.core.Processor;
import test.cluster.core.ServiceFactory;

public class ClusterShareRoot {
	public static final ClusterShareRoot instance = new ClusterShareRoot();
	
	private ClusterShareRoot() {
	}
	
	private Map<String, Lock> lockMap = new HashMap<String, Lock>();
	
	private Map<String, BlockingQueue<Task>> queueMap = new HashMap<String, BlockingQueue<Task>>();
	
	private Map<String, Map<String, Object>> cacheMap = new HashMap<String, Map<String, Object>>();
	
	
	public void acquireMutex(String procName) throws InterruptedException {
		Lock lock = getLock(procName);
		
		lock.lockInterruptibly();
	}
	
	public void releaseMutex(String procName) {
		Lock lock = getLock(procName);
		
		lock.unlock();
	}
	
	public int addTask(String procName, Task task) {
		BlockingQueue<Task> queue = getQueue(procName);
		
		queue.add(task);
		return queue.size();
	}
	
	public Task takeTask(String procName) throws InterruptedException {
		BlockingQueue<Task> queue = getQueue(procName);
		
		Task task = queue.take();
		
		return task;
	}
	
	public Object getCacheValue(String procName, String key) {
		Map<String, Object> cache = getCache(procName);
		
		return cache.get(key);
	}
	
	public void setCacheValue(String procName, String key, Object value) {
		Map<String, Object> cache = getCache(procName);
		
		cache.put(key, value);
	}
	
	public void dmiTask(String procName, Task task) {
		Processor<?> processor = ServiceFactory.getProcessor(procName);
		if (processor == null) {
			return;
		}
		processor.workerProcess(task);
	}
	
	private Lock getLock(String procName) {
		Lock lock = lockMap.get(procName);
		if (lock == null) {
			lock = this.<Lock>addValue(procName, lockMap, new ReentrantLock());
		}
		return lock;
	}
	
	private BlockingQueue<Task> getQueue(String procName) {
		BlockingQueue<Task> queue = queueMap.get(procName);
		if (queue == null) {
			queue = this.<BlockingQueue<Task>>addValue(procName, queueMap, new LinkedBlockingQueue<Task>());
		}
		return queue;
	}
	
	private Map<String, Object> getCache(String procName) {
		Map<String, Object> cache = cacheMap.get(procName);
		if (cache == null) {
			cache = this.<Map<String, Object>>addValue(procName, cacheMap, new ConcurrentHashMap<String, Object>());
		}
		return cache;
	}
	
	private <V> V addValue(String procName, Map<String, V> map, V newV) {
		V v = null;
		synchronized (map) {
			v = map.get(procName);
			if (v == null) {
				v = newV;
				map.put(procName, v);
			}
		}
		return v;
	}
}
