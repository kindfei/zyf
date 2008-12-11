package core.jmx;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonDynamicMBean extends NotificationBroadcasterSupport implements DynamicMBean {
	private static Log log = LogFactory.getLog(CommonDynamicMBean.class);
	
	private Object instance;
	private String className;
	
	private String description = "Common implementation of a dynamic MBean.";

	private List<MBeanAttributeInfo> attributeList = new ArrayList<MBeanAttributeInfo>();
	private List<MBeanConstructorInfo> constructorList = new ArrayList<MBeanConstructorInfo>();
	private List<MBeanOperationInfo> operationList = new ArrayList<MBeanOperationInfo>();
	private List<MBeanNotificationInfo> notificationList = new ArrayList<MBeanNotificationInfo>();
	
	private MBeanInfo dMBeanInfo = null;

	private Map<String, RelfectionAttribute> attributes = new HashMap<String, RelfectionAttribute>();
	private Map<String, RelfectionOperation> operations = new HashMap<String, RelfectionOperation>();
	
	public CommonDynamicMBean(Object instance) {
		this.instance = instance;
		this.className = instance.getClass().getName();

		init(instance.getClass());

		this.dMBeanInfo = new MBeanInfo(
				className,
				description,
				attributeList.toArray(new MBeanAttributeInfo[0]),
				constructorList.toArray(new MBeanConstructorInfo[0]),
				operationList.toArray(new MBeanOperationInfo[0]),
				notificationList.toArray(new MBeanNotificationInfo[0]));
	}
	
	private void init(Class<?> clazz) {
		initAttributeList(clazz);
		initOperationList(clazz);
		
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null) return;
		init(superClass);
	}
	
	private void initAttributeList(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ATTRIBUTE attr = field.getAnnotation(ATTRIBUTE.class);
			if (attr != null) {
				RelfectionAttribute ra = new RelfectionAttribute(instance, field, attr);
				attributeList.add(ra.getInfo());
				attributes.put(field.getName(), ra);
			}
		}
	}
	
	private void initOperationList(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			OPERATION oper = method.getAnnotation(OPERATION.class);
			if (oper != null) {
				RelfectionOperation ro = new RelfectionOperation(instance, method, oper);
				operationList.add(ro.getInfo());
				operations.put(method.getName(), ro);
			}
		}
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"),
					"Cannot invoke a getter of " + className + " with null attribute name");
		}
		
		RelfectionAttribute ra = attributes.get(attribute);
		if (ra == null) {
			throw new AttributeNotFoundException("Cannot find " + attribute + " attribute in " + className);
		}
		
		try {
			return ra.get();
		} catch (Exception e) {
			throw new MBeanException(e, "Get " + attribute + " attribute error in " + className);
		}
	}

	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"),
					"Cannot invoke a setter of " + className + " with null attribute");
		}
		String name = attribute.getName();
		Object value = attribute.getValue();
		if (name == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"),
					"Cannot invoke the setter of " + className + " with null attribute name");
		}
		
		RelfectionAttribute ra = attributes.get(name);
		if (ra == null) {
			throw new AttributeNotFoundException("Attribute " + name + " not found in " + className);
		}
		
		try {
			ra.set(value);
		} catch (Exception e) {
			throw new MBeanException(e, "Set " + attribute + " attribute error in " + className);
		}
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"),
					"Cannot invoke a getter of " + className);
		}
		
		AttributeList resultList = new AttributeList();

		if (attributes.length == 0)
			return resultList;

		for (int i = 0; i < attributes.length; i++) {
			try {
				Object value = getAttribute((String) attributes[i]);
				resultList.add(new Attribute(attributes[i], value));
			} catch (Exception e) {
				log.error("getAttribute error." + e);
			}
		}
		return resultList;
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList attributes cannot be null"),
					"Cannot invoke a setter of " + className);
		}
		AttributeList resultList = new AttributeList();

		if (attributes.isEmpty())
			return resultList;

		for (Iterator<Object> i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				resultList.add(new Attribute(name, value));
			} catch (Exception e) {
				log.error("setAttribute error." + e);
			}
		}
		return resultList;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		
		if (actionName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"),
					"Cannot invoke a null operation in " + className);
		}
		
		RelfectionOperation ro = operations.get(actionName);
		if (ro == null) {
			throw new ReflectionException(new NoSuchMethodException(actionName), "Cannot find the operation " + actionName + " in " + className);
		}
		
		try {
			return ro.invoke(params);
		} catch (Exception e) {
			throw new MBeanException(e, "Invoke the operation " + actionName + " error in " + className);
		}
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return dMBeanInfo;
	}

}
