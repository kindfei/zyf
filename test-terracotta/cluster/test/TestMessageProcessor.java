package test;

import java.util.List;

import javax.jms.Message;

import test.cluster.core.MessageProcessor;
import test.cluster.core.tc.Task;

public class TestMessageProcessor extends MessageProcessor {

	@Override
	public List<Task> masterProcess(Message t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void workerProcess(Task task) {
		// TODO Auto-generated method stub

	}

}
