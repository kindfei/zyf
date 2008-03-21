package test.jms.jboss;

import java.util.Iterator;

import javax.jms.Message;
import javax.jms.MessageListener;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class PubMessageAdapter extends MessageAdapter {
	public PubMessageAdapter(int capacity, int countLimit) throws IllegalAccessException {
		super(capacity, countLimit, new ConcurrentHashMap());
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
	
	public int countMessage(MessageListener listener) {
		MessageDispatcher dispatcher = (MessageDispatcher) listenerMap.get(listener);
		return dispatcher.getMsgQueue().size();
	}
	
	public void clearMessage(MessageListener listener) {
		MessageDispatcher dispatcher = (MessageDispatcher) listenerMap.get(listener);
		dispatcher.getMsgQueue().clear();
	}
}
