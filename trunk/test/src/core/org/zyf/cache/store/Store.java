package org.zyf.cache.store;

import java.util.List;

import org.zyf.cache.Element;

public interface Store<K, V> {
	public Element<K, V> put(Element<K, V> element);
	public boolean putIfAbsent(Element<K, V> element);
	public Element<K, V> remove(K key);
	public void removeAll();
	public Element<K, V> get(K key);
	public List<K> getKeys();
	public List<Element<K, V>> getValues();
	public int getSize();
	public boolean containsKey(K key);
}
