package zyf.jms;

import javax.jms.Destination;
import javax.jms.Session;

public interface MessageConnection {
	public Session createSession(boolean transacted, int acknowledgeMode) throws MessageException;
	public Destination createDestination(MessageDestination dest) throws MessageException;
}
