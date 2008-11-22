package test.core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQConnection extends AbstractConnection {
	
	ActiveMQConnection(String groupName, String clientID) throws MessageException {
		super(ProviderType.ActiveMQ, groupName, clientID);
	}
	
	@Override
	protected Connection createConnection(String url, String user, String password) throws Exception {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url);
		return cf.createConnection(user, password);
	}

	@Override
	protected Destination createDestination(String strDest) throws Exception {
		String type = strDest.split("/")[0];
		if (type.equals("queue")) {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createQueue(strDest);
		} else if (type.equals("topic")) {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createTopic(strDest);
		} else {
			throw new IllegalArgumentException("Create destination error, invalid destination name. strDest=" + strDest);
		}
	}
}
