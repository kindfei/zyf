package test.core.jms;

import java.io.Serializable;

public interface MessageCallback {
	public void onMessage(Serializable msg);
}
