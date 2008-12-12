package core.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManagementBeanServer {
	private static final Log log = LogFactory.getLog(ConnectorServer.class);
	
	private static final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	
	private static final String domain = "test";
	
	public static void register(Object instance) {
		register(instance, null);
	}
	
	public static void register(Object instance, String name) {
		try {
			CommonDynamicMBean mbean = new CommonDynamicMBean(instance);
			
			ObjectName mbeanObjectName = createObjectName(instance, name);
			
			mbs.registerMBean(mbean, mbeanObjectName);
			
			log.info(instance.getClass().getName() + " is registered as MBean.");
		} catch (Exception e) {
			log.error("Register MBean error. instance=" + instance + " name=" + name, e);
		}
	}
	
	public static void unregister(Object instance) {
		unregister(instance, null);
	}
	
	public static void unregister(Object instance, String name) {
		try {
			mbs.unregisterMBean(createObjectName(instance, name));
		} catch (Exception e) {
			log.error("Unregister MBean error. instance=" + instance + " name=" + name, e);
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
