package test.jms.jboss;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

public class PubMessageAdapter extends MessageAdapter {
	
	public PubMessageAdapter(int capacity, boolean isDaemon) {
		super(capacity, new ConcurrentHashMap(), isDaemon);
	}

	public void onMessage(Message msg) {
		try {
			for (Iterator iter = listenerMap.values().iterator(); iter.hasNext();) {
				MessageDispatcher dispatcher = (MessageDispatcher) iter.next();
				BlockingQueue msgQueue = dispatcher.getMsgQueue();
				addMessage(msgQueue, msg);
			}
		} catch (InterruptedException e) {
		}
	}
	
	protected BlockingQueue getMsgQueue() {
		return createMsgQueue();
	}
	
}
