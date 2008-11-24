package test.core.jms;

import java.io.Serializable;

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
	public Serializable receive(long timeout) throws MessageException {
		try {
			return receiveMessage(timeout);
		} catch (Exception e) {
			if (isClosed) {
				throw new MessageException("MessageReceiver is closed.", e);
			}
			try {
				build();
				return receiveMessage(timeout);
			} catch (Exception ex) {
				throw new MessageException("Exception occurred when receive message.", ex);
			}
		}
	}
}
