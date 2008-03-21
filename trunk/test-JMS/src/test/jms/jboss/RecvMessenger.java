package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class RecvMessenger extends Messenger {
	
	private static final boolean transacted = false;
	private static final int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	
	private Session session;
	private MessageConsumer consumer;
	private MessageListener listener;
	private String selector;

	public RecvMessenger(String destName, MessageListener listener) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, listener, null);
	}

	public RecvMessenger(String destName, MessageListener listener, String selector) 
			throws BuildException, FailoverException, ConnectException {
		super(destName);
		this.listener = listener;
		this.selector = selector;
		executeBuild();
	}

	protected void build(Connection conn, Destination dest) throws JMSException {
		session = conn.createSession(transacted, acknowledgeMode);
		consumer = session.createConsumer(dest, selector);
		consumer.setMessageListener(listener);
	}

	protected void innerClose() {
		try {
			consumer.setMessageListener(null);
		} catch (JMSException e) {
		}
		try {
			consumer.close();
		} catch (JMSException e) {
		}
		try {
			session.close();
		} catch (JMSException e) {
		}
	}

}
