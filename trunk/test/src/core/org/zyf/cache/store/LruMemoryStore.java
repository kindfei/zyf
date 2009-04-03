package org.zyf.cache.store;

import java.util.Map;

import org.zyf.cache.Cache;
import org.zyf.cache.DefaultElement;

public class LruMemoryStore<K, V> extends AbstractStore<K, V> {

	LruMemoryStore(Cache<K, V> cache) {
		super(cache);
		map = new SpoolingLinkedHashMap();
	}

	public final class SpoolingLinkedHashMap extends java.util.LinkedHashMap<K, DefaultElement<K,V>> {

		private static final long serialVersionUID = 8103440809510633465L;
		
		private static final int INITIAL_CAPACITY = 100;
		private static final float GROWTH_FACTOR = .75F;

		public SpoolingLinkedHashMap() {
			super(INITIAL_CAPACITY, GROWTH_FACTOR, true);
		}

		protected final boolean removeEldestEntry(Map.Entry<K, DefaultElement<K,V>> eldest) {
			DefaultElement<K,V> element = eldest.getValue();
			removeLeastRecentlyUsedElement(element);
			return false;
		}

		private void removeLeastRecentlyUsedElement(DefaultElement<K,V> element) {
			if (element.isExpired() || isFull()) {
				cache.remove(element.getKey());
			}
		}
	}
}
