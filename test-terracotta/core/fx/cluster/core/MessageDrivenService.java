package fx.cluster.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MessageDrivenService
 * @author zhangyf
 *
 */
public class MessageDrivenService extends AbstractService<Message> {
	
	private static final Log log = LogFactory.getLog(MessageDrivenService.class);
	
	private String destName;
	private int receiverSize;
	private boolean receiverExecute;
	
	private test.jms.activemq.core.Receiver recv;
	private List<MessageReceiver> receiverList = new ArrayList<MessageReceiver>();
	
	private ExecutorService threadPool;

	MessageDrivenService(ServiceMode serviceMode, int takerSize, boolean takerExecute, boolean fairTake
			, MessageProcessor processor, String destName, int receiverSize, boolean receiverExecute) {
		super(serviceMode, takerSize, takerExecute, fairTake, processor);
		this.destName = destName;
		this.receiverSize = receiverSize;
		this.receiverExecute = receiverExecute;
	}

	protected void init() throws JMSException {
		recv = new test.jms.activemq.core.Receiver(destName);
		
		MessageReceiver receiver = null;
		for (int i = 0; i < receiverSize; i++) {
			receiver = new MessageReceiver();
			receiver.setName(destName + "-MessageReceiver" + i);
			receiverList.add(receiver);
			receiver.start();
		}
	}

	protected void close() {
		for (MessageReceiver receiver : receiverList) {
			receiver.end();
		}
		try {
			recv.close();
		} catch (JMSException e) {
			log.error("Close message receiver error.", e);
		}
		
		if (threadPool != null) {
			threadPool.shutdown();
		}
	}
	
	private class MessageReceiver extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while(isActive) {
					Message msg = recv.receive();
					if (msg != null) {
						if (receiverExecute) {
							process(msg);
						} else {
							execute(msg);
						}
					}
				}
			} catch (JMSException e) {
				log.error("Receive message error.", e);
			}
		}
		
		public void end() {
			isActive = false;
		}
	}
	
	private synchronized void execute(final Message msg) {
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
