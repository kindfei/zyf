package org.zyf.cache;

import java.util.Map;

public interface CacheInitializer<K, V> {
	public Map<K, V> initialize() throws Exception;
}
