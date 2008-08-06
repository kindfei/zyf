package test.cluster.core.tc;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterShareRoot {
	public static ClusterShareRoot instance = new ClusterShareRoot();
	
	private ClusterShareRoot() {
	}
	
	private ConcurrentHashMap<String, ClusterLock> lockMap = new ConcurrentHashMap<String, ClusterLock>();
	
	private ConcurrentHashMap<String, BlockingQueue<?>> queueMap = new ConcurrentHashMap<String, BlockingQueue<?>>();
	
	private ConcurrentHashMap<String, Map<String, ?>> cacheMap = new ConcurrentHashMap<String, Map<String, ?>>();
}
