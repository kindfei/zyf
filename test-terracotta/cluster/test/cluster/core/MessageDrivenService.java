package test.cluster.core;

import javax.jms.Message;

/**
 * MessageDrivenService
 * @author zhangyf
 *
 */
public class MessageDrivenService extends AbstractService<Message> {
	
	private String destName;

	public MessageDrivenService(ServiceMode serviceMode, int executorSize, MessageProcessor processor, String destName) {
		super(serviceMode, executorSize, processor);
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
