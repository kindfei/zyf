package core.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Index<K, V> {
	private ConditionMap rootMap = new ConditionMap();
	
	private String name;
	private IndexKeyBuilder<K, V> builder;
	private IndexedCacheLoader<V> loader;
	private boolean needInitialize;
	
	private IndexedCache<K, V> cache;
	
	private ReentrantLock lock = new ReentrantLock();
	
	public Index(String name, IndexKeyBuilder<K, V> builder) {
		this(name, builder, false, null);
	}
	
	public Index(String name, IndexKeyBuilder<K, V> builder, boolean needInitialize, IndexedCacheLoader<V> loader) {
		this.name = name;
		this.builder = builder;
		this.needInitialize = needInitialize;
		this.loader = loader;
	}
	
	void setCache(IndexedCache<K, V> cache) {
		this.cache = cache;
	}
	
	public String getName() {
		return name;
	}
	
	void add(V value) {
		IndexElement set = getElement(builder.getConditions(value));
		set.addKey(builder.getKey(value));
	}
	
	boolean remove(V value) {
		IndexElement set = getElement(builder.getConditions(value));
		return set.removeKey(builder.getKey(value));
	}
	
	void removeAll() {
		rootMap.clear();
	}
	
	public List<V> get(Object... conditions) {
		if (cache == null) {
			throw new RuntimeException("Index is not assembled.");
		}
		
		IndexElement element = getElement(conditions);
		
		if (!element.isInitialized()) {
			lock.lock();
			try {
				if (!element.isInitialized()) {
					List<V> values = loader.load(conditions);
					for (V value : values) {
						cache.put(new Element<K, V>(builder.getKey(value), value));
					}
					element.setInitialized(true);
				}
			} finally {
				lock.unlock();
			}
		}
		
		return cache.fetch(element.getKeySet());
	}
	
	private IndexElement getElement(Object... conditions) {
		if (conditions.length == 0) {
			throw new IllegalArgumentException("Have no condition to be specified.");
		}
		
		Object obj = this.rootMap;
		ConditionMap map = null;
		for (int i = 0; i < conditions.length; i++) {
			map = (ConditionMap) obj;
			obj = map.get(conditions[i]);
			
			if (obj == null) {
				if (i == conditions.length - 1) {
					obj = map.putIfAbsent(conditions[i], new IndexElement(!needInitialize));
				} else {
					obj = map.putIfAbsent(conditions[i], new ConditionMap());
				}
			}
		}
		return (IndexElement) obj;
	}

	private class ConditionMap extends ConcurrentHashMap<Object, Object> {
		private static final long serialVersionUID = -5204391047193649905L;
		
		private Object nullKey = new Object();
		
		@Override
		public Object putIfAbsent(Object key, Object value) {
			if (key == null) key = nullKey;
			Object obj = super.putIfAbsent(key, value);
			if (obj == null) {
				return value;
			} else {
				return obj;
			}
		}
		
		@Override
		public Object get(Object key) {
			if (key == null) key = nullKey;
			return super.get(key);
		}
	}

	class IndexElement {
		private Set<K> set = new HashSet<K>();
		private boolean initialized;
		
		IndexElement(boolean initialized) {
			this.initialized = initialized;
		}
		
		public boolean isInitialized() {
			return initialized;
		}

		public void setInitialized(boolean initialized) {
			this.initialized = initialized;
		}

		public boolean addKey(K key) {
			return set.add(key);
		}
		
		public Set<K> getKeySet() {
			return set;
		}
		
		public boolean removeKey(K key) {
			return set.remove(key);
		}
	}
}