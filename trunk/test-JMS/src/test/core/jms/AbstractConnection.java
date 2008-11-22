package test.core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConnection implements MessageConnection {
	private static final Log log = LogFactory.getLog(AbstractConnection.class);
	
	private ProviderType type;
	private String groupName;
	private String clientID;
	private String user;
	private String password;
	private boolean manualHA;
	
	private ProviderManager hostManager;
	
	protected Connection connection;
	
	private Lock lock = new ReentrantLock();
	
	AbstractConnection(ProviderType type, String groupName, String clientID) throws MessageException {
		this.type = type;
		this.groupName = groupName;
		this.clientID = clientID;
		
		this.hostManager = new ProviderManager(type, groupName);
		this.user = hostManager.getUser();
		this.password = hostManager.getPassword();
		this.manualHA = hostManager.isManualHa();
		
		connect(hostManager.getCurrentURL());
	}
	
	private void connect(String url) throws MessageException {
		try {
			log.info("MessageConnection begin connecting. " + getInfo(url));

			Connection conn = createConnection(url, user, password);
			if (clientID != null) conn.setClientID(clientID);
			if (manualHA) conn.setExceptionListener(new FailoverHandler(url));

			connection = conn;
			connection.start();
			
			log.info("MessageConnection successfully connected. " + getInfo(url));
		} catch (Exception e) {
			if (manualHA) {
				log.error("MessageConnection connect error. " + getInfo(url), e);
				log.info("reconnect begin...");
				connect(hostManager.getNextURL());
			} else {
				throw new MessageException("MessageConnection connect error. " + getInfo(url), e);
			}
		}
	}
	
	protected abstract Connection createConnection(String url, String user, String password) throws Exception;
	
	private class FailoverHandler implements ExceptionListener {
		private String url;
		private boolean isClosed;
		
		private FailoverHandler(String url) {
			this.url = url;
		}
		
		@Override
		public void onException(JMSException exception) {
			lock.lock();
			
			if (isClosed) return;
			
			log.error("MessageConnection on exception. " + getInfo(url), exception);
			
			try {
				connection.stop();
			} catch (JMSException e) {
			}
			try {
				connection.close();
			} catch (JMSException e) {
			}
			
			isClosed = true;
			
			log.info("reconnect begin...");
			try {
				connect(hostManager.getNextURL());
			} catch (MessageException e) {
				log.error("Should never have happened.", e);
			} finally {
				lock.unlock();
			}
		}
	}

	private String getInfo(String url) {
	    final String separator = ", ";
	    StringBuilder info = new StringBuilder();
	    info.append("MessageConnection[")
        	.append("URL=").append(url).append(separator)
	        .append("type=").append(this.type).append(separator)
	        .append("groupName=").append(this.groupName).append(separator)
	        .append("user=").append(this.user).append(separator)
	        .append("password=").append(this.password).append(separator)
	        .append("clientID=").append(this.clientID).append(separator)
	        .append("manualHA=").append(this.manualHA)
	        .append("]");
	    return info.toString();
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
			return createDestination(dest.getStrDest());
		} catch (Exception e) {
			throw new MessageException("Create destination error.", e);
		} finally {
			lock.unlock();
		}
	}
	
	protected abstract Destination createDestination(String strDest) throws Exception;
}
