package core.jmx;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.Descriptor;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import com.sun.jmx.mbeanserver.Introspector;

public class ReflectionOperation {
	private Object instance;
	private Method method;
	
	private String name;
	private String description;
	private String type;
	
	ReflectionOperation(Object instance, Method method, OPERATION oper) {
		this.instance = instance;
		this.method = method;
		
		this.name = oper.name();
		this.description = oper.description();
		this.type = oper.type();
		
		if (name.equals("")) {
			name = method.getName();
		}
		
		if (type.equals("")) {
			type = method.getReturnType().getName();
		}
		
		method.setAccessible(true);
	}
	
	String getName() {
		return name;
	}
	
	MBeanOperationInfo getInfo() {
		return getInfo(null);
	}
	
	MBeanOperationInfo getInfo(String superName) {
		return new MBeanOperationInfo(
				superName == null ? name : superName,
				description,
				methodSignature(method),
				method.getReturnType().getName(),
				MBeanOperationInfo.UNKNOWN);
	}

	Object invoke(Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return method.invoke(instance, params);
	}
	
    private static MBeanParameterInfo[] methodSignature(Method method) {
		final Class<?>[] classes = method.getParameterTypes();
		final Annotation[][] annots = method.getParameterAnnotations();
		return parameters(classes, annots);
	}

    private static MBeanParameterInfo[] parameters(Class<?>[] classes, Annotation[][] annots) {
		final MBeanParameterInfo[] params = new MBeanParameterInfo[classes.length];
		assert (classes.length == annots.length);

		for (int i = 0; i < classes.length; i++) {
			Descriptor d = Introspector.descriptorForAnnotations(annots[i]);
			final String pn = "p" + (i + 1);
			params[i] = new MBeanParameterInfo(pn, classes[i].getName(), "", d);
		}

		return params;
	}
}
