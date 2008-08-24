package test.jms.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class Receiver {
	private Connector connector;
	private Session session;
	private Destination dest;
	private MessageConsumer consumer;
	
	public Receiver(String destName, DestTypes type) throws JMSException {
		connector = ConnectorFactory.createConnection();
		session = connector.createSession(false, Session.AUTO_ACKNOWLEDGE);
		if (type == DestTypes.Queue) {
			dest = session.createQueue(destName);
		} else {
			dest = session.createTopic(destName);
		}
		consumer = session.createConsumer(dest);
	}
	
	public synchronized Message receive() throws JMSException {
		return consumer.receive();
	}
	
	public void setListener(MessageListener listener) throws JMSException {
		consumer.setMessageListener(listener);
	}
	
	public void close() throws JMSException {
		consumer.close();
		session.close();
		connector.close();
	}
}
