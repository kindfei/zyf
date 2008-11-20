package test.core.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;

public interface Provider {
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException;
	public Destination createDestination(MessageDestination dest) throws NamingException;
}
