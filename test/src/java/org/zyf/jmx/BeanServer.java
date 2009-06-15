package org.zyf.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanServer {
	private static final Log log = LogFactory.getLog(ConnectorServer.class);
	
	private static final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	
	private static final String domain = "test";
	
	public static void annotatedRegister(Object instance, String name) {
		try {
			AnnotatedMBean mbean = new AnnotatedMBean(instance);
			ObjectName mbeanObjectName = createObjectName(instance, name);
			mbs.registerMBean(mbean, mbeanObjectName);
			
			log.info(instance.getClass().getName() + " is registered as MBean. name=" + name);
		} catch (Exception e) {
			log.error("Register MBean error. instance=" + instance + " name=" + name, e);
		}
	}
	
	public static <T> void standardRegister(T implementation, Class<T> mbeanInterface, String name) {
		try {
			StandardMBean mbean = new StandardMBean(implementation, mbeanInterface);
			ObjectName mbeanObjectName = createObjectName(implementation, name);
			mbs.registerMBean(mbean, mbeanObjectName);
			
			log.info(implementation.getClass().getName() + " is registered as MBean. name="
					+ name + ", mbeanInterface=" + mbeanInterface.getName());
		} catch (Exception e) {
			log.error("Register MBean error. implementation=" + implementation + " name=" + name, e);
		}
	}
	
	public static void unregister(Object object, String name) {
		try {
			mbs.unregisterMBean(createObjectName(object, name));
		} catch (Exception e) {
			log.error("Unregister MBean error. object=" + object + " name=" + name, e);
		}
	}
	
	private static ObjectName createObjectName(Object instance, String name)
			throws MalformedObjectNameException, NullPointerException {
		String str = domain + ":type=" + instance.getClass().getName() + ",name=";
		if (name == null) {
			str += System.identityHashCode(instance);
		} else {
			str += name + "(" + System.identityHashCode(instance) + ")";
		}

		return ObjectName.getInstance(str);
	}
	
	public static MBeanServer getMBeanServer() {
		return mbs;
	}
}
