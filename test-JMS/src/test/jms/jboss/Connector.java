package test.jms.jboss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public class Connector {
	private static final Log log = LogFactory.getLog(Connector.class);
	
	private String groupName;
	private boolean isHA;
	private String user;
	private String password;
	private String clientID;
	private Lock failoverLock;
	private Condition reconnected;
	private List failHosts;
	private ExecutorService exeServ;
	private Set messengerSet;
	
	private String currentHost;
	private InitialContext context;
	private Connection connection;
	private boolean isOpened;
	
	public Connector(String groupName, boolean isHA, String user, String password, String clientID) {
		this.groupName = groupName;
		this.isHA = isHA;
		this.user = user;
		this.password = password;
		this.clientID = clientID;
		failoverLock = new ReentrantLock();
		reconnected = failoverLock.newCondition();
		failHosts = new ArrayList();
		exeServ = Executors.newCachedThreadPool();
		messengerSet = new HashSet();
	}
	
	public void buildMessenger(Messenger target, String destName) throws BuildException, FailoverException, ConnectException {
		failoverLock.lock();
		try {
			if (!isOpened) open();
			build(target, destName);
		} catch (BuildException e) {
			try {
				reconnected.await(5, TimeUnit.SECONDS);
				try {
					build(target, destName);
				} catch (BuildException ex) {
					removeMessenger(target);
					throw ex;
				}
			} catch (InterruptedException ex) {
				log.info("Should never happened.", ex);
			}
		} finally {
			failoverLock.unlock();
		}
	}
	
	public void removeMessenger(Messenger target) {
		if (target == null) return;
		failoverLock.lock();
		try {
			messengerSet.remove(target);
			if (messengerSet.size() == 0 && isOpened) {
				close();
			}
		} finally {
			failoverLock.unlock();
		}
	}
	
	private void open() throws FailoverException, ConnectException {
		try {
			connect(HostManager.getFristHost(groupName));
		} catch (ConnectException e) {
			if (isHA) {
				failover(e);
			} else {
				throw e;
			}
		}
	}
	
	private void connect(String url) throws ConnectException, FailoverException {
		try {
			log.info("Connect start, host [" + url + "]");
			
			log.info("Group Name:        [" + groupName + "]");
			log.info("High Availability: [" + isHA + "]");
			log.info("User:              [" + user + "]");
			log.info("Password:          [" + password + "]");
			log.info("Client ID:         [" + clientID + "]");
			
			currentHost = url;
			
			Hashtable env = new Hashtable();
			env.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.put("java.naming.provider.url", url);
			env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			context = new InitialContext(env);
			
			ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
			Connection conn = cf.createConnection(user, password);
			if (clientID != null) conn.setClientID(clientID);
			
			if (isHA)
				conn.setExceptionListener(new ExceptionListener() {
					public void onException(JMSException ex) {
						try {
							failover(new ConnectException("JMSException occurred on exception listener", ex, currentHost));
						} catch (FailoverException e) {
							log.error("Failover failed, this is a fatal error", e);
						}
					}
				});
			
			conn.start();
			connection = conn;
			
			isOpened = true;
			
			log.info("Connect end, host [" + url + "]");
		} catch (NamingException e) {
			throw new ConnectException("NamingException occurred when connect", e, currentHost);
		} catch (InvalidClientIDException e) {
			throw new FailoverException("Service [" + clientID + "] is already started", e);
		} catch (JMSException e) {
			throw new ConnectException("JMSException occurred when connect", e, currentHost);
		}
	}
	
	private void failover(ConnectException ex) throws FailoverException {
		failoverLock.lock();
		String failHost = ex.getFailHost();
		log.warn("Exception occurred on " + failHost + " try another one.", ex);
		try {
			log.info("Failover start");
			
			if (!failHost.equals(currentHost)) {
				log.info("Failing host [" + failHost + "] isn't the current host ["+ currentHost + "], so quit the failover operation.");
				return;
			}
			
			if (failHosts.contains(failHost)) {
				throw new FailoverException("### No working host available ###", ex);
			}
			
			failHosts.add(failHost);
			
			if (isOpened) close();
			
			connect(HostManager.getNextHost(groupName));
			
			failHosts.clear();
			
			for (Iterator iter = messengerSet.iterator(); iter.hasNext();) {
				Runnable element = (Runnable) iter.next();
				exeServ.submit(element);
			}
			
			reconnected.signalAll();
			
			log.info("Failover end");
		} catch (ConnectException e) {
			failover(e);
		} finally {
			failoverLock.unlock();
		}
	}
	
	private void build(Messenger target, String destName) throws BuildException {
		try {
			Destination dest = (Destination) context.lookup(destName);
			target.build(connection, dest);
			messengerSet.add(target);
		} catch (NamingException e) {
			throw new BuildException("NamingException occurred when build", e);
		} catch (JMSException e) {
			throw new BuildException("JMSException occurred when build", e);
		}
	}
	
	private void close() {
		log.info("Close start, host [" + currentHost + "]");
		try {
			context.close();
		} catch (NamingException e) {
		}
		try {
			connection.stop();
		} catch (JMSException e) {
		}
		try {
			connection.close();
		} catch (JMSException e) {
		}
		isOpened = false;
		log.info("Close end, host [" + currentHost + "]");
	}
}
