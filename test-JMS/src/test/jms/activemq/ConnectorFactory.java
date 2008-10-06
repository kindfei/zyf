package test.jms.activemq;

import javax.jms.JMSException;

public class ConnectorFactory {
	
	private static Connector connector;
	
	public static synchronized Connector createConnection() throws JMSException {
		if (connector == null) {
			connector = new Connector("tcp://10.4.6.218:61616");
		}
		connector.start();
		return connector;
	}
	
}
