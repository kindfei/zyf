package org.zyf.jms;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {
	private static String clientID;
	private static Map<String, MessageConnection> connectionMap = new HashMap<String, MessageConnection>();
	
	static {
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
	
	static synchronized MessageConnection getConnection(Provider provider) throws MessageException {
		MessageConnection conn = connectionMap.get(provider.getProviderId());
		
		if (conn == null) {
			
			switch (provider.getProviderType()) {
			case JBossMQ:
				conn = new JBossMQConnection(provider, clientID);
				break;

			case ActiveMQ:
				conn = new ActiveMQConnection(provider, clientID);
				break;

			case JBossMessaging:
				conn = new JBossMessagingConnection(provider, clientID);
				break;
			}
			
			connectionMap.put(provider.getProviderId(), conn);
		}
		
		return conn;
	}
}
