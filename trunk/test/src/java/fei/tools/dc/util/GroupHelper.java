package fei.tools.dc.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GroupHelper<E, R extends GroupHelper.GroupingResult<E>> {
	private List<String> keys;
	
	public GroupHelper() {
		super();
	}

	public GroupHelper(List<String> keys) {
		super();
		this.keys = keys;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<R> group(List<E> elements) {
		List<R> grs = new ArrayList<R>();
		
		while (elements.size() > 0) {
			R gr = newGroupingResult();
			List<E> groupedElements = new ArrayList<E>();
			
			for (Iterator<E> iterator = elements.iterator(); iterator.hasNext();) {
				E element = iterator.next();
				
				if (gr.getKeyValues() == null) {
					Map<String, Object> keyValues = new LinkedHashMap<String, Object>();
					if(keys != null) {
						for (String key : keys) {
							keyValues.put(key, getValue(element, key));
						}
					}
					gr.setKeyValues(keyValues);
					groupedElements.add(element);
					iterator.remove();
					continue;
				}
				
				boolean equals = true;
				for (Entry<String, Object> entry : gr.getKeyValues().entrySet()) {
					Object value = getValue(element, entry.getKey());
					if (value != entry.getValue()) {
						if (value == null || !value.equals(entry.getValue())) {
							equals = false;
							break;
						}
					}
				}
				
				if (equals) {
					groupedElements.add(element);
					iterator.remove();
				}
			}
			
			gr.setElements(groupedElements);
			grs.add(gr);
		}
		
		return grs;
	}
	
	protected abstract Object getValue(E element, String key);
	protected abstract R newGroupingResult();
	
	public static class GroupingResult<E> {
		private Map<String, Object> keyValues;
		private List<E> elements;
		
		public Map<String, Object> getKeyValues() {
			return keyValues;
		}

		public void setKeyValues(Map<String, Object> keyValues) {
			this.keyValues = keyValues;
		}

		public List<E> getElements() {
			return elements;
		}

		public void setElements(List<E> elements) {
			this.elements = elements;
		}
	}
}
