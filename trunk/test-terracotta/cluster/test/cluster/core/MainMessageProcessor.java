package test.cluster.core;

import java.util.List;

import javax.jms.Message;

import test.cluster.core.tc.Task;

public abstract class MainMessageProcessor extends MessageProcessor {

	public List<Task> masterProcess(Message msg) {
		mainProcess(msg);
		return null;
	}
	
	public abstract void mainProcess(Message msg);

	public void workerProcess(Task task) {
	}

}
