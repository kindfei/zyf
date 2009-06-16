package zyf.cache;

import java.util.List;

public interface Store<K, V> {
	Element<K, V> put(Element<K, V> element);
	Element<K, V> remove(K key);
	void removeAll();
	Element<K, V> get(K key);
	List<K> getKeys();
	List<Element<K, V>> getValues();
	int getSize();
	boolean containsKey(K key);
}
