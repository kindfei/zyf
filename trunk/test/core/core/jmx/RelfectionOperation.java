package core.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.MBeanOperationInfo;

public class RelfectionOperation {
	private Object instance;
	private Method method;
	
	private MBeanOperationInfo info;
	
	RelfectionOperation(Object instance, Method method, OPERATION oper) {
		this.instance = instance;
		this.method = method;
		
		info = new MBeanOperationInfo(oper.description(), method);
		
		method.setAccessible(true);
	}
	
	MBeanOperationInfo getInfo() {
		return info;
	}

	Object invoke(Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(instance, params);
	}
}
