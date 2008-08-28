package test.cluster.core.tc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.BlockingQueue;

import test.cluster.core.Processor;
import test.cluster.core.ServiceFactory;

/**
 * ClusterShareRoot
 * @author zhangyf
 *
 */
public class ClusterShareRoot {
	public static final ClusterShareRoot instance = new ClusterShareRoot();
	
	private ClusterShareRoot() {
	}
	
	private Map<String, Lock> lockMap = new HashMap<String, Lock>();
	
	private Map<String, TaskQueue> queueMap = new HashMap<String, TaskQueue>();
	
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
	
	public Task takeTask(String procName, boolean fairTake) throws InterruptedException {
		TaskQueue queue = getQueue(procName);
		
		Task task = null;
		if (fairTake) {
			task = queue.fairTake();
		} else {
			task = queue.take();
		}
		
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
		try {
			Processor<?> processor = ServiceFactory.getProcessor(procName);
			if (processor == null) {
				return;
			}
			processor.workerProcess(task);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private Lock getLock(String procName) {
		Lock lock = lockMap.get(procName);
		if (lock == null) {
			lock = this.<Lock>addValue(procName, lockMap, new ReentrantLock());
		}
		return lock;
	}
	
	private TaskQueue getQueue(String procName) {
		TaskQueue queue = queueMap.get(procName);
		if (queue == null) {
			queue = this.<TaskQueue>addValue(procName, queueMap, new TaskQueue());
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
