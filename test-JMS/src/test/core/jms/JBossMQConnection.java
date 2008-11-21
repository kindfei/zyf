package test.core.jms;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public class JBossMQConnection implements MessageConnection {
	private static final Log log = LogFactory.getLog(JBossMQConnection.class);
	
	private String groupName;
	private String user;
	private String password;
	private String clientID;
	private boolean isHA;
	
	private HostManager hostManager;
	
	private InitialContext context;
	private Connection connection;
	
	private Lock lock = new ReentrantLock();
	
	JBossMQConnection(String groupName, String user, String password, String clientID) throws MessageException {
		this.groupName = groupName;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
		
		hostManager = new HostManager(ProviderType.JBossMQ, groupName);
		isHA = hostManager.isHA();
		
		connect(hostManager.getCurrentURL());
	}
	
	private void connect(String url) throws MessageException {
		lock.lock();
		try {
			log.info("JbossConnection connecting. URL=" + url);
			
			log.info("GroupName:        [" + groupName + "]");
			log.info("HighAvailability: [" + isHA + "]");
			log.info("User:             [" + user + "]");
			log.info("Password:         [" + password + "]");
			log.info("ClientID:         [" + clientID + "]");
			
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.provider.url", url);
			env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			InitialContext ctx = new InitialContext(env);
			
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			Connection conn = cf.createConnection(user, password);
			if (clientID != null) conn.setClientID(clientID);
			if (isHA) conn.setExceptionListener(new FailoverHandler());
			conn.start();
			
			context = ctx;
			connection = conn;
			
			log.info("JbossConnection connected. URL=" + url);
		} catch (Exception e) {
			if (isHA) {
				log.error("JbossConnection connect error.", e);
				log.info("JbossConnection reconnect begin...");
				connect(hostManager.getNextURL());
			} else {
				throw new MessageException("JbossConnection connect error.", e);
			}
		} finally {
			lock.unlock();
		}
	}
	
	private class FailoverHandler implements ExceptionListener {
		@Override
		public void onException(JMSException exception) {
			try {
				connect(hostManager.getNextURL());
			} catch (MessageException e) {
				log.error("Should never have happened.", e);
			}
		}
	}

	@Override
	public Session createSession() throws MessageException {
		lock.lock();
		try {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			throw new MessageException("Create session error.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Destination createDestination(MessageDestination dest) throws MessageException {
		lock.lock();
		try {
			return (Destination) context.lookup(dest.getStrDest());
		} catch (NamingException e) {
			throw new MessageException("Create destination error.", e);
		} finally {
			lock.unlock();
		}
	}
}
