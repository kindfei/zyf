package test.jms.activemq;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public class Sender {
	private Connector connector;
	private Session session;
	private Destination dest;
	private MessageProducer producer;
	
	public Sender(String destName) throws JMSException {
		connector = ConnectorFactory.createConnection();
		session = connector.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		String type = destName.split("/")[0];
		if (type.equals("queue")) {
			dest = session.createQueue(destName);
		} else if (type.equals("topic")) {
			dest = session.createTopic(destName);
		} else {
			throw new RuntimeException("Error destName format - " + destName);
		}
		
		producer = session.createProducer(dest);
	}
	
	public void send(Serializable obj) throws JMSException {
		ObjectMessage msg = session.createObjectMessage(obj);
		producer.send(msg);
	}
	
	public void close() throws JMSException {
		session.close();
		connector.close();
	}
}
