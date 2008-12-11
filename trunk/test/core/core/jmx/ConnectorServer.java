package core.jmx;

import java.rmi.registry.LocateRegistry;
import java.util.Map;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.entry.ServiceDefinition;

public class ConnectorServer {
	private static Log log = LogFactory.getLog(ConnectorServer.class);
	
	private static JMXConnectorServer connectorServer;
	
	public static synchronized void startServer() {
		if (connectorServer != null) return;
		
		try {
			int port = ServiceDefinition.getServiceDefinition().getRegisterPort();
			LocateRegistry.createRegistry(port);
			String str = "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/server";
			JMXServiceURL url = new JMXServiceURL(str);
			Map<String, Object> environment = null;
			connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, environment, ManagementBeanServer.getMBeanServer());
			
			connectorServer.start();
			
			log.info("JMXConnectorServer started. url=" + str);
		} catch (Exception e) {
			log.error("Start JMXConnectorServer error.", e);
			connectorServer = null;
		}
	}
	
	public static synchronized void stopServer() {
		try {
			connectorServer.stop();
		} catch (Exception e) {
			log.error("Stop JMXConnectorServer error.", e);
		} finally {
			connectorServer = null;
		}
	}
}
