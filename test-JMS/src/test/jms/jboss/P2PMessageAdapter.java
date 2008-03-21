package test.jms.jboss;

import java.util.Hashtable;

import javax.jms.Message;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

public class P2PMessageAdapter extends MessageAdapter {
	private BlockingQueue msgQueue;
	
	public P2PMessageAdapter(int capacity, int countLimit) throws IllegalAccessException {
		super(capacity, countLimit, new Hashtable());
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

	public int countMessage() {
		return msgQueue.size();
	}

	public void clearMessage() {
		msgQueue.clear();
	}
}
