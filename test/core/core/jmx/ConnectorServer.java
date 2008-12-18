package core.jmx;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import core.entry.ServiceDefinition;

public class ConnectorServer {
	private static Log log = LogFactory.getLog(ConnectorServer.class);
	
	private static String strUrl;
	
	private static JMXConnectorServer connectorServer;
	
	static {
		int port = ServiceDefinition.getServiceDefinition().getRegisterPort();
		try {
			LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			log.error("Create Registry error. port=" + port, e);
		}
		
		strUrl = "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmx";
	}
	
	public static synchronized void startServer() {
		if (connectorServer != null) return;
		
		try {
			JMXServiceURL url = new JMXServiceURL(strUrl);
			MBeanServer mbeanServer = BeanServer.getMBeanServer();
			JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbeanServer);
			connectorServer.start();
			
			ConnectorServer.connectorServer = connectorServer;
			
			log.info("JMXConnectorServer is started. url=" + strUrl);
		} catch (Exception e) {
			log.error("Start JMXConnectorServer error.", e);
		}
	}
	
	public static synchronized void stopServer() {
		if (connectorServer == null) return;
		
		try {
			connectorServer.stop();
			
			log.info("JMXConnectorServer is stopped. url=" + strUrl);
		} catch (Exception e) {
			log.error("Stop JMXConnectorServer error.", e);
		} finally {
			connectorServer = null;
		}
	}
}
