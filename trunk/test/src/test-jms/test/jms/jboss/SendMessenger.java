package test.jms.jboss;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

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

	public SendMessenger(String destName, Map props) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, props, false, Session.AUTO_ACKNOWLEDGE);
	}

	public SendMessenger(String destName, Map props, boolean transacted, int acknowledgeMode) 
			throws BuildException, FailoverException, ConnectException {
		
		super(destName);

		this.transacted = transacted;
		this.acknowledgeMode = acknowledgeMode;
		
		if (props == null) props = new HashMap();
		this.properties = props;
		
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
	
	public Object removeProperty(String key) {
		return properties.remove(key);
	}
	
	public void clearProperty() {
		properties.clear();
	}

	public void send(String text) throws JMSException {
		sendMsg(text, null);
	}

	public void send(String text, Map props) throws JMSException {
		sendMsg(text, props);
	}
	
	public void send(Serializable obj) throws JMSException {
		sendMsg(obj, null);
	}
	
	public void send(Serializable obj, Map props) throws JMSException {
		sendMsg(obj, props);
	}
	
	public void commit() throws JMSException {
		session.commit();
	}
	
	public void rollback() throws JMSException {
		session.rollback();
	}
	
	private void sendMsg(Object obj, Map props) throws JMSException {
		rebuildLock.lock();
		try {
			Message msg = createMsg(obj, props);
			producer.send(msg);
		} catch (JMSException e) {
			try {
				rebuilt.await(10, TimeUnit.SECONDS);
				Message msg = createMsg(obj, props);
				producer.send(msg);
			} catch (InterruptedException ex) {
			}
		} finally {
			rebuildLock.unlock();
		}
	}
	
	private Message createMsg(Object obj, Map props) throws JMSException {
		Message msg = null;
		
		if (obj instanceof String) {
			msg = session.createTextMessage((String)obj);
		} else {
			msg = session.createObjectMessage((Serializable)obj);
		}
		
		copyProperty(msg, properties);
		copyProperty(msg, props);
		
		return msg;
	}
	
	private void copyProperty(Message msg, Map props) throws JMSException {
		if (props != null && props.size() != 0) {
			for (Iterator iter = props.entrySet().iterator(); iter.hasNext();) {
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
