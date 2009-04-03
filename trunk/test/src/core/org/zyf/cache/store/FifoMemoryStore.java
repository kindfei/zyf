package org.zyf.cache.store;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.zyf.cache.Cache;
import org.zyf.cache.DefaultElement;

public class FifoMemoryStore<K, V> extends AbstractStore<K, V> {

	FifoMemoryStore(Cache<K, V> cache) {
		super(cache);
		map = new LinkedHashMap<K, DefaultElement<K, V>>();
	}
	
    protected final void doPut(DefaultElement<K, V> element) {
        if (isFull()) {
            removeFirstElement();
        }
    }
    
    private void removeFirstElement() {
        DefaultElement<K, V> element = getFirstElement();
        cache.remove(element.getKey());
    }
    
    private DefaultElement<K, V> getFirstElement() {
        if (map.size() == 0) {
            return null;
        }

        DefaultElement<K, V> element = null;

        Set<K> keySet = map.keySet();
        Iterator<K> itr = keySet.iterator();
        if (itr.hasNext()) {
            element = map.get(itr.next());
        }

        return element;
    }
}
