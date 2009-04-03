package org.zyf.cache;

public interface Element<K, V> {
	public K getKey();
	public V getValue();
	public long getTimeToLive();
	public long getTimeToIdle();
	public long getCreationTime();
	public long getLastAccessTime();
	public long getHitCount();
	
	public void resetAccessStatistics();
	public boolean updateAccessStatistics();
	public boolean isExpired();
}
