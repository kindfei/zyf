package org.zyf.cache;

import java.util.List;

public interface RangedIndexLoader<V> {
	public List<V> load(RangedCondition... conditions);
}
