package org.zyf.cache.index;

import java.util.Comparator;
import java.util.List;

public interface TreeIndex<K, V> extends Index<K, V> {
	public Comparator<K> getComparator();
	public List<V> rangeQuery(RangedCondition... conditions);
}
