package incubation.cache;

public interface ElementComparator<V> {
	public boolean isNew(V preElement, V newElement);
}
