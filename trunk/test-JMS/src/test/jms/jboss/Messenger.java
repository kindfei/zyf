package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public abstract class Messenger implements Runnable {
	private static final Log log = LogFactory.getLog(Messenger.class);

	private String destName;
	private Connector connector;
	
	private boolean isOpened;
	
	protected Lock rebuildLock = new ReentrantLock();
	protected Condition rebuilt = rebuildLock.newCondition();
	
	protected Messenger(String destName) {
		connector = ConnectorFactory.getConnector(destName);
		this.destName = destName;
	}
	
	protected void executeBuild() throws BuildException, FailoverException, ConnectException {
		log.info("Build start. Destination:" + destName);
		connector.buildMessenger(this, destName);
		isOpened = true;
		log.info("Build end. Destination:" + destName);
	}
	
	protected abstract void build(Connection conn, Destination dest) throws JMSException;
	
	public String getDestName() {
		return destName;
	}
	
	public void run() {
		rebuildLock.lock();
		if (!isOpened) return;
		try {
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
