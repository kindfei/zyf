package test.jms.jboss;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;

public abstract class Messenger implements Runnable {
	
	protected abstract void build(Connection conn, Destination dest) throws JMSException;
	
}
