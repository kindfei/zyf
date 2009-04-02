package org.zyf.cache;

import java.util.List;
import java.util.Map;

import org.zyf.cache.store.StoreEvictionPolicy;

public interface Cache<K, V> {
	public String getName();
	public int getMaxElements();
	public long getTimeToLive();
	public long getTimeToIdle();
	public StoreEvictionPolicy getPolicy();
	public ElementValidator<V> getValidator();
	public CacheLoader<K, V> getLoader();
	
	public CacheStatistics getStatistics();

	public void put(K key, V value);
	public void putAll(Map<K, V> values);
	public boolean remove(K key);
	public void removeAll();
	public V get(K key);
	public List<V> getAll(List<K> keys);
	public List<K> getKeys();
	public List<V> getValues();
	public int getSize();
	public boolean isKeyInCache(K key);
	public boolean isValueInCache(V value);
}
