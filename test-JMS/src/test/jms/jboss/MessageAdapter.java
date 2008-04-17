package test.jms.jboss;

import java.util.Iterator;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.SynchronousQueue;

public abstract class MessageAdapter implements MessageListener {
	private static final Log log = LogFactory.getLog(HostManager.class);
	
	private int capacity;
	private boolean isDaemon;
	
	protected Map listenerMap;
	
	public MessageAdapter(int capacity, Map listenerMap, boolean isDaemon) {
		this.capacity = capacity;
		this.listenerMap = listenerMap;
		this.isDaemon = isDaemon;
	}
	
	public void addListener(MessageListener listener) {
		if (listenerMap.containsKey(listener)) {
			log.info("Listener:" + listener + " already added.");
			return;
		}
		MessageDispatcher dispatcher = new MessageDispatcher(getMsgQueue(), listener, isDaemon);
		listenerMap.put(listener, dispatcher);
	}
	
	public void removeListener(MessageListener listener) {
		MessageDispatcher dispatcher = (MessageDispatcher) listenerMap.remove(listener);
		if (dispatcher == null) {
			log.info("Listener:" + listener + " is not exist.");
			return;
		}
		dispatcher.stop();
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
	
	public boolean containsListener(MessageListener listener) {
		return listenerMap.containsKey(listener);
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public MessageDispatcher getDispatcher(MessageListener listener) {
		return (MessageDispatcher) listenerMap.get(listener);
	}
	
	protected abstract BlockingQueue getMsgQueue();
	
	protected BlockingQueue createMsgQueue() {
		BlockingQueue msgQueue = null;
		if (capacity > 0) {
			msgQueue = new LinkedBlockingQueue();
		} else {
			msgQueue = new SynchronousQueue();
		}
		return msgQueue;
	}
	
	protected void addMessage(BlockingQueue msgQueue, Message msg) throws InterruptedException {
		if (capacity > 0) {
			int size = msgQueue.size();
			if (size > capacity) {
				msgQueue.clear();
				log.warn("msgQueue size:" + size + " > capacity:" + capacity + " so clear the queue.");
			}
		}
		msgQueue.put(msg);
	}
}
