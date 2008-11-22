package test.core.jms;

import java.io.Serializable;
import java.util.Map;

import javax.jms.JMSException;

public class SimpleSender extends AbstractSender {

	SimpleSender(MessageDestination dest, String groupId) throws MessageException {
		super(dest, groupId);
	}

	@Override
	protected void init() throws MessageException {
		try {
			build();
		} catch (JMSException e) {
			throw new MessageException("Exception occurred when init.", e);
		}
	}

	@Override
	public void send(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws MessageException {
		try {
			sendMessage(msg, deliveryMode, priority, timeToLive, props);
		} catch (JMSException e) {
			throw new MessageException("Exception occurred when send message.", e);
		}
	}

}
