package org.zyf.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LfuMemoryStore<K, V> extends MemoryStore<K, V> {

	private static final int DEFAULT_SAMPLE_SIZE = 30;

	private static final Random RANDOM = new Random();

	LfuMemoryStore(Cache<K, V> cache) {
		super(cache);
		map = new HashMap<K, Element<K, V>>();
	}

	protected void doPut(Element<K, V> elementJustAdded) {
		if (isFull()) {
			removeLfuElement(elementJustAdded);
		}
	}

	private void removeLfuElement(Element<K, V> elementJustAdded) {
		Element<K, V> element = findRelativelyUnused(elementJustAdded);
		cache.remove(element.getKey());
	}

	private Element<K, V> findRelativelyUnused(Element<K, V> elementJustAdded) {
		List<Element<K, V>> elements = sampleElements(map.size());
		Element<K, V> element = leastHit(elements, elementJustAdded);
		return map.get(element.getKey());
	}

	private List<Element<K, V>> sampleElements(int size) {
		int[] offsets = generateRandomSample(size);
		List<Element<K, V>> elements = new ArrayList<Element<K, V>>(offsets.length);
		Iterator<Element<K, V>> iterator = map.values().iterator();
		for (int i = 0; i < offsets.length; i++) {
			for (int j = 0; j < offsets[i]; j++) {
				iterator.next();
			}
			elements.add(iterator.next());
		}
		return elements;
	}

	private int[] generateRandomSample(int populationSize) {
		int sampleSize = calculateSampleSize(populationSize);
		int[] offsets = new int[sampleSize];
		int maxOffset = populationSize / sampleSize;
		for (int i = 0; i < sampleSize; i++) {
			offsets[i] = RANDOM.nextInt(maxOffset);
		}
		return offsets;
	}

	private int calculateSampleSize(int populationSize) {
		if (populationSize < DEFAULT_SAMPLE_SIZE) {
			return populationSize;
		} else {
			return DEFAULT_SAMPLE_SIZE;
		}
	}

	private Element<K, V> leastHit(List<Element<K, V>> sampledElements,
			Element<K, V> justAdded) {
		// edge condition when Memory Store configured to size 0
		if (sampledElements.size() == 1 && justAdded != null) {
			return justAdded;
		}
		Element<K, V> lowestElement = null;
		for (Element<K, V> element : sampledElements) {
			if (lowestElement == null) {
				if (!element.equals(justAdded)) {
					lowestElement = element;
				}
			} else {
				if (element.getHitCount() < lowestElement.getHitCount()
						&& !element.equals(justAdded)) {
					lowestElement = element;
				}
			}
		}
		return lowestElement;
	}
}
