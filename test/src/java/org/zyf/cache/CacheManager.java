package org.zyf.cache;

public class CacheManager {
	
	public static <K, V> Cache<K, V> createCache(String name, int maxElements, long timeToLive,
			long timeToIdle, StoreEvictionPolicy policy) {
		return new CacheImpl<K, V>(name, maxElements, timeToLive, timeToIdle, policy);
	}
	
	public static <K, V> Cache<K, V> createCache(String name, int maxElements, long timeToLive,
			long timeToIdle) {
		return createCache(name, maxElements, timeToLive, timeToIdle, StoreEvictionPolicy.LRU);
	}
	
	public static <K, V> Cache<K, V> createCache(String name, int maxElements) {
		return createCache(name, maxElements, 0, 0, StoreEvictionPolicy.FIFO);
	}
	
	public static <K, V> IndexedCache<K, V> createIndexedCache(String name) {
		return new IndexedCacheImpl<K, V>(name);
	}
	
	public static <K, V> IndexedCache<K, V> createIndexedCache(String name, int maxElements, long timeToLive,
			long timeToIdle, StoreEvictionPolicy policy) {
		return new IndexedCacheImpl<K, V>(name, maxElements, timeToLive, timeToIdle, policy);
	}
}
