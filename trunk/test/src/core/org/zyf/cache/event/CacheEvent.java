package org.zyf.cache.event;

import org.zyf.cache.Cache;

public class CacheEvent<K, V> {
	//
	private final CacheEventType type;
	
	//
	private K key;
	private V value;
	private V previousValue;
	private Cache<K, V> cache;
	private Exception exception;
	
	
	/**
	 * 
	 */
	public CacheEvent(CacheEventType type) {
		this.type = type;
	}

	/**
	 * 
	 */
	public CacheEventType getType() {
		return type;
	}
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
	
	public V getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(V previousValue) {
		this.previousValue = previousValue;
	}

	public Cache<K, V> getCache() {
		return cache;
	}

	public void setCache(Cache<K, V> cache) {
		this.cache = cache;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
