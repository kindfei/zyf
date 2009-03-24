package core.cache;

public interface CacheLoader<K, V> {
	public V load(K key);
}
