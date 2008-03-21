package test.jms.jboss;

import javax.jms.Message;
import javax.jms.MessageListener;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;

public class MessageDispatcher implements Runnable {
	private BlockingQueue msgQueue;
	private MessageListener listener;
	private volatile boolean isActive;
	private Thread thread;
	
	MessageDispatcher (BlockingQueue msgQueue, MessageListener listener) {
		this.msgQueue = msgQueue;
		this.listener = listener;
		isActive = true;
		thread = new Thread(this);
		thread.setDaemon(false);
		thread.start();
	}
	
	public BlockingQueue getMsgQueue() {
		return msgQueue;
	}
	
	public MessageListener getListener() {
		return listener;
	}

	public void run() {
		try {
			while (isActive) {
				Message msg = null;
				msg = (Message) msgQueue.take();
				listener.onMessage(msg);
			}
		} catch (InterruptedException e) {
		}
	}
	
	public void stop() {
		isActive = false;
		thread.interrupt();
	}

	public int countMessage() {
		return msgQueue.size();
	}

	public void clearMessage() {
		msgQueue.clear();
	}
}
