package org.zyf.cache.index;

public interface HashIndex<K, V> extends Index<K, V> {
	public IndexInitializer<V> getInitializer();
}
