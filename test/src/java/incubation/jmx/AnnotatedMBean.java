package incubation.jmx;

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
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnnotatedMBean implements DynamicMBean {
	private static Log log = LogFactory.getLog(AnnotatedMBean.class);
	
	private Object instance;
	private String className;
	
	private String description = "Common implementation of a dynamic MBean.";

	private List<MBeanAttributeInfo> attributeList = new ArrayList<MBeanAttributeInfo>();
	private List<MBeanConstructorInfo> constructorList = new ArrayList<MBeanConstructorInfo>();
	private List<MBeanOperationInfo> operationList = new ArrayList<MBeanOperationInfo>();
	private List<MBeanNotificationInfo> notificationList = new ArrayList<MBeanNotificationInfo>();
	
	private MBeanInfo dMBeanInfo = null;

	private Map<String, ReflectionAttribute> attributes = new HashMap<String, ReflectionAttribute>();
	private Map<String, ReflectionOperation> operations = new HashMap<String, ReflectionOperation>();
	
	public AnnotatedMBean(Object instance) {
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
		if (superClass != null) {
			if (superClass != Object.class) {
				init(superClass);
			}
		}
	}
	
	private void initAttributeList(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ATTRIBUTE attr = field.getAnnotation(ATTRIBUTE.class);
			if (attr != null) {
				ReflectionAttribute ra = new ReflectionAttribute(instance, field, attr);
				String name = ra.getName();
				if (attributes.containsKey(name)) {
					name = clazz.getSimpleName() + "." + name;
					String tmpName = name;
					for (int i = 1; attributes.containsKey(tmpName); i++) {
						tmpName = name + i;
					}
					name = tmpName;
				}
				attributeList.add(ra.getInfo(name));
				attributes.put(name, ra);
			}
		}
	}
	
	private void initOperationList(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			OPERATION oper = method.getAnnotation(OPERATION.class);
			if (oper != null) {
				ReflectionOperation ro = new ReflectionOperation(instance, method, oper);
				String name = ro.getName();
				if (operations.containsKey(name)) {
					name = clazz.getSimpleName() + "." + name;
					String tmpName = name;
					for (int i = 1; operations.containsKey(tmpName); i++) {
						tmpName = name + i;
					}
					name = tmpName;
				}
				operationList.add(ro.getInfo(name));
				operations.put(name, ro);
			}
		}
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"),
					"Cannot invoke a getter of " + className + " with null attribute name");
		}
		
		ReflectionAttribute ra = attributes.get(attribute);
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
		
		ReflectionAttribute ra = attributes.get(name);
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
		
		ReflectionOperation ro = operations.get(actionName);
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
