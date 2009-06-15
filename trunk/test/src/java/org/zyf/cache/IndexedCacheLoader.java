package org.zyf.cache;

import java.util.List;

public interface IndexedCacheLoader<V> {
	public List<V> load(Object... conditions);
}
