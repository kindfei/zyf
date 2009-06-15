package test.jms.activemq;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Connector {
	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private int counter;
	
	public Connector(String url) {
		connectionFactory = new ActiveMQConnectionFactory(url);
	}
	
	public synchronized void start() throws JMSException {
		if (connection == null) {
			connection = connectionFactory.createConnection();
			connection.start();
		}
		counter++;
	}
	
	public synchronized void close() throws JMSException {
		counter--;
		if (counter == 0) {
			connection.close();
			connection = null;
		}
	}
	
	public synchronized Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		return connection.createSession(transacted, acknowledgeMode);
	}
}
