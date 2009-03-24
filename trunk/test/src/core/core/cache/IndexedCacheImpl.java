package core.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexedCacheImpl<K, V> extends CacheImpl<K, V> implements IndexedCache<K, V> {
	
	private Map<String, Index<K, V>> indexMap = new HashMap<String, Index<K, V>>();

	public IndexedCacheImpl(String name) {
		super(name, Integer.MAX_VALUE, 0, 0, StoreEvictionPolicy.FIFO);
	}

	@Override
	public void addIndex(Index<K, V> index) {
		lock.readLock().lock();
		try {
			List<Element<K, V>> elements = store.getValues();
			for (Element<K, V> element : elements) {
				index.add(element.getValue());
			}
			
			index.setCache(this);
			indexMap.put(index.getName(), index);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Index<K, V> getIndex(String name) {
		return indexMap.get(name);
	}
	
	@Override
	public List<V> fetch(Set<K> set) {
		lock.readLock().lock();
		try {
			List<V> list = new ArrayList<V>(set.size());
			for (K key : set) {
				Element<K, V> e = get(key);
				if (e == null) {
					throw new RuntimeException("Cache is not consistent with index.");
				}
				list.add(e.getValue());
			}
			
			return list;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	@Override
	protected void putElement(Element<K, V> element) {
		Element<K, V> oldElement = store.put(element);
		putToIndex(oldElement, element);
	}
	
	private void putToIndex(Element<K, V> oldElement, Element<K, V> newElement) {
		for (Index<K, V> index : indexMap.values()) {
			if (oldElement != null) {
				index.remove(oldElement.getValue());
			}
			index.add(newElement.getValue());
		}
	}
	
	@Override
	protected boolean removeElement(K key) {
		Element<K, V> element = store.remove(key);
		removeFromIndex(element);
		return element != null;
	}
	
	private void removeFromIndex(Element<K, V> element) {
		if (element == null) {
			return;
		}
		for (Index<K, V> index : indexMap.values()) {
			index.remove(element.getValue());
		}
	}
	
	@Override
	protected void removeAllElement() {
		store.removeAll();
		removeAllFromIndex();
	}
	
	private void removeAllFromIndex() {
		for (Index<K, V> index : indexMap.values()) {
			index.removeAll();
		}
	}
}
