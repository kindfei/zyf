package org.zyf.cache;

public class DefaultElement<K, V> {
	
	private K key;
	private V value;

	private long timeToLive;
	private long timeToIdle;
	private long creationTime;
	
	private long lastAccessTime;
	private long hitCount;

	DefaultElement(K key, V value) {
		this.key = key;
		this.value = value;
	}

	void setTimeToLive(long timeToLiveSeconds) {
		this.timeToLive = timeToLiveSeconds;
	}

	void setTimeToIdle(long timeToIdleSeconds) {
		this.timeToIdle = timeToIdleSeconds;
	}

	public void setCreateTime(long createTime) {
		this.creationTime = createTime;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public long getTimeToIdle() {
		return timeToIdle;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public long getHitCount() {
		return hitCount;
	}

	void resetAccessStatistics() {
		lastAccessTime = 0;
		hitCount = 0;
	}

	public boolean updateAccessStatistics() {
		if (isExpired()) {
			return false;
		}
		lastAccessTime = System.currentTimeMillis();
		hitCount++;
		return true;
	}

	public boolean isExpired() {
		if (timeToLive == 0 && timeToIdle == 0) {
			return false;
		}

		long expirationTime = 0;
		long ttlExpiry = creationTime + timeToLive * 1000L;

		long mostRecentTime = Math.max(creationTime, lastAccessTime);
		long ttiExpiry = mostRecentTime + timeToIdle * 1000L;

		if (timeToIdle == 0) {
			expirationTime = ttlExpiry;
		} else if (timeToLive == 0) {
			expirationTime = ttiExpiry;
		} else {
			expirationTime = Math.min(ttlExpiry, ttiExpiry);
		}
		
		long now = System.currentTimeMillis();

		return now > expirationTime;
	}

	public boolean equals(Object object) {
		if (object == null || !(object instanceof DefaultElement)) {
			return false;
		}

		DefaultElement<?, ?> element = (DefaultElement<?, ?>) object;
		if (key == null || element.getKey() == null) {
			return false;
		}

		return key.equals(element.getKey());
	}

	public int hashCode() {
		return key.hashCode();
	}
}
