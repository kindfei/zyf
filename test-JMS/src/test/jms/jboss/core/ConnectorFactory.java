package test.jms.jboss.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectorFactory {
	private static final Log log = LogFactory.getLog(ConnectorFactory.class);
	private static final String fileName = "JMSConnector.properties";
	private static final String prefix = "DEST_";
	private static Map connectorMap;
	private static Properties prop;
	private static boolean isHA;
	private static String user;
	private static String password;
	private static String clientID;
	
	static {
		connectorMap = new HashMap();
		prop = getProperties();
		isHA = !Boolean.valueOf(prop.getProperty("DISABLE_HIGH_AVAILABILITY")).booleanValue();
		user = prop.getProperty("CONNECT_USER");
		password = prop.getProperty("CONNECT_PASSWORD");
		boolean setClientID = Boolean.valueOf(prop.getProperty("SET_CLIENT_ID")).booleanValue();
		if (setClientID) {
			String name = System.getProperty("processName");
			if (name != null && !name.equals("")) {
				StringBuffer sb = new StringBuffer();
				sb.append(name);
				try {
					String ip = InetAddress.getLocalHost().getHostAddress();
					sb.append("-").append(ip);
				} catch (UnknownHostException e) {
				}
				clientID = sb.toString();
			}
		}
	}
	
	private static Properties getProperties() {
		InputStream in = null;
		
		String path = System.getProperty("configPath");
		if (path != null)
			try {
				in = new FileInputStream(path + "/" + fileName);
			} catch (FileNotFoundException e) {
			}
		
		if (in == null)
			in = ClassLoader.getSystemResourceAsStream(fileName);

		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			log.error("Properties File Not Found.", e);
		}
		
		return p;
	}
	
	public synchronized static Connector getConnector(String destName) {
		String groupName = prop.getProperty(prefix + destName);
		if (groupName == null || groupName.equals("")) {
			log.error(destName + " is not configed in " + fileName);
			return null;
		}
		DefaultConnector inst = (DefaultConnector) connectorMap.get(groupName);
		if (inst == null) {
			inst = new DefaultConnector(groupName, isHA, user, password, clientID);
			connectorMap.put(groupName, inst);
		}
		return inst;
	}
	
}
