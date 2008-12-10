package core.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
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
			
			String mbeanObjectNameStr = null;
			if (name == null) {
				mbeanObjectNameStr = domain + ":type=" + instance.getClass().getName() + ",name=" + System.identityHashCode(mbean);
			} else {
				mbeanObjectNameStr = domain + ":type=" + instance.getClass().getName() + ",name=" + name + "(" + System.identityHashCode(mbean) + ")";
			}
			ObjectName mbeanObjectName = ObjectName.getInstance(mbeanObjectNameStr);
			mbs.registerMBean(mbean, mbeanObjectName);
		} catch (Exception e) {
			log.error("Register MBean error. instance=" + instance + " name=" + name, e);
		}
	}
	
	public static MBeanServer getMBeanServer() {
		return mbs;
	}
}
