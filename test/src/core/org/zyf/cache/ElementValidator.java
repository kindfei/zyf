package org.zyf.cache;

public interface ElementValidator<V> {
	public boolean isNew(V preElement, V newElement);
}
