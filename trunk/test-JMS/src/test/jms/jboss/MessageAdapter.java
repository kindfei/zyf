package test.jms.jboss;

import java.util.Iterator;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.ArrayBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;

public abstract class MessageAdapter implements MessageListener {
	private static final Log log = LogFactory.getLog(P2PMessageAdapter.class);
	
	protected int capacity;
	protected int countLimit;
	protected Map listenerMap;
	
	public MessageAdapter(int capacity, int countLimit, Map listenerMap) throws IllegalAccessException {
		if (capacity <= 0 && countLimit <= 0) throw new IllegalAccessException();
		this.capacity = capacity;
		this.countLimit = countLimit;
		this.listenerMap = listenerMap;
	}
	
	public void addListener(MessageListener listener) {
		if (listenerMap.containsKey(listener)) {
			log.info("Listener:" + listener + " already added.");
			return;
		}
		MessageDispatcher dispatcher = new MessageDispatcher(getMsgQueue(), listener);
		listenerMap.put(listener, dispatcher);
	}
	
	protected abstract BlockingQueue getMsgQueue();
	
	protected BlockingQueue createMsgQueue() {
		BlockingQueue msgQueue = null;
		if (capacity > 0) {
			msgQueue = new ArrayBlockingQueue(capacity);
		} else {
			msgQueue = new LinkedBlockingQueue();
		}
		return msgQueue;
	}
	
	public void removeListener(MessageListener listener) {
		MessageDispatcher dispatcher = (MessageDispatcher) listenerMap.get(listener);
		if (dispatcher == null) {
			log.info("Listener:" + listener + " is not exist.");
			return;
		}
		dispatcher.stop();
		listenerMap.remove(listener);
	}
	
	public void removeAll() {
		for (Iterator iter = listenerMap.values().iterator(); iter.hasNext();) {
			MessageDispatcher dispatcher = (MessageDispatcher) iter.next();
			dispatcher.stop();
		}
		listenerMap.clear();
	}

	public int countListener() {
		return listenerMap.size();
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getCountLimit() {
		return countLimit;
	}
	
	public MessageDispatcher getDispatcher(MessageListener listener) {
		return (MessageDispatcher) listenerMap.get(listener);
	}
	
	protected void addMessage(BlockingQueue msgQueue, Message msg) throws InterruptedException {
		int size = msgQueue.size();
		if (countLimit > 0 && size > countLimit) {
			msgQueue.clear();
			log.warn("msgQueue size:" + size + " > countLimit:" + countLimit + " so clear the queue.");
		}
		msgQueue.put(msg);
	}
}
