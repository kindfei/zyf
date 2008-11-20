package test.core.jms;

import java.io.Serializable;

public interface Sender {
	public void send(Serializable obj);
}
