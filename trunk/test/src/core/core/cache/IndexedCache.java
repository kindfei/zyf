package core.cache;

public interface IndexedCache<K, V> extends Cache<K, V> {
	public void addIndex(Index<K, V> index);
	public Index<K, V> getIndex(String name);
}
