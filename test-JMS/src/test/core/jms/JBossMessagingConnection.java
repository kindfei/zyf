package test.core.jms;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class JBossMessagingConnection implements MessageConnection {
	private static final Log log = LogFactory.getLog(JBossMessagingConnection.class);

	private InitialContext context;
	private Connection connection;
	
	JBossMessagingConnection(String groupName, String user, String password, String clientID) throws MessageException {
		try {
			HostManager hm = new HostManager(ProviderType.JBossMessaging, groupName);
			String url = hm.getURL();
			
			log.info("MessagingConnection connecting. URL=" + url);
			
			log.info("GroupName: [" + groupName + "]");
			log.info("User:      [" + user + "]");
			log.info("Password:  [" + password + "]");
			log.info("ClientID:  [" + clientID + "]");
			
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.provider.url", url);
			env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			context = new InitialContext(env);
			
			ConnectionFactory cf = (ConnectionFactory) context.lookup("/ConnectionFactory");;
			connection = cf.createConnection(user, password);
			if (clientID != null) connection.setClientID(clientID);
			connection.start();
			
			log.info("MessagingConnection connected. URL=" + url);
		} catch (Exception e) {
			throw new MessageException("MessagingConnection connect error.", e);
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
		try {
			return (Destination) context.lookup(dest.getStrDest());
		} catch (NamingException e) {
			throw new MessageException("Create destination error.", e);
		}
	}
}
