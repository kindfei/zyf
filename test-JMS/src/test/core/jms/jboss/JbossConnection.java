package test.core.jms.jboss;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

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
	private Set<JbossReceiver> rebuildTargets = new HashSet<JbossReceiver>();
	
	private JbossConnection(String groupName, boolean isHA, String user, String password, String clientID) {
		this.groupName = groupName;
		this.isHA = isHA;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
		
		hostManager = new HostManager(groupName);
	}
	
	private void connect(String url) {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.provider.url", url);
			env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			InitialContext ctx = new InitialContext(env);
			
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			Connection conn = cf.createConnection(user, password);
			if (clientID != null) conn.setClientID(clientID);
			if (isHA) conn.setExceptionListener(new ExceptionHandler(url));
			conn.start();
			
			context = ctx;
			connection = conn;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void reconnect() {
		
	}
	
	private class ExceptionHandler implements ExceptionListener {
		private String url;
		
		private ExceptionHandler(String url) {
			this.url = url;
		}

		@Override
		public void onException(JMSException exception) {

		}
		
	}
	
	void setTarget(JbossReceiver receiver) {
		rebuildTargets.add(receiver);
	}
	
	void removeTarget(JbossReceiver receiver) {
		rebuildTargets.remove(receiver);
	}

	Session createSession() throws JMSException {
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	Destination createDestination(MessageDestination dest) throws NamingException {
		return (Destination) context.lookup(dest.getStrDest());
	}
}
