package test.cluster.core;

import java.util.List;

import javax.jms.Message;


/**
 * MainMessageProcessor
 * @author zhangyf
 *
 */
public abstract class MainMessageProcessor extends MessageProcessor {

	public final List<Task> masterProcess(Message msg) {
		mainProcess(msg);
		return null;
	}
	
	public abstract void mainProcess(Message msg);

	public final void workerProcess(Task task) {
	}

}
