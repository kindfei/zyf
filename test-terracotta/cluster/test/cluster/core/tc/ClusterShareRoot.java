package test.cluster.core.tc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterShareRoot {
	public static ClusterShareRoot instance = new ClusterShareRoot();
	
	private ClusterShareRoot() {
	}
	
	private Map<String, ClusterLock> lockMap = new HashMap<String, ClusterLock>();
	
	private Map<String, BlockingQueue<?>> queueMap = new ConcurrentHashMap<String, BlockingQueue<?>>();
	
	private Map<String, Map<String, ?>> cacheMap = new ConcurrentHashMap<String, Map<String, ?>>();
	
	
	public void acquireMutex(String name) {
		ClusterLock lock = null;
		
		synchronized (lockMap) {
			lock = lockMap.get(name);
			if (lock == null) {
				lock = new ClusterLock();
				lockMap.put(name, lock);
			}
		}
		
		System.out.println("acquiring mutex...");
		lock.acquire();
		System.out.println("released mutex...");
	}
	
	public void addTask() {
		
	}
	
}
