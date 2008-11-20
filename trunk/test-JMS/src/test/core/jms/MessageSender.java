package test.core.jms;

import java.io.Serializable;

public interface MessageSender {
	public void send(Serializable obj);
}
