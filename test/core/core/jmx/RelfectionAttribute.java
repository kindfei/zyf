package core.jmx;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.MBeanAttributeInfo;

public class RelfectionAttribute {
	private Object instance;
	private Field field;
	private boolean isToString;
	
	private MBeanAttributeInfo info;
	
	RelfectionAttribute(Object instance, Field field, ATTRIBUTE attr) {
		this.instance = instance;
		this.field = field;
		this.isToString = attr.isToString();
		
		String name = attr.name();
		String type = attr.type();
		boolean isReadable = attr.isReadable();
		boolean isWritable = attr.isWritable();
		String description = attr.description();
		
		if (name.equals("")) {
			name = field.getName();
		}
		
		if (type.equals("")) {
			type = field.getType().getName();
		}
		
		info = new MBeanAttributeInfo(
				name,
				type,
				description,
				isReadable,
				isWritable,
				false);
		
		field.setAccessible(true);
	}
	
	MBeanAttributeInfo getInfo() {
		return info;
	}

	Object get() throws IllegalArgumentException, IllegalAccessException {
		Object value = field.get(instance);
		
		if (Map.class.isAssignableFrom(value.getClass())) {
			Map<?, ?> map = (Map<?, ?>) value;
			return mapToString(map);
		} else if (List.class.isAssignableFrom(value.getClass())) {
			List<?> list = (List<?>) value;
			return arrayToString(list.toArray(new Object[0]));
		} else if (Object[].class.isAssignableFrom(value.getClass())) {
			Object[] array = (Object[]) value;
			return arrayToString(array);
		} else if (isToString) {
			return value.toString();
		}
		
		return value;
	}

	void set(Object value) throws IllegalArgumentException, IllegalAccessException {
		field.set(instance, value);
	}
	
	private String mapToString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		
		for (Entry<?, ?> entry : map.entrySet()) {
			Object key = (Object) entry.getKey();
			Object value = (Object) entry.getValue();
			sb.append(key.toString()).append("-").append(value.toString()).append(", ");
		}
		
		return sb.toString();
	}
	
	private String arrayToString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		
		for (Object obj : array) {
			sb.append(obj.toString()).append(", ");
		}
		
		return sb.toString();
	}
}
