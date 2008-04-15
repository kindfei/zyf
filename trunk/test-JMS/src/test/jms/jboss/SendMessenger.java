package test.jms.jboss;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class SendMessenger extends DefaultMessenger {
	
	private boolean transacted;
	private int acknowledgeMode;
	
	private Session session;
	private MessageProducer producer;
	
	private Map properties;

	public SendMessenger(String destName) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, null);
	}

	public SendMessenger(String destName, Map prop) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, prop, false, Session.AUTO_ACKNOWLEDGE);
	}

	public SendMessenger(String destName, Map prop, boolean transacted, int acknowledgeMode) 
			throws BuildException, FailoverException, ConnectException {
		
		super(destName);

		this.transacted = transacted;
		this.acknowledgeMode = acknowledgeMode;
		
		if (prop == null) prop = new HashMap();
		this.properties = prop;
		
		executeBuild();
	}

	protected void build(Connection conn, Destination dest) throws JMSException {
		session = conn.createSession(transacted, acknowledgeMode);
		producer = session.createProducer(dest);
	}
	
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	public String getProperty(String key) {
		return (String) properties.get(key);
	}
	
	public void removeProperty(String key) {
		properties.remove(key);
	}
	
	public void clearProperty() {
		properties.clear();
	}

	public void send(String text) throws JMSException {
		sendMsg(text, null);
	}

	public void send(String text, Map prop) throws JMSException {
		sendMsg(text, prop);
	}
	
	public void send(Serializable obj) throws JMSException {
		sendMsg(obj, null);
	}
	
	public void send(Serializable obj, Map prop) throws JMSException {
		sendMsg(obj, prop);
	}
	
	private void sendMsg(Object obj, Map prop) throws JMSException {
		rebuildLock.lock();
		try {
			Message msg = createMsg(obj, prop);
			producer.send(msg);
		} catch (JMSException e) {
			try {
				rebuilt.await(10, TimeUnit.SECONDS);
				Message msg = createMsg(obj, prop);
				producer.send(msg);
			} catch (InterruptedException ex) {
			}
		} finally {
			rebuildLock.lock();
		}
	}
	
	private Message createMsg(Object obj, Map prop) throws JMSException {
		Message msg = null;
		
		if (obj instanceof String) {
			msg = session.createTextMessage((String)obj);
		} else {
			msg = session.createObjectMessage((Serializable)obj);
		}
		
		copyProperty(msg, properties);
		copyProperty(msg, prop);
		
		return msg;
	}
	
	private void copyProperty(Message msg, Map prop) throws JMSException {
		if (prop != null && prop.size() != 0) {
			for (Iterator iter = prop.entrySet().iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				msg.setStringProperty((String)entry.getKey(), (String)entry.getValue());
			}
		}
	}

	protected void innerClose() {
		try {
			producer.close();
		} catch (JMSException e) {
		}
		try {
			session.close();
		} catch (JMSException e) {
		}
	}

}
