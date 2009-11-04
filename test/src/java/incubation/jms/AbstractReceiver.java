package incubation.jms;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public abstract class AbstractReceiver implements MessageReceiver {
	
	private Lock lock = new ReentrantLock();
	
	private MessageDestination dest;
	private String messageSelector;
	
	private MessageConnection connection;
	
	private Session session;
	private MessageConsumer consumer;
	
	private MsgHandler handler;
	
	protected volatile boolean isClosed;
	
	protected AbstractReceiver(MessageDestination dest, String messageSelector) throws MessageException {
		this.dest = dest;
		this.messageSelector = messageSelector;

		connection = ConnectionFactory.getConnection(dest.getProvider());
		
		init();
	}
	
	protected abstract void init() throws MessageException;
	
	protected void build() throws MessageException, JMSException {
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = connection.createDestination(dest);
		consumer = session.createConsumer(destination, messageSelector);
	}
	
	protected Serializable receiveMessage(long timeout) throws JMSException {
		return ((ObjectMessage) consumer.receive(timeout)).getObject();
	}
	
	private class MsgHandler extends Thread {
		
		private MessageCallback listener;
		
		MsgHandler(MessageCallback listener, boolean isDeamon) {
			this.listener = listener;
			this.setDaemon(isDeamon);
		}
		
		@Override
		public void run() {
			Serializable msg = null;
			while (!isClosed) {
				try {
					msg = receive();
				} catch (MessageException e) {
					continue;
				}
				
				if (msg != null) {
					listener.onMessage(msg);
				}
			}
		}
	}

	@Override
	public Serializable receive() throws MessageException {
		return receive(0);
	}
	
	@Override
	public abstract Serializable receive(long timeout) throws MessageException;

	@Override
	public void setMessageListener(MessageCallback listener, boolean isDeamon) {
		lock.lock();
		try {
			if (handler == null && listener != null) {
				handler = new MsgHandler(listener, isDeamon);
				handler.start();
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
		isClosed = true;
		
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
