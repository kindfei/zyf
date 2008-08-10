package test.cluster.core;

import javax.jms.Message;

public class MessageDrivenService extends AbstractService<Message> {
	
	private String destName;

	public MessageDrivenService(int serviceMode, int executorSize, boolean acceptTask, MessageProcessor processor, String destName) {
		super(serviceMode, executorSize, acceptTask, processor);
		this.destName = destName;
	}
	
	public String getDestName() {
		return destName;
	}

	public void init() {
		
	}

	public void close() {
	}
}
