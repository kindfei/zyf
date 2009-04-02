package org.zyf.cache.index;

import java.util.List;

public interface IndexableCacheLoader<V> {
	public List<V> load(Object... conditions) throws Exception;
}
