package test.core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ActiveMQConnection implements MessageConnection {
	private static final Log log = LogFactory.getLog(ActiveMQConnection.class);
	
	private Connection connection;
	
	ActiveMQConnection(String groupName, String user, String password, String clientID) throws MessageException {
		try {
			HostManager hm = new HostManager(ProviderType.ActiveMQ, groupName);
			String url = hm.getURL();
			
			log.info("ActivemqConnection connecting. URL=" + url);
			
			log.info("GroupName: [" + groupName + "]");
			log.info("User:      [" + user + "]");
			log.info("Password:  [" + password + "]");
			log.info("ClientID:  [" + clientID + "]");
			
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url);
			connection = cf.createConnection(user, password);
			if (clientID != null) connection.setClientID(clientID);
			connection.start();
			
			log.info("ActivemqConnection connected. URL=" + url);
		} catch (JMSException e) {
			throw new MessageException("ActivemqConnection connect error.", e);
		}
	}
	
	@Override
	public Session createSession() throws MessageException {
		try {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			throw new MessageException("Create session error.", e);
		}
	}

	@Override
	public Destination createDestination(MessageDestination dest) throws MessageException {
		String strDest = dest.getStrDest();
		String type = strDest.split("/")[0];
		try {
			if (type.equals("queue")) {
				return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createQueue(strDest);
			} else if (type.equals("topic")) {
				return connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createTopic(strDest);
			} else {
				throw new MessageException("Create destination error, invalid destination name. strDest=" + strDest);
			}
		} catch (JMSException e) {
			throw new MessageException("Create destination error.", e);
		}
	}
}
