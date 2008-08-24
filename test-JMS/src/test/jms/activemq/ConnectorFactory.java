package test.jms.activemq;

import javax.jms.JMSException;

public class ConnectorFactory {
	
	private static Connector connector;
	
	public static synchronized Connector createConnection() throws JMSException {
		if (connector == null) {
			connector = new Connector("vm://192.168.0.10");
		}
		connector.start();
		return connector;
	}
	
}
