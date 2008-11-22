package test.core.jms;

import java.io.Serializable;

import javax.jms.JMSException;

public class SimpleReceiver extends AbstractReceiver {

	SimpleReceiver(MessageDestination dest, String messageSelector) throws MessageException {
		super(dest, messageSelector);
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
	public Serializable receive() throws MessageException {
		try {
			return receiveMessage();
		} catch (JMSException e) {
			throw new MessageException("Exception occurred when receive message.", e);
		}
	}

}
