package zyf.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryStore<K, V> implements Store<K, V> {

	protected Map<K, Element<K, V>> map;
	protected Cache<K, V> cache;
	
	MemoryStore(Cache<K, V> cache) {
		this.cache = cache;
	}
	
	static <K, V> Store<K, V> create(Cache<K, V> cache) {
		Store<K, V> store = null;
		switch (cache.getPolicy()) {
		case FIFO:
			store = new FifoMemoryStore<K, V>(cache);
			break;

		case LFU:
			store = new LfuMemoryStore<K, V>(cache);
			break;

		case LRU:
			store = new LruMemoryStore<K, V>(cache);
			break;

		}
		return store;
	}

	@Override
	public Element<K, V> put(Element<K, V> element) {
		Element<K, V> oldElement = null;
		if (element != null) {
			oldElement = map.put(element.getKey(), element);
			doPut();
		}
		return oldElement;
	}

	protected void doPut() {

	}

	@Override
	public Element<K, V> remove(K key) {
		return map.remove(key);
	}

	@Override
	public void removeAll() {
		map.clear();
	}

	@Override
	public Element<K, V> get(K key) {
		Element<K, V> element = map.get(key);
		if (element != null && !element.updateAccessStatistics()) {
			cache.remove(key);
			element = null;
		}
		return element;
	}

	@Override
	public List<K> getKeys() {
		return new ArrayList<K>(map.keySet());
	}
	
	@Override
	public List<Element<K, V>> getValues() {
		return new ArrayList<Element<K, V>>(map.values());
	}

	@Override
	public int getSize() {
		return map.size();
	}

	@Override
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	protected final boolean isFull() {
		return map.size() > cache.getMaxElements();
	}
}
