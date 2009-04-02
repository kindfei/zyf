package org.zyf.cache;

public interface CacheLoader<K, V> {
	public V load(K key) throws Exception;
}
