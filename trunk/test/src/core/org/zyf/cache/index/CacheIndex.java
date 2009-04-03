package org.zyf.cache.index;

import org.zyf.cache.Cache;

public interface CacheIndex<K, V> extends Index<K, V> {
	public void setCache(Cache<K, V> cache);
	public void add(V value);
	public boolean evict(V value);
	public void evictAll();
}
