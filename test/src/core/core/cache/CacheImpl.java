package core.cache;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheImpl<K, V> implements Cache<K, V> {

	private String name;
	private int maxElements;
	private long timeToLive;
	private long timeToIdle;
	private StoreEvictionPolicy policy;

	protected Store<K, V> store;
	
	protected ElementComparator<V> comparator;
	
	private CacheLoader<K, V> loader;
	
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public CacheImpl(String name, int maxElements, long timeToLive,
			long timeToIdle, StoreEvictionPolicy policy) {
		this.name = name;
		this.maxElements = maxElements;
		this.timeToLive = timeToLive;
		this.timeToIdle = timeToIdle;
		this.policy = policy;
		
		store = MemoryStore.create(this);
	}
	
	@Override
	public void setComparator(ElementComparator<V> comparator) {
		this.comparator = comparator;
	}
	
	@Override
	public void setCacheLoader(CacheLoader<K, V> loader) {
		this.loader = loader;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMaxElements() {
		return maxElements;
	}

	@Override
	public long getTimeToLive() {
		return timeToLive;
	}

	@Override
	public long getTimeToIdle() {
		return timeToIdle;
	}

	@Override
	public StoreEvictionPolicy getPolicy() {
		return policy;
	}
	
	@Override
	public ElementComparator<V> getComparator() {
		return comparator;
	}
	
	@Override
	public CacheLoader<K, V> getCacheLoader() {
		return loader;
	}
	
	@Override
	public void put(K key, V value) {
		Element<K, V> element = new Element<K, V>(key, value);

		element.setCreateTime(System.currentTimeMillis());
		element.setTimeToLive(timeToLive);
		element.setTimeToIdle(timeToIdle);
		
		put(element);
	}
	
	protected void put(Element<K, V> element) {
		lock.writeLock().lock();
		try {
			if (comparator != null) {
				Element<K, V> preElement = store.get(element.getKey());
				if (preElement == null || comparator.isNew(preElement.getValue(), element.getValue())) {
					putElement(element);
				}
			} else {
				putElement(element);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	protected void putElement(Element<K, V> element) {
		store.put(element);
	}

	@Override
	public boolean remove(K key) {
		lock.writeLock().lock();
		try {
			return removeElement(key);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	protected boolean removeElement(K key) {
		return store.remove(key) != null;
	}

	@Override
	public void removeAll() {
		lock.writeLock().lock();
		try {
			removeAllElement();
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	protected void removeAllElement() {
		store.removeAll();
	}

	@Override
	public V get(K key) {
		Element<K, V> element = null;
		
		lock.readLock().lock();
		try {
			element = store.get(key);
			
			if (element == null && loader != null) {
				element = new FutureElement<K, V>(key, this);
				put(element);
			}
		} finally {
			lock.readLock().unlock();
		}
		
		if (element == null && loader != null) {
			FutureElement<K, V> futureElement = null;
			
			lock.writeLock().lock();
			try {
				element = store.get(key);
				
				if (element == null) {
					element = futureElement = new FutureElement<K, V>(key, this);
					put(element);
				}
			} finally {
				lock.writeLock().unlock();
			}
			
			futureElement.run();
		}

		return element.getValue();
	}

	@Override
	public List<K> getKeys() {
		lock.readLock().lock();
		try {
			return store.getKeys();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public int getSize() {
		lock.readLock().lock();
		try {
			return store.getSize();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isKeyInCache(K key) {
		lock.readLock().lock();
		try {
			return store.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isValueInCache(V value) {
		List<K> keys = getKeys();
		for (K key : keys) {
			V elementValue = get(key);
			if (elementValue != null) {
				if (elementValue == value) {
					return true;
				} else if (elementValue.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
}
