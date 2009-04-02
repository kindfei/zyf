package org.zyf.cache.index;

import java.util.List;
import java.util.Set;

import org.zyf.cache.Cache;

public interface IndexableCache<K, V> extends Cache<K, V> {
	public void addIndex(Index<K, V> index);
	public Index<K, V> getIndex(String name);
	public List<V> fetch(Set<K> set);
}
