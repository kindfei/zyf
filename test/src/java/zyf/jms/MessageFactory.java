package zyf.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageFactory {
	private final static Log log = LogFactory.getLog(MessageFactory.class);
	
	public static MessageReceiver createReceiver(MessageDestination dest) throws MessageException {
		return createReceiver(dest, null);
	}
	
	public static MessageReceiver createReceiver(MessageDestination dest, String messageSelector) throws MessageException {
		if (dest.getProvider() == null) {
			throw new MessageException("Create message receiver error, no provider mapped to " + dest.toString());
		}
		
		MessageReceiver receiver = null;
		if (dest.getProvider().isManualHa()) {
			receiver = new HaReceiver(dest, messageSelector);
		} else {
			receiver = new SimpleReceiver(dest, messageSelector);
		}
		
		log.info("Successfully create message receiver. destName="
				+ dest.getDestName() + ", providerId="
				+ dest.getProvider().getProviderId());
		return receiver;
	}
	
	public static MessageSender createSender(MessageDestination dest) throws MessageException {
		if (dest.getProvider() == null) {
			throw new MessageException("Create message sender error, no provider mapped to " + dest.toString());
		}
		
		MessageSender sender = null;
		if (dest.getProvider().isManualHa()) {
			sender = new HaSender(dest);
		} else {
			sender = new SimpleSender(dest);
		}
		
		log.info("Successfully create message sender. destName="
				+ dest.getDestName() + ", providerId="
				+ dest.getProvider().getProviderId());
		return sender;
	}
}
