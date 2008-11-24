package test.core.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.core.jms.MessageDestination;
import test.core.jms.MessageException;
import test.core.jms.MessageFactory;

/**
 * MessageDrivenService
 * @author zhangyf
 *
 */
public class MessageDrivenService extends AbstractService<Serializable> {
	
	private static final Log log = LogFactory.getLog(MessageDrivenService.class);
	
	private MessageDestination dest;
	private String selector;
	private int receiverSize;
	private boolean receiverExecute;
	
	private test.core.jms.MessageReceiver recv;
	private List<MessageReceiver> receiverList = new ArrayList<MessageReceiver>();
	
	private ExecutorService threadPool;

	MessageDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute, boolean fairTake
			, MessageProcessor processor, MessageDestination dest, String selector, int receiverSize, boolean receiverExecute) {
		super(serviceMode, takerSize, takerExecute, fairTake, processor);
		this.dest = dest;
		this.selector = selector;
		this.receiverSize = receiverSize;
		this.receiverExecute = receiverExecute;
	}

	protected void init() throws MessageException {
		recv = MessageFactory.createReceiver(dest, selector);
		
		MessageReceiver receiver = null;
		for (int i = 0; i < receiverSize; i++) {
			receiver = new MessageReceiver();
			receiver.setName(dest + "-MessageReceiver" + i);
			receiverList.add(receiver);
			receiver.start();
		}
	}

	protected void close() {
		for (MessageReceiver receiver : receiverList) {
			receiver.end();
		}
		
		recv.close();
		
		if (threadPool != null) {
			threadPool.shutdown();
		}
	}
	
	private class MessageReceiver extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while(isActive) {
					Serializable msg = recv.receive();
					if (msg != null) {
						if (receiverExecute) {
							process(msg);
						} else {
							execute(msg);
						}
					}
				}
			} catch (MessageException e) {
				log.error("Receive message error.", e);
			}
		}
		
		public void end() {
			isActive = false;
		}
	}
	
	private synchronized void execute(final Serializable msg) {
		if (threadPool == null) {
			threadPool = new ThreadPoolExecutor(0
					, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS
					, new SynchronousQueue<Runnable>());
		}
		
		threadPool.execute(new Runnable() {
			public void run() {
				process(msg);
			}
		});
	}
}
