package org.zyf.cache.index;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.zyf.cache.Cache;

public class TreeIndexImpl<K, V> implements CacheIndex<K, V>, TreeIndex<K, V> {
	
	private ConditionMap rootMap;
	
	private String name;
	private IndexKeyBuilder<K, V> builder;
	private Comparator<Object>[] comparators;
	
	private Cache<K, V> cache;
	
	TreeIndexImpl(String name, IndexKeyBuilder<K, V> builder, Comparator<Object>[] comparators) {
		this.name = name;
		this.builder = builder;
		this.comparators = comparators;
		
		rootMap = new ConditionMap(comparators[0]);
	}

	public void setCache(Cache<K, V> cache) {
		this.cache = cache;
	}
	
	public void add(V value) {
		IndexKeySet keySet = getKeySet(builder.getConditions(value));
		keySet.addKey(builder.getKey(value));
	}
	
	public boolean evict(V value) {
		IndexKeySet keySet = getKeySet(builder.getConditions(value));
		return keySet.removeKey(builder.getKey(value));
	}
	
	public void evictAll() {
		rootMap.clear();
	}

	public String getName() {
		return name;
	}
	
	public IndexKeyBuilder<K, V> getBuilder() {
		return builder;
	}

	public Comparator<Object>[] getComparators() {
		return comparators;
	}

	public List<V> query(Object... conditions) {
		if (cache == null) {
			throw new RuntimeException("Index is not assembled.");
		}
		
		IndexKeySet keySet = getKeySet(conditions);
		
		return cache.getAll(keySet.getKeys());
	}
	
	private IndexKeySet getKeySet(Object... conditions) {
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
					obj = map.putIfAbsent(conditions[i], new IndexKeySet());
				} else {
					obj = map.putIfAbsent(conditions[i], new ConditionMap(comparators[i]));
				}
			}
		}
		return (IndexKeySet) obj;
	}

	public List<V> rangeQuery(RangedCondition... conditions) {
		// TODO Auto-generated method stub
		return null;
	}

	private class ConditionMap {
		private TreeMap<Object, Object> map;
		
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		
		ConditionMap(Comparator<Object> comparator) {
			map = new TreeMap<Object, Object>(comparator);
		}
		
		public Object putIfAbsent(Object key, Object value) {
			lock.writeLock().lock();
			try {
				Object preValue = map.get(key);
				if (preValue == null) {
					map.put(key, value);
					return value;
				} else {
					return preValue;
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		
		public Object get(Object key) {
			lock.readLock().lock();
			try {
				return map.get(key);
			} finally {
				lock.readLock().unlock();
			}
		}
		
		public List<Object> subMap(Object fromKey, Object toKey) {
			lock.readLock().lock();
			try {
				NavigableMap<Object, Object> subMap = map.subMap(fromKey, true, toKey, false);
				return new ArrayList<Object>(subMap.values());
			} finally {
				lock.readLock().unlock();
			}
		}
		
		public void clear() {
			lock.writeLock().lock();
			try {
				map.clear();
			} finally {
				lock.writeLock().unlock();
			}
		}
	}

	private class IndexKeySet {
		private Set<K> set = new HashSet<K>();
		
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

		public boolean addKey(K key) {
			lock.writeLock().lock();
			try {
				return set.add(key);
			} finally {
				lock.writeLock().lock();
			}
		}
		
		public boolean removeKey(K key) {
			lock.writeLock().lock();
			try {
				return set.remove(key);
			} finally {
				lock.writeLock().lock();
			}
		}
		
		public List<K> getKeys() {
			lock.readLock().lock();
			try {
				return new ArrayList<K>(set);
			} finally {
				lock.readLock().lock();
			}
		}
	}
}
