package org.zyf.cache.event;

public interface CacheEventListener<K, V> {
	
	void onEvent(CacheEvent<K, V> event);
}
