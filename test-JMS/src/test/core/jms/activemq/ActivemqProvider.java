package test.core.jms.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;

import test.core.jms.MessageDestination;
import test.core.jms.Provider;

public class ActivemqProvider implements Provider {

	@Override
	public Destination createDestination(MessageDestination dest) throws NamingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
