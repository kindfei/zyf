package test.cluster.core;

import java.util.List;

import javax.jms.Message;

/**
 * MessageDrivenService
 * @author zhangyf
 *
 */
public class MessageDrivenService extends AbstractService<Message> {
	
	private String destName;
	private int receiverSize;
	private List<Receiver> receiverList;

	public MessageDrivenService(ServiceMode serviceMode, int executorSize, MessageProcessor processor, String destName, int receiverSize) {
		super(serviceMode, executorSize, processor);
		this.destName = destName;
		this.receiverSize = receiverSize;
	}
	
	public String getDestName() {
		return destName;
	}

	public void init() {
		Receiver receiver = null;
		for (int i = 0; i < receiverSize; i++) {
			receiver = new Receiver();
			receiverList.add(receiver);
			receiver.start();
		}
	}

	public void close() {
		for (Receiver receiver : receiverList) {
			receiver.end();
		}
	}
	
	class Receiver extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			while(isActive) {
				process(null);
			}
		}
		
		public void end() {
			isActive = false;
		}
	}
}
