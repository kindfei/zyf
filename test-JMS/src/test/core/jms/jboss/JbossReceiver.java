package test.core.jms.jboss;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.NamingException;

import test.core.jms.MessageCallback;
import test.core.jms.MessageDestination;
import test.core.jms.MessageException;

public class JbossReceiver {
	
	private Lock lock = new ReentrantLock();
	
	private MessageDestination dest;
	private String messageSelector;
	
	private JbossConnection connection;
	
	private Session session;
	private MessageConsumer consumer;
	
	private MsgHandler handler;
	
	private volatile boolean isClosed;
	
	public JbossReceiver(MessageDestination dest) {
		this(dest, null);
	}
	
	public JbossReceiver(MessageDestination dest, String messageSelector) {
		this.dest = dest;
		this.messageSelector = messageSelector;

		connection = ConnectionFactory.getConnection();
		
		init();
	}
	
	private void init () {
		try {
			build();
		} catch (Exception e) {
			init ();
		}
	}
	
	private void build() throws JMSException, NamingException {
		session = connection.createSession();
		Destination destination = connection.createDestination(dest);
		consumer = session.createConsumer(destination, messageSelector);
	}
	
	public Serializable receive() throws MessageException {
		try {
			ObjectMessage msg = (ObjectMessage) consumer.receive();
			return msg.getObject();
		} catch (JMSException e) {
			if (isClosed) {
				throw new MessageException("MessageReceiver is closed.", e);
			}
			try {
				build();
				ObjectMessage msg = (ObjectMessage) consumer.receive();
				return msg.getObject();
			} catch (Exception e1) {
				throw new MessageException("Exception occurred when receive message.", e1);
			}
		}
	}
	
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
}
