package test.jms.jboss.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public abstract class DefaultMessenger extends Messenger {
	private static final Log log = LogFactory.getLog(DefaultMessenger.class);

	private String destName;
	private Connector connector;
	
	private boolean isOpened;
	
	protected Lock rebuildLock = new ReentrantLock();
	protected Condition rebuilt = rebuildLock.newCondition();
	
	protected DefaultMessenger(String destName) {
		connector = ConnectorFactory.getConnector(destName);
		this.destName = destName;
	}
	
	protected void executeBuild() throws BuildException, FailoverException, ConnectException {
		log.info("Build start. Destination:" + destName);
		connector.buildMessenger(this, destName);
		isOpened = true;
		log.info("Build end. Destination:" + destName);
	}
	
	public String getDestName() {
		return destName;
	}
	
	public void run() {
		rebuildLock.lock();
		try {
			if (!isOpened) {
				log.info("Messenger is closed. So cancel the rebuild process.");
				return;
			}
			
			log.info("Rebuild start. Destination:" + destName);
			innerClose();
			connector.buildMessenger(this, destName);
			rebuilt.signalAll();
			log.info("Rebuild end. Destination:" + destName);
			
		} catch (BuildException e) {
			log.error("Rebuild failed.", e);
		} catch (FailoverException e) {
			log.error("Failover failed.", e);
		} catch (ConnectException e) {
			log.error("Connect failed.", e);
		} finally {
			rebuildLock.unlock();
		}
	}
	
	public void close() {
		rebuildLock.lock();
		try {
			connector.removeMessenger(this);
			innerClose();
			isOpened = false;
		} finally {
			rebuildLock.unlock();
		}
	}

	protected abstract void innerClose();
}
