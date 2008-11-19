package test.core.cluster;

import java.util.HashMap;
import java.util.Map;

/**
 * Cluster handler factory
 * @author zhangyf
 *
 */
public class ClusterHandlerFactory {
	private static final Map<String, ClusterLock> lockMap = new HashMap<String, ClusterLock>();
	private static final Map<String, ClusterTaskQueue> queueMap = new HashMap<String, ClusterTaskQueue>();
	private static final Map<String, ClusterExecutor> executorMap = new HashMap<String, ClusterExecutor>();
	private static final Map<String, ClusterCache> cacheMap = new HashMap<String, ClusterCache>();
	
	static ClusterLock getClusterLock(String procName) {
		ClusterLock lock = lockMap.get(procName);
		if (lock == null) lock = createHandler(procName, lockMap, new ClusterLock());
		return lock;
	}
	
	static ClusterTaskQueue getClusterTaskQueue(String procName, boolean fairTake) {
		ClusterTaskQueue queue = queueMap.get(procName);
		if (queue == null) queue = createHandler(procName, queueMap, new ClusterTaskQueue(fairTake));
		return queue;
	}
	
	static ClusterExecutor getClusterExecutor(String procName) {
		ClusterExecutor executor = executorMap.get(procName);
		if (executor == null) executor = createHandler(procName, executorMap, new ClusterExecutor(procName));
		return executor;
	}
	
	static ClusterCache getClusterCache(String procName) {
		ClusterCache cache = cacheMap.get(procName);
		if (cache == null) cache = createHandler(procName, cacheMap, new ClusterCache());
		return cache;
	}
	
	static <H> H createHandler(String procName, Map<String, H> map, H newHandler) {
		H h = null;
		synchronized (map) {
			h = map.get(procName);
			if (h == null) {
				h = newHandler;
				map.put(procName, h);
			}
		}
		return h;
	}
}
