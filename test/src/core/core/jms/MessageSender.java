package core.jms;

import java.io.Serializable;
import java.util.Map;

public interface MessageSender {
	public void send(Serializable msg) throws MessageException;
	public void send(Serializable msg, Map<String, Object> props) throws MessageException;
	public void send(Serializable msg, int deliveryMode, int priority, long timeToLive, Map<String, Object> props) throws MessageException;
	public void close();
}
