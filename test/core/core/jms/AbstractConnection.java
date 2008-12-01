package core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConnection implements MessageConnection {
	private static final Log log = LogFactory.getLog(AbstractConnection.class);

	private Provider provider;
	
	private String providerId;
	private ProviderType providerType;
	private boolean manualHA;
	private String user;
	private String password;
	private String clientID;
	
	protected Connection connection;
	
	private Lock lock = new ReentrantLock();
	
	AbstractConnection(Provider provider, String clientID) throws MessageException {
		this.provider = provider;
		this.providerId = provider.getProviderId();
		this.providerType = provider.getProviderType();
		this.manualHA = provider.isManualHa();
		this.user = provider.getUser();
		this.password = provider.getPassword();
		this.clientID = clientID;
		
		connect(provider.getURL());
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
		} catch (InvalidClientIDException e) {
			throw new MessageException("MessageConnection connect error, clientID [" + clientID + "] is already registered.", e);
		} catch (Exception e) {
			if (manualHA) {
				log.error("MessageConnection connect error. " + getInfo(url), e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
				}
				log.info("reconnect begin...");
				connect(provider.getNextURL());
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
			try {
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
				connect(provider.getNextURL());
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
	        .append("ProviderId=").append(this.providerId).append(separator)
	        .append("ProviderType=").append(this.providerType).append(separator)
	        .append("manualHA=").append(this.manualHA).append(separator)
        	.append("URL=").append(url).append(separator)
	        .append("user=").append(this.user).append(separator)
	        .append("password=").append(this.password).append(separator)
	        .append("clientID=").append(this.clientID).append("]");
	    return info.toString();
	}

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws MessageException {
		lock.lock();
		try {
			return connection.createSession(transacted, acknowledgeMode);
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
			return createDestination(dest.getDestName());
		} catch (Exception e) {
			throw new MessageException("Create destination error.", e);
		} finally {
			lock.unlock();
		}
	}
	
	protected abstract Destination createDestination(String destName) throws Exception;
}
