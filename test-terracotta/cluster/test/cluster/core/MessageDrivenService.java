package test.cluster.core;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import test.jms.activemq.DestTypes;

/**
 * MessageDrivenService
 * @author zhangyf
 *
 */
public class MessageDrivenService extends AbstractService<Message> {
	
	private test.jms.activemq.Receiver recv;
	private String destName;
	private int receiverSize;
	private List<Receiver> receiverList;

	public MessageDrivenService(ServiceMode serviceMode, int executorSize, boolean acceptTask, MessageProcessor processor, String destName, int receiverSize, boolean newReceiver) {
		super(serviceMode, executorSize, acceptTask, processor);
		this.destName = destName;
		this.receiverSize = receiverSize;
	}
	
	public String getDestName() {
		return destName;
	}

	public void init() throws JMSException {
		recv = new test.jms.activemq.Receiver(destName, DestTypes.Queue);
		
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
		try {
			recv.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	class Receiver extends Thread {
		private volatile boolean isActive = true;
		
		public void run() {
			try {
				while(isActive) {
					Message msg = (ObjectMessage) recv.receive();
					if (msg != null) process(msg);
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		public void end() {
			isActive = false;
		}
	}
}
