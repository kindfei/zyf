package zyf.cache;

public interface IndexKeyBuilder<K, V> {
	public K getKey(V value);
	public Object[] getConditions(V value);
}
