package org.zyf.cache.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zyf.cache.Cache;
import org.zyf.cache.DefaultElement;

public class AbstractStore<K, V> implements Store<K, V> {

	protected Map<K, DefaultElement<K, V>> map;
	protected Cache<K, V> cache;
	
	AbstractStore(Cache<K, V> cache) {
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
	public DefaultElement<K, V> put(DefaultElement<K, V> element) {
		DefaultElement<K, V> preElement = null;
		if (element != null) {
			preElement = map.put(element.getKey(), element);
			doPut();
		}
		return preElement;
	}

	@Override
	public boolean putIfAbsent(DefaultElement<K, V> element) {
		// TODO Auto-generated method stub
		return false;
	}

	protected void doPut() {

	}

	@Override
	public DefaultElement<K, V> remove(K key) {
		return map.remove(key);
	}

	@Override
	public void removeAll() {
		map.clear();
	}

	@Override
	public DefaultElement<K, V> get(K key) {
		DefaultElement<K, V> element = map.get(key);
		if (element != null && !element.updateAccessStatistics()) {
//TODO			cache.remove(key);
			element = null;
		}
		return element;
	}

	@Override
	public List<K> getKeys() {
		return new ArrayList<K>(map.keySet());
	}
	
	@Override
	public List<DefaultElement<K, V>> getValues() {
		return new ArrayList<DefaultElement<K, V>>(map.values());
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
