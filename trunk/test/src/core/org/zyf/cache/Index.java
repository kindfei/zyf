package org.zyf.cache;

import java.util.List;

public interface Index<K, V> {
	public String getName();
	void setCache(Cache<K, V> cache);
	public List<V> query(Object... conditions);
}
