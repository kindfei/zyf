package test.core.jms;

import java.io.Serializable;

public interface MessageReceiver {
	public Serializable receive() throws MessageException;
	public void setMessageListener(MessageCallback listener, boolean isDeamon);
	public void close();
}
