package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class RecvMessenger extends DefaultMessenger {
	
	private MessageListener listener;
	private String selector;
	private boolean isDaemon;
	
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
		this(destName, listener, selector, false);
	}

	public RecvMessenger(String destName, MessageListener listener, String selector, boolean isDaemon) 
			throws BuildException, FailoverException, ConnectException {
		this(destName, listener, selector, isDaemon, false, Session.AUTO_ACKNOWLEDGE);
	}

	public RecvMessenger(String destName, MessageListener listener, String selector
			, boolean isDaemon, boolean transacted, int acknowledgeMode) 
			throws BuildException, FailoverException, ConnectException {
		
		super(destName);
		
		this.listener = listener;
		this.selector = selector;
		this.isDaemon = isDaemon;

		this.transacted = transacted;
		this.acknowledgeMode = acknowledgeMode;
		
		executeBuild();
	}

	protected void build(Connection conn, Destination dest) throws JMSException {
		session = conn.createSession(transacted, acknowledgeMode);
		consumer = session.createConsumer(dest, selector);
		
		if (isDaemon) {
			Dispatcher.createDispatcher(consumer, listener);
		} else {
			consumer.setMessageListener(listener);
		}
	}
	
	private static class Dispatcher extends Thread {
		private MessageConsumer consumer;
		private MessageListener listener;
		
		private static void createDispatcher(MessageConsumer consumer, MessageListener listener) {
			Dispatcher inst = new Dispatcher(consumer, listener);
			inst.start();
		}
		
		private Dispatcher(MessageConsumer consumer, MessageListener listener) {
			this.consumer = consumer;
			this.listener = listener;
			setDaemon(true);
		}
		
		public void run() {
			try {
				while (true) {
					Message msg = consumer.receive();
					listener.onMessage(msg);
				}
			} catch (JMSException e) {
			}
		}
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
