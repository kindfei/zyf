package test.cluster.core;

import javax.jms.Message;

public class MessageDrivenService extends AbstractService<Message> {
	
	private String destName;

	public MessageDrivenService(int mode, MessageProcessor processor, String destName) {
		super(mode, processor);
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
