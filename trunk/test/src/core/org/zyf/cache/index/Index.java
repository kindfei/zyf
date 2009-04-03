package org.zyf.cache.index;

import java.util.List;

public interface Index<K, V> {
	public String getName();
	public IndexKeyBuilder<K, V> getBuilder();
	public List<V> query(Object... conditions);
}
