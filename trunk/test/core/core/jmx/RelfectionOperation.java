package core.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RelfectionOperation {
	private Object instance;
	private Method method;
	
	public RelfectionOperation(Object instance, Method method) {
		this.instance = instance;
		this.method = method;
	}

	public Object invoke(Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(instance, params);
	}
}
