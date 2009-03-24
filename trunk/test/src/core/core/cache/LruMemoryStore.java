package core.cache;

import java.util.Map;

public class LruMemoryStore<K, V> extends MemoryStore<K, V> {

	LruMemoryStore(Cache<K, V> cache) {
		super(cache);
		map = new SpoolingLinkedHashMap();
	}

	/**
	 * An extension of LinkedHashMap which overrides {@link #removeEldestEntry}
	 * to persist cache entries to the auxiliary cache before they are removed.
	 * <p/>
	 * This implementation also provides LRU by access order.
	 */
	public final class SpoolingLinkedHashMap extends java.util.LinkedHashMap<K, Element<K,V>> {

		private static final long serialVersionUID = 8103440809510633465L;
		
		private static final int INITIAL_CAPACITY = 100;
		private static final float GROWTH_FACTOR = .75F;

		/**
		 * Default constructor. Will create an initial capacity of 100, a
		 * loading of .75 and LRU by access order.
		 */
		public SpoolingLinkedHashMap() {
			super(INITIAL_CAPACITY, GROWTH_FACTOR, true);
		}

		/**
		 * Returns <tt>true</tt> if this map should remove its eldest entry.
		 * This method is invoked by <tt>put</tt> and <tt>putAll</tt> after
		 * inserting a new entry into the map. It provides the implementer with
		 * the opportunity to remove the eldest entry each time a new one is
		 * added. This is useful if the map represents a cache: it allows the
		 * map to reduce memory consumption by deleting stale entries.
		 * <p/>
		 * Will return true if:
		 * <ol>
		 * <li>the element has expired
		 * <li>the cache size is greater than the in-memory actual. In this case
		 * we spool to disk before returning.
		 * </ol>
		 * 
		 * @param eldest
		 *            The least recently inserted entry in the map, or if this
		 *            is an access-ordered map, the least recently accessed
		 *            entry. This is the entry that will be removed it this
		 *            method returns <tt>true</tt>. If the map was empty prior
		 *            to the <tt>put</tt> or <tt>putAll</tt> invocation
		 *            resulting in this invocation, this will be the entry that
		 *            was just inserted; in other words, if the map contains a
		 *            single entry, the eldest entry is also the newest.
		 * @return true if the eldest entry should be removed from the map;
		 *         <tt>false</t> if it should be retained.
		 */
		protected final boolean removeEldestEntry(Map.Entry<K, Element<K,V>> eldest) {
			Element<K,V> element = eldest.getValue();
			return removeLeastRecentlyUsedElement(element);
		}

		/**
		 * Relies on being called from a synchronized method
		 * 
		 * @param element
		 * @return true if the LRU element should be removed
		 */
		private boolean removeLeastRecentlyUsedElement(Element<K,V> element) {
			// check for expiry and remove before going to the trouble of
			// spooling it
			if (element.isExpired()) {
				return true;
			}

			if (isFull()) {
				return true;
			} else {
				return false;
			}

		}
	}
}
