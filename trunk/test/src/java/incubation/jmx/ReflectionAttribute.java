package incubation.jmx;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.MBeanAttributeInfo;

public class ReflectionAttribute {
	private Object instance;
	private Field field;
	
	private String name;
	private String type;
	private boolean isReadable;
	private boolean isWritable;
	private String description;
	private boolean isToString;
	
	ReflectionAttribute(Object instance, Field field, ATTRIBUTE attr) {
		this.instance = instance;
		this.field = field;
		
		this.name = attr.name();
		this.type = attr.type();
		this.isReadable = attr.isReadable();
		this.isWritable = attr.isWritable();
		this.description = attr.description();
		this.isToString = attr.isToString();
		
		if (name.equals("")) {
			name = field.getName();
		}
		
		if (type.equals("")) {
			type = field.getType().getName();
		}
		
		field.setAccessible(true);
	}
	
	String getName() {
		return name;
	}
	
	MBeanAttributeInfo getInfo() {
		return getInfo(null);
	}
	
	MBeanAttributeInfo getInfo(String superName) {
		return new MBeanAttributeInfo(
				superName == null ? name : superName,
				type,
				description,
				isReadable,
				isWritable,
				false);
	}

	Object get() throws IllegalArgumentException, IllegalAccessException {
		Object value = field.get(instance);
		
		if (value == null) {
			return value;
		}
		
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
