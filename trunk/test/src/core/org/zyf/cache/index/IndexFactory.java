package org.zyf.cache.index;

import java.util.Comparator;

public class IndexFactory {
	public static <K, V> HashIndex<K, V> createHashIndex(String name,
			IndexKeyBuilder<K, V> builder, IndexInitializer<V> initializer) {
		return new HashIndexImpl<K, V>(name, builder, initializer);
	}
	
	public static <K, V> TreeIndex<K, V> createTreeIndex(String name,
			IndexKeyBuilder<K, V> builder, Comparator<Object>[] comparators) {
		return new TreeIndexImpl<K, V>(name, builder, comparators);
	}
}
