package test.core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQConnection extends AbstractConnection {
	
	ActiveMQConnection(Provider provider, String clientID) throws MessageException {
		super(provider, clientID);
	}
	
	@Override
	protected Connection createConnection(String url, String user, String password) throws Exception {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url);
		Connection conn = null;
		if (user == null || password == null) {
			conn = cf.createConnection();
		} else {
			conn = cf.createConnection(user, password);
		}
		return conn;
	}

	@Override
	protected Destination createDestination(String destName) throws Exception {
		String type = destName.split("/")[0];
		if (type.equals("queue")) {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createQueue(destName);
		} else if (type.equals("topic")) {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createTopic(destName);
		} else {
			throw new IllegalArgumentException("Create destination error, invalid destination name. destName=" + destName);
		}
	}
}
