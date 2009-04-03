package org.zyf.cache.index;

import java.util.List;

public interface IndexInitializer<V> {
	public List<V> initialize(Object... conditions);
}
