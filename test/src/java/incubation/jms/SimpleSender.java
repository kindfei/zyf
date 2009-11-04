package incubation.jms;

import java.io.Serializable;
import java.util.Map;

public class SimpleSender extends AbstractSender {

	SimpleSender(MessageDestination dest) throws MessageException {
		super(dest);
	}

	@Override
	protected void init() throws MessageException {
		try {
			build();
		} catch (Exception e) {
			throw new MessageException("Exception occurred when init.", e);
		}
	}

	@Override
	public void send(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws MessageException {
		try {
			sendMessage(msg, deliveryMode, priority, timeToLive, props);
		} catch (Exception e) {
			throw new MessageException("Exception occurred when send message.", e);
		}
	}

}
