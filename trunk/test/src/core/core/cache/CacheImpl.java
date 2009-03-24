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
	public Element<K, V> get(K key) {
		lock.readLock().lock();
		try {
			Element<K, V> element = store.get(key);
			
			if (element == null) {
				if (loader != null) {
					V value = loader.load(key);
					if (value == null) {
						return null;
					}
					element = new Element<K, V>(key, value);
					put(element);
				}
			}

			return element;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void put(Element<K, V> element) {
		element.resetAccessStatistics();

		if (!element.isCreated()) element.setCreateTime(System.currentTimeMillis());
		element.setTimeToLive(timeToLive);
		element.setTimeToIdle(timeToIdle);
		
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
			Element<K, V> element = get(key);
			if (element != null) {
				Object elementValue = element.getValue();
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
