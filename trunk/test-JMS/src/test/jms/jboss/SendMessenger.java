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

public class SendMessenger extends Messenger {
	
	private static final int TYPE_TEXT = 1;
	private static final int TYPE_OBJECT = 2;
	private static final boolean transacted = false;
	private static final int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	
	private Session session;
	private MessageProducer producer;
	private Map prop;

	public SendMessenger(String destName) 
			throws BuildException, FailoverException, ConnectException {
		super(destName);
		executeBuild();
		this.prop = new HashMap();
	}

	protected void build(Connection conn, Destination dest) throws JMSException {
		session = conn.createSession(transacted, acknowledgeMode);
		producer = session.createProducer(dest);
	}
	
	public void setProperty(String key, String value) {
		prop.put(key, value);
	}
	
	public void removeProperty(String key) {
		prop.remove(key);
	}

	public void send(String text) throws JMSException {
		send(text, TYPE_TEXT);
	}
	
	public void send(Serializable obj) throws JMSException {
		send(obj, TYPE_OBJECT);
	}
	
	private void send(Object obj, int msgType) throws JMSException {
		rebuildLock.lock();
		try {
			Message msg = createMsg(obj, msgType);
			producer.send(msg);
		} catch (JMSException e) {
			try {
				rebuilt.await(10, TimeUnit.SECONDS);
				Message msg = createMsg(obj, msgType);
				producer.send(msg);
			} catch (InterruptedException ex) {
			}
		} finally {
			rebuildLock.lock();
		}
	}
	
	private Message createMsg(Object obj, int msgType) throws JMSException {
		Message msg = null;
		
		switch (msgType) {
		case TYPE_TEXT:
			msg = session.createTextMessage((String)obj);
			break;
		case TYPE_OBJECT:
			msg = session.createObjectMessage((Serializable)obj);
			break;
		default:
			return null;
		}
		
		if (prop.size() != 0) {
			for (Iterator iter = prop.entrySet().iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				msg.setStringProperty((String)entry.getKey(), (String)entry.getValue());
			}
		}
		
		return msg;
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
