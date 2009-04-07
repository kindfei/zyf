package org.zyf.cache.index;

import java.util.List;

import org.zyf.cache.Cache;

public interface Index<K, V> {
	public void setCache(Cache<K, V> cache);
	public void add(V value);
	public boolean evict(V value);
	public void evictAll();
	
	public String getName();
	public IndexKeyBuilder<K, V> getBuilder();
	public List<V> query(Object... conditions);
}
