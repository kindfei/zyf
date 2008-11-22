package test.core.jms;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {
	
	private static ProviderType type;
	private static String clientID;
	private static Map<String, MessageConnection> connectionMap = new HashMap<String, MessageConnection>();
	
	static {
		type = ProviderType.JBossMQ;
		
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
	
	static synchronized MessageConnection getConnection(String groupId) throws MessageException {
		MessageConnection conn = connectionMap.get(groupId);
		
		if (conn == null) {
			
			switch (type) {
			case JBossMQ:
				conn = new JBossMQConnection(groupId, clientID);
				break;

			case ActiveMQ:
				conn = new ActiveMQConnection(groupId, clientID);
				break;

			case JBossMessaging:
				conn = new JBossMessagingConnection(groupId, clientID);
				break;
			}
			
			connectionMap.put(groupId, conn);
		}
		
		return conn;
	}
}
