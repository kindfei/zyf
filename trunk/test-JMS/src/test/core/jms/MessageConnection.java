package test.core.jms;

import javax.jms.Destination;
import javax.jms.Session;

public interface MessageConnection {
	public Session createSession() throws MessageException;
	public Destination createDestination(MessageDestination dest) throws MessageException;
}
