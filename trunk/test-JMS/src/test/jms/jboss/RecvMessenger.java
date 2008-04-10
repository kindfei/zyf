package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class RecvMessenger extends Messenger {
	
	private MessageListener listener;
	private String selector;
	
	private boolean transacted;
	private int acknowledgeMode;
	
	private Session session;
	private MessageConsumer consumer;

	public RecvMessenger(String destName, MessageListener listener) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, listener, null);
	}

	public RecvMessenger(String destName, MessageListener listener, String selector) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, listener, selector, false, Session.AUTO_ACKNOWLEDGE);
	}

	public RecvMessenger(String destName, MessageListener listener, String selector
			, boolean transacted, int acknowledgeMode) 
			throws BuildException, FailoverException, ConnectException {
		
		super(destName);
		
		this.listener = listener;
		this.selector = selector;

		this.transacted = transacted;
		this.acknowledgeMode = acknowledgeMode;
		
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
