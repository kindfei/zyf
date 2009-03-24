package core.cache;

public class Element<K, V> {

	private static final long serialVersionUID = 5538336091459133307L;
	
	private final K key;
	private final V value;

	private long timeToLive;
	private long timeToIdle;
	private long creationTime;
	private boolean created;
	
	private long lastAccessTime;
	private long hitCount;

	public Element(K key, V value) {
		this.key = key;
		this.value = value;
	}

	final void setTimeToLive(long timeToLiveSeconds) {
		this.timeToLive = timeToLiveSeconds;
	}

	final void setTimeToIdle(long timeToIdleSeconds) {
		this.timeToIdle = timeToIdleSeconds;
	}

	public final void setCreateTime(long createTime) {
		this.creationTime = createTime;
		created = true;
	}
	
	final boolean isCreated() {
		return created;
	}

	public final K getKey() {
		return key;
	}

	public final V getValue() {
		return value;
	}

	public final long getTimeToLive() {
		return timeToLive;
	}

	public final long getTimeToIdle() {
		return timeToIdle;
	}

	public final long getCreationTime() {
		return creationTime;
	}

	public final long getLastAccessTime() {
		return lastAccessTime;
	}

	public final long getHitCount() {
		return hitCount;
	}

	final void resetAccessStatistics() {
		lastAccessTime = 0;
		hitCount = 0;
	}

	final boolean updateAccessStatistics() {
		if (isExpired()) {
			return false;
		}
		lastAccessTime = System.currentTimeMillis();
		hitCount++;
		return true;
	}

	final boolean isExpired() {
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

	public final boolean equals(Object object) {
		if (object == null || !(object instanceof Element)) {
			return false;
		}

		Element<?, ?> element = (Element<?, ?>) object;
		if (key == null || element.getKey() == null) {
			return false;
		}

		return key.equals(element.getKey());
	}

	public final int hashCode() {
		return key.hashCode();
	}
}
