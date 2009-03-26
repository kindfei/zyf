package core.cache;

public interface CacheLoader<K, V> {
	public Element<K, V> load(K key);
}
