package test.core.jms.jboss;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.NamingException;

import test.core.jms.MessageDestination;
import test.core.jms.Provider;
import test.core.jms.Rebuildable;
import test.core.jms.Receiver;

public class JbossReceiver implements Receiver, Rebuildable {
	
	private Provider provider;
	private MessageDestination dest;
	private String selector;
	
	private Session session;
	private MessageConsumer consumer;
	private MessageListener listener;
	
	public JbossReceiver(Provider provider, MessageDestination dest, String selector) {
		this.provider = provider;
		this.dest = dest;
		this.selector = selector;
	}
	
	private synchronized void build() throws JMSException, NamingException {
		session = provider.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = provider.createDestination(dest);
		consumer = session.createConsumer(destination, selector);
	}

	@Override
	public synchronized void rebuild() throws JMSException, NamingException {
		if (session != null) {
			try {
				session.close();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
		if (consumer != null) {
			try {
				consumer.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		build();
		
		if (listener != null) {
			consumer.setMessageListener(listener);
		}
	}

	@Override
	public synchronized Serializable receive() throws JMSException {
		ObjectMessage msg = (ObjectMessage) consumer.receive();
		return msg.getObject();
	}
	
	public synchronized void setMessageListener(MessageListener listener) throws JMSException {
		consumer.setMessageListener(listener);
		this.listener = listener;
	}
}
