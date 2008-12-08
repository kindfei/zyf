package core.entry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class EntryMBean extends NotificationBroadcasterSupport implements DynamicMBean {
	private String dClassName = this.getClass().getName();

	private MBeanAttributeInfo[] dAttributes = null;
	private MBeanConstructorInfo[] dConstructors = null;
	private MBeanOperationInfo[] dOperations = null;
	private MBeanNotificationInfo[] dNotifications = null;
	
	private MBeanInfo dMBeanInfo = null;
	
	private Map<String, Command> commands = new HashMap<String, Command>();
	
	private JMXConnectorServer connectorServer;
	
	protected EntryMBean() {
		Constructor<?>[] constructors = this.getClass().getConstructors();
		dConstructors = new MBeanConstructorInfo[constructors.length];
		for (int i = 0; i < constructors.length; i++) {
			dConstructors[i] = new MBeanConstructorInfo("Construct", constructors[i]);
		}

		dOperations = getOperations();

		dMBeanInfo = new MBeanInfo(dClassName, "ServiceEntry MBean", dAttributes, dConstructors, dOperations, dNotifications);
	}
	
	private MBeanOperationInfo[] getOperations() {
		ArrayList<MBeanOperationInfo> operationList = new ArrayList<MBeanOperationInfo>();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			CMD cmd = method.getAnnotation(CMD.class);
			if (cmd != null) {
				commands.put(method.getName(), new RelfectionCommand(cmd.key(), cmd.type(), cmd.description(), this, method));
				operationList.add(new MBeanOperationInfo(cmd.description(), method));
			}
		}
		return operationList.toArray(new MBeanOperationInfo[operationList.size()]);
	}
	
	protected void startServer() throws Exception {
		MBeanServer mbs = MBeanServerFactory.createMBeanServer();
		
		String mbeanObjectNameStr = mbs.getDefaultDomain() + ":type=" + dClassName + ",name=1";
		ObjectName mbeanObjectName = ObjectName.getInstance(mbeanObjectNameStr);
		mbs.registerMBean(this, mbeanObjectName);
		
		LocateRegistry.createRegistry(9999);
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
		Map<String, Object> environment = null;
		connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mbs);
		
		connectorServer.start();
	}
	
	protected void stopServer() throws Exception {
		connectorServer.stop();
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return dMBeanInfo;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		
		if (actionName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"),
					"Cannot invoke a null operation in " + dClassName);
		}
		
		Command command = commands.get(actionName);
		if (command != null) {
			try {
				return command.execute(params);
			} catch (Exception e) {
				new MBeanException(e, "Execute error. actionName=" + actionName + ", error=" + e.getMessage());
			}
		}
		
		throw new ReflectionException(new NoSuchMethodException(actionName), "Cannot find the operation "
				+ actionName + " in " + dClassName);
	}

	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		return null;
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		return null;
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		return null;
	}

}
