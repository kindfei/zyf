package test.core.jms;

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
		session = connection.createSession();
		Destination destination = connection.createDestination(dest);
		consumer = session.createConsumer(destination, messageSelector);
	}
	
	public Serializable receiveMessage() throws JMSException {
		return ((ObjectMessage) consumer.receive()).getObject();
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
				
				listener.onMessage(msg);
			}
		}
	}

	@Override
	public abstract Serializable receive() throws MessageException;

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
