package org.zyf.cache;

import java.util.List;

public interface IndexLoader<V> {
	public List<V> load(Object... conditions);
}
