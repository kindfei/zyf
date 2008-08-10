package test.cluster.core.tc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import test.cluster.core.Processor;
import test.cluster.core.ServiceFactory;

public class ClusterShareRoot {
	public static ClusterShareRoot instance = new ClusterShareRoot();
	
	private ClusterShareRoot() {
	}
	
	private Map<String, ClusterLock> lockMap = new HashMap<String, ClusterLock>();
	
	private Map<String, BlockingQueue<Task>> queueMap = new HashMap<String, BlockingQueue<Task>>();
	
	private Map<String, Map<String, Object>> cacheMap = new HashMap<String, Map<String, Object>>();
	
	
	public void acquireMutex(String procName) {
		ClusterLock lock = lockMap.get(procName);
		if (lock == null) {
			lock = this.<ClusterLock>addValue(procName, lockMap, new ClusterLock());
		}
		
		System.out.println("acquiring mutex...");
		lock.acquire();
		System.out.println("released mutex...");
	}
	
	public void addTask(String procName, Task task) {
		BlockingQueue<Task> queue = queueMap.get(procName);
		if (queue == null) {
			queue = this.<BlockingQueue<Task>>addValue(procName, queueMap, new LinkedBlockingQueue<Task>());
		}
		
		queue.add(task);
	}
	
	public Task takeTask(String procName) throws InterruptedException {
		BlockingQueue<Task> queue = queueMap.get(procName);
		if (queue == null) {
			queue = this.<BlockingQueue<Task>>addValue(procName, queueMap, new LinkedBlockingQueue<Task>());
		}
		
		Task task = queue.take();
		
		return task;
	}
	
	public Object getCache(String procName, String key) {
		Map<String, Object> cache = cacheMap.get(procName);
		if (cache == null) {
			cache = this.<Map<String, Object>>addValue(procName, cacheMap, new ConcurrentHashMap<String, Object>());
		}
		
		return cache.get(key);
	}
	
	public void setCache(String procName, String key, Object value) {
		Map<String, Object> cache = cacheMap.get(procName);
		if (cache == null) {
			cache = this.<Map<String, Object>>addValue(procName, cacheMap, new ConcurrentHashMap<String, Object>());
		}
		
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
	
	private <V> V addValue(String procName, Map<String, V> map, V newV) {
		V v = null;
		synchronized (map) {
			v = map.get(procName);
			if (v == null) {
				v = newV;
				map.put(procName, newV);
			}
		}
		return v;
	}
}
