package org.zyf.cache.store;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.zyf.cache.Cache;
import org.zyf.cache.Element;

public class FifoMemoryStore<K, V> extends AbstractStore<K, V> {

	FifoMemoryStore(Cache<K, V> cache) {
		super(cache);
		map = new LinkedHashMap<K, Element<K, V>>();
	}
	
    protected final void doPut(Element<K, V> element) {
        if (isFull()) {
            removeFirstElement();
        }
    }
    
    private void removeFirstElement() {
        Element<K, V> element = getFirstElement();
        cache.remove(element.getKey());
    }
    
    private Element<K, V> getFirstElement() {
        if (map.size() == 0) {
            return null;
        }

        Element<K, V> element = null;

        Set<K> keySet = map.keySet();
        Iterator<K> itr = keySet.iterator();
        if (itr.hasNext()) {
            element = map.get(itr.next());
        }

        return element;
    }
}
