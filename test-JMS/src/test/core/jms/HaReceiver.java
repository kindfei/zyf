package test.core.jms;

import java.io.Serializable;

import javax.jms.JMSException;

public class HaReceiver extends AbstractReceiver {
	
	HaReceiver(MessageDestination dest, String messageSelector) throws MessageException {
		super(dest, messageSelector);
	}

	@Override
	protected void init() throws MessageException {
		try {
			build();
		} catch (Exception e) {
			init ();
		}
	}

	@Override
	public Serializable receive() throws MessageException {
		try {
			return receiveMessage();
		} catch (JMSException e) {
			if (isClosed) {
				throw new MessageException("MessageReceiver is closed.", e);
			}
			try {
				build();
				return receiveMessage();
			} catch (JMSException je) {
				throw new MessageException("Exception occurred when receive message.", je);
			}
		}
	}
}
