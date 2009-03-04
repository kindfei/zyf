package core.cluster;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClusterCache
 * @author zhangyf
 *
 */
public class ClusterCache {
	private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
	void set(String key, Object value) {
		cache.put(key, value);
	}
	
	Object get(String key) {
		return cache.get(key);
	}
}
