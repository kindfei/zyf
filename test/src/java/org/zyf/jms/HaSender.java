package org.zyf.jms;

import java.io.Serializable;
import java.util.Map;

public class HaSender extends AbstractSender {
	
	HaSender(MessageDestination dest) throws MessageException {
		super(dest);
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
	public void send(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws MessageException {
		try {
			sendMessage(msg, deliveryMode, priority, timeToLive, props);
		} catch (Exception e) {
			if (isClosed) {
				throw new MessageException("MessageSender is closed.", e);
			}
			try {
				build();
				sendMessage(msg, deliveryMode, priority, timeToLive, props);
			} catch (Exception ex) {
				throw new MessageException("Exception occurred when send message.", ex);
			}
		}
	}
}
