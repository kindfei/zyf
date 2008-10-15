package test.jms.jboss.core;

import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageReceiver {
	public static final int DELIVERY_TYPE_NORMAL = 0;
	public static final int DELIVERY_TYPE_P2P = 1;
	public static final int DELIVERY_TYPE_PUB = 2;

	private static final Log log = LogFactory.getLog(MessageReceiver.class);
	
	private RecvMessenger receiver;
	private String destName;
	private int type;
	private String selector;
	private boolean isDaemon;
	
	public MessageReceiver(String destName) {
		this(destName, DELIVERY_TYPE_NORMAL);
	}
	
	public MessageReceiver(String destName, int deliveryType) {
		this(destName, deliveryType, null);
	}
	
	public MessageReceiver(String destName, int deliveryType, String selector) {
		this(destName, deliveryType, null, false);
	}
	
	public MessageReceiver(String destName, int deliveryType, String selector, boolean isDaemon) {
		this.destName = destName;
		this.type = deliveryType;
		this.selector = selector;
		this.isDaemon = isDaemon;
	}
	
	public void setListener(MessageListener msgListener) 
			throws BuildException, FailoverException, ConnectException {
		if (receiver == null) {
			MessageListener listener = null;
			
			if (type == DELIVERY_TYPE_NORMAL) {
				listener = msgListener;
			} else if (type == DELIVERY_TYPE_P2P) {
				MessageAdapter adapter = new P2PMessageAdapter(0, isDaemon);
				adapter.addListener(msgListener);
				listener = adapter;
			} else if (type == DELIVERY_TYPE_PUB) {
				MessageAdapter adapter = new PubMessageAdapter(0, isDaemon);
				adapter.addListener(msgListener);
				listener = adapter;
			} else {
				log.error("Unknown delivery type: " + type);
				return;
			}
			
			receiver = new RecvMessenger(destName, listener, selector, isDaemon);
			log.info("Create RecvMessenger. destName:" + destName + " selector:" + selector + " isDaemon:" + isDaemon);
		} else {
			if (type == DELIVERY_TYPE_NORMAL) {
				log.error("Normal delivery type, and listener is already setted.");
				return;
			}
			
			MessageAdapter adapter = (MessageAdapter) receiver.getListener();
			adapter.addListener(msgListener);
			log.info("Add listener to MessageAdapter. destName:" + destName + " selector:" + selector + " isDaemon:" + isDaemon);
		}
	}
	
	public void removeListener(MessageListener msgListener) {
		if (receiver == null) {
			log.error("There is no listener to remove.");
			return;
		}
		
		if (type == DELIVERY_TYPE_NORMAL) {
			close();
		} else {
			MessageAdapter adapter = (MessageAdapter) receiver.getListener();
			if (adapter.containsListener(msgListener)) {
				if (adapter.countListener() == 1) {
					close();
				} else {
					adapter.removeListener(msgListener);
				}
			}
		}
	}
	
	public void close() {
		if (receiver != null) {
			receiver.close();
			if (type != DELIVERY_TYPE_NORMAL) {
				MessageAdapter adapter = (MessageAdapter) receiver.getListener();
				adapter.removeAll();
			}
			receiver = null;
		}
	}
}
