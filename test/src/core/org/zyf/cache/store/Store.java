package org.zyf.cache.store;

import java.util.List;

import org.zyf.cache.DefaultElement;

public interface Store<K, V> {
	public DefaultElement<K, V> put(DefaultElement<K, V> element);
	public boolean putIfAbsent(DefaultElement<K, V> element);
	public DefaultElement<K, V> remove(K key);
	public void removeAll();
	public DefaultElement<K, V> get(K key);
	public List<K> getKeys();
	public List<DefaultElement<K, V>> getValues();
	public int getSize();
	public boolean containsKey(K key);
}
