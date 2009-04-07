package org.zyf.cache.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.zyf.cache.Cache;

public class HashIndexImpl<K, V> implements HashIndex<K, V> {
	private ConditionMap rootMap = new ConditionMap();
	
	private String name;
	private IndexKeyBuilder<K, V> builder;
	private IndexInitializer<V> initializer;
	
	private Cache<K, V> cache;
	
	public HashIndexImpl(String name, IndexKeyBuilder<K, V> builder, IndexInitializer<V> initializer) {
		this.name = name;
		this.builder = builder;
		this.initializer = initializer;
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

	public IndexInitializer<V> getInitializer() {
		return initializer;
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
					obj = map.putIfAbsent(conditions[i], new IndexKeySet(conditions));
				} else {
					obj = map.putIfAbsent(conditions[i], new ConditionMap());
				}
			}
		}
		return (IndexKeySet) obj;
	}
	
	/**
	 * 
	 * @author zhangyf
	 *
	 */
	private class ConditionMap {
		private ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();
		
		private Object nullKey = new Object();
		
		public Object putIfAbsent(Object key, Object value) {
			if (key == null) key = nullKey;
			Object obj = map.putIfAbsent(key, value);
			if (obj == null) {
				return value;
			} else {
				return obj;
			}
		}
		
		public Object get(Object key) {
			if (key == null) key = nullKey;
			return map.get(key);
		}

		public void clear() {
			map.clear();
		}
	}

	/**
	 * 
	 * @author zhangyf
	 *
	 */
	private class IndexKeySet {
		
		private Object[] conditions;
		
		private Set<K> set = new HashSet<K>();
		
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		
		private AtomicBoolean initialized = new AtomicBoolean(false);
		
		private AtomicReference<FutureTask<List<K>>> task = new AtomicReference<FutureTask<List<K>>>();
		
		public IndexKeySet(Object[] conditions) {
			this.conditions = conditions;
		}
		
		public void initialize() {
			if (task.compareAndSet(null, new FutureTask<List<K>>(new InitializeCall()))) {
				task.get().run();
			}
			try {
				task.get().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
			if (!initialized.get()) {
				initialize();
			}
			
			lock.readLock().lock();
			try {
				return new ArrayList<K>(set);
			} finally {
				lock.readLock().lock();
			}
		}
		
		class InitializeCall implements Callable<List<K>> {

			@Override
			public List<K> call() throws Exception {
				List<V> values = initializer.initialize(conditions);
				for (V value : values) {
					cache.put(builder.getKey(value), value);
				}
				initialized.set(true);
				return null;
			}
			
		}
	}
}
