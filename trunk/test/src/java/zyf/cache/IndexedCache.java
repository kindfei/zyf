package zyf.cache;

import java.util.List;
import java.util.Set;

public interface IndexedCache<K, V> extends Cache<K, V> {
	public void addIndex(Index<K, V> index);
	public Index<K, V> getIndex(String name);
	public List<V> fetch(Set<K> set);
}
