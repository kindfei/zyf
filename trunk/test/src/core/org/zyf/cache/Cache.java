package org.zyf.cache;

import java.util.List;

public interface Cache<K, V> {
	public void setComparator(ElementValidator<V> comparator);
	public void setCacheLoader(CacheLoader<K, V> loader);
	public String getName();
	public int getMaxElements();
	public long getTimeToLive();
	public long getTimeToIdle();
	public StoreEvictionPolicy getPolicy();
	public ElementValidator<V> getComparator();
	public CacheLoader<K, V> getCacheLoader();

	public void put(K key, V value);
	public boolean remove(K key);
	public void removeAll();
	public V get(K key);
	public List<K> getKeys();
	public List<V> getValues();
	public int getSize();
	public boolean isKeyInCache(K key);
	public boolean isValueInCache(V value);
}
