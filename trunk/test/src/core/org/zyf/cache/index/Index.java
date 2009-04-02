package org.zyf.cache.index;

import java.util.List;

public interface Index<K, V> {
	public String getName();
	public IndexableCacheLoader<V> getLoader();
	public List<V> query(Object... conditions);
}
