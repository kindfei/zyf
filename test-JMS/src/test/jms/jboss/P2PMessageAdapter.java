package test.jms.jboss;

import java.util.HashMap;

import javax.jms.Message;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

public class P2PMessageAdapter extends MessageAdapter {
	private BlockingQueue msgQueue;
	
	public P2PMessageAdapter(int capacity) throws IllegalAccessException {
		super(capacity, new HashMap());
	}

	public void onMessage(Message msg) {
		try {
			addMessage(msgQueue, msg);
		} catch (InterruptedException e) {
		}
	}
	
	protected BlockingQueue getMsgQueue() {
		if (msgQueue == null) {
			msgQueue = createMsgQueue();
		}
		return msgQueue;
	}
}
