package core.jmx;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
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
	private String dClassName;
	
	private String dDescription = "Common implementation of a dynamic MBean.";

	private MBeanAttributeInfo[] dAttributes = null;
	private MBeanConstructorInfo[] dConstructors = null;
	private MBeanOperationInfo[] dOperations = null;
	private MBeanNotificationInfo[] dNotifications = new MBeanNotificationInfo[1];
	
	private MBeanInfo dMBeanInfo = null;

	private Map<String, RelfectionAttribute> attributes = new HashMap<String, RelfectionAttribute>();
	private Map<String, RelfectionOperation> operations = new HashMap<String, RelfectionOperation>();
	
	public CommonDynamicMBean(Object instance) {
		this.instance = instance;
		this.dClassName = instance.getClass().getName();

		this.dAttributes = getAttributes();
		this.dOperations = getOperations();
		
		dNotifications[0] = new MBeanNotificationInfo(
				new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
				AttributeChangeNotification.class.getName(),
				"This notification is emitted when the attributes changed.");

		this.dMBeanInfo = new MBeanInfo(dClassName, dDescription, dAttributes, dConstructors, dOperations, dNotifications);
	}
	
	private MBeanAttributeInfo[] getAttributes() {
		ArrayList<MBeanAttributeInfo> attributeList = new ArrayList<MBeanAttributeInfo>();
		Field[] fields = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			ATTRIBUTE attr = field.getAnnotation(ATTRIBUTE.class);
			if (attr != null) {
				attributeList.add(new MBeanAttributeInfo(field.getName(), field.getType().getName(), attr.description(), attr.isReadable(), attr.isWritable(), false));
				attributes.put(field.getName(), new RelfectionAttribute(instance, field));
			}
		}
		return attributeList.toArray(new MBeanAttributeInfo[attributeList.size()]);
	}
	
	private MBeanOperationInfo[] getOperations() {
		ArrayList<MBeanOperationInfo> operationList = new ArrayList<MBeanOperationInfo>();
		Method[] methods = instance.getClass().getDeclaredMethods();
		for (Method method : methods) {
			OPERATION oper = method.getAnnotation(OPERATION.class);
			if (oper != null) {
				operationList.add(new MBeanOperationInfo(oper.description(), method));
				operations.put(method.getName(), new RelfectionOperation(instance, method));
			}
		}
		return operationList.toArray(new MBeanOperationInfo[operationList.size()]);
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"),
					"Cannot invoke a getter of " + dClassName + " with null attribute name");
		}
		
		RelfectionAttribute ra = attributes.get(attribute);
		if (ra == null) {
			throw new AttributeNotFoundException("Cannot find " + attribute + " attribute in " + dClassName);
		}
		
		try {
			return ra.get();
		} catch (Exception e) {
			throw new MBeanException(e, "Get " + attribute + " attribute error in " + dClassName);
		}
	}

	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"),
					"Cannot invoke a setter of " + dClassName + " with null attribute");
		}
		String name = attribute.getName();
		Object value = attribute.getValue();
		if (name == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"),
					"Cannot invoke the setter of " + dClassName + " with null attribute name");
		}
		
		RelfectionAttribute ra = attributes.get(name);
		if (ra == null) {
			throw new AttributeNotFoundException("Attribute " + name + " not found in " + dClassName);
		}
		
		try {
			ra.set(value);
		} catch (Exception e) {
			throw new MBeanException(e, "Set " + attribute + " attribute error in " + dClassName);
		}
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"),
					"Cannot invoke a getter of " + dClassName);
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
					"Cannot invoke a setter of " + dClassName);
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
					"Cannot invoke a null operation in " + dClassName);
		}
		
		RelfectionOperation ro = operations.get(actionName);
		if (ro == null) {
			throw new ReflectionException(new NoSuchMethodException(actionName), "Cannot find the operation " + actionName + " in " + dClassName);
		}
		
		try {
			return ro.invoke(params);
		} catch (Exception e) {
			throw new MBeanException(e, "Invoke the operation " + actionName + " error in " + dClassName);
		}
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return dMBeanInfo;
	}

}
