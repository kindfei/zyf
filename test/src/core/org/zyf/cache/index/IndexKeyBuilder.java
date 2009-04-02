package org.zyf.cache.index;

public interface IndexKeyBuilder<K, V> {
	public K getKey(V value);
	public Object[] getConditions(V value);
}
