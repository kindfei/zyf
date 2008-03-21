package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class DurableTopicRecvMessenger extends Messenger {
	
	private static final boolean transacted = false;
	private static final int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	private static final boolean noLocal = false;
	
	private TopicSession session;
	private TopicSubscriber subscriber;
	
	private MessageListener listener;
	private String name;
	private String messageSelector;

	public DurableTopicRecvMessenger(String destName, MessageListener listener, String name) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, listener, name, null);
	}

	public DurableTopicRecvMessenger(String destName, MessageListener listener, String name, String messageSelector) 
			throws BuildException, FailoverException, ConnectException {
		super(destName);
		this.listener = listener;
		this.name = name;
		this.messageSelector = messageSelector;
		executeBuild();
	}
	
	protected void build(Connection conn, Destination dest) throws JMSException {
		TopicConnection tConn = (TopicConnection) conn;
		session = tConn.createTopicSession(transacted, acknowledgeMode);
		subscriber = session.createDurableSubscriber((Topic)dest, name, messageSelector, noLocal);
		subscriber.setMessageListener(listener);
	}
	
	public void unsubscribe() throws JMSException {
		session.unsubscribe(name);
	}

	protected void innerClose() {
		try {
			subscriber.setMessageListener(null);
		} catch (JMSException e) {
		}
		try {
			subscriber.close();
		} catch (JMSException e) {
		}
		try {
			session.close();
		} catch (JMSException e) {
		}
	}

}
