package core.jmx;

import java.lang.reflect.Field;

public class RelfectionAttribute {
	private Object instance;
	private Field field;
	
	public RelfectionAttribute(Object instance, Field field) {
		this.instance = instance;
		this.field = field;
		
		field.setAccessible(true);
	}

	public Object get() throws IllegalArgumentException, IllegalAccessException {
		return field.get(instance);
	}

	public void set(Object value) throws IllegalArgumentException, IllegalAccessException {
		field.set(instance, value);
	}
}
