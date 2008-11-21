package test.core.jms.jboss;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.NamingException;

import test.core.jms.MessageDestination;
import test.core.jms.MessageException;

public class JbossSender {

	private MessageDestination dest;
	
	private JbossConnection connection;
	
	private Session session;
	private MessageProducer producer;
	
	private volatile boolean isClosed;
	
	public JbossSender(MessageDestination dest) {
		this.dest = dest;
		
		connection = ConnectionFactory.getConnection();
		
		init();
	}
	
	private void init() {
		try {
			build();
		} catch (Exception e) {
			init ();
		}
	}
	
	private void build() throws JMSException, NamingException {
		session = connection.createSession();
		Destination destination = connection.createDestination(dest);
		producer = session.createProducer(destination);
	}
	
	public void send(Serializable msg) throws MessageException {
		try {
			ObjectMessage message = session.createObjectMessage(msg);
			producer.send(message);
		} catch (JMSException e) {
			if (isClosed) {
				throw new MessageException("MessageSender is closed.", e);
			}
			try {
				build();
				ObjectMessage message = session.createObjectMessage(msg);
				producer.send(message);
			} catch (Exception e1) {
				throw new MessageException("Exception occurred when receive message.", e1);
			}
		}
	}
	
	public void close() {
		isClosed = true;
		
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
