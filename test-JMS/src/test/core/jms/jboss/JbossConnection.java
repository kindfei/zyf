package test.core.jms.jboss;

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

import test.core.jms.MessageDestination;
import test.jms.jboss.core.HostManager;

public class JbossConnection {
	private static final Log log = LogFactory.getLog(JbossConnection.class);
	
	private String groupName;
	private String user;
	private String password;
	private String clientID;
	private boolean isHA;
	
	private HostManager hostManager;
	
	private InitialContext context;
	private Connection connection;
	
	private Lock lock = new ReentrantLock();
	
	private JbossConnection(String groupName, boolean isHA, String user, String password, String clientID) {
		this.groupName = groupName;
		this.isHA = isHA;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
		
		hostManager = new HostManager(groupName);
		
		connect(hostManager.getCurrentHost());
	}
	
	private void connect(String url) {
		lock.lock();
		try {
			log.info("Connecting to " + url);
			log.info("Group Name:        [" + groupName + "]");
			log.info("High Availability: [" + isHA + "]");
			log.info("User:              [" + user + "]");
			log.info("Password:          [" + password + "]");
			log.info("Client ID:         [" + clientID + "]");
			
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
			log.info("Connected to " + url);
		} catch (Exception e) {
			log.error("Connect error.", e);
			log.info("Reconnect begin.");
			connect(hostManager.getNextHost());
		} finally {
			lock.unlock();
		}
	}
	
	private class FailoverHandler implements ExceptionListener {
		@Override
		public void onException(JMSException exception) {
			connect(hostManager.getNextHost());
		}
	}

	Session createSession() throws JMSException {
		lock.lock();
		try {
			return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} finally {
			lock.unlock();
		}
	}

	Destination createDestination(MessageDestination dest) throws NamingException {
		lock.lock();
		try {
			return (Destination) context.lookup(dest.getStrDest());
		} finally {
			lock.unlock();
		}
	}
}
