package test.cluster;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.MessageProcessor;
import fx.cluster.core.Task;

import test.cluster.tasks.TestTask;

public class TestMessageProcessor extends MessageProcessor {

	public List<Task> masterProcess(Message msg) {
		List<Task> list = null;
		
		try {
			Object obj = ((ObjectMessage) msg).getObject();
			
			Task task = new Task(ExecuteMode.TASK_QUEUE, obj);
			
			list = new ArrayList<Task>();
			list.add(task);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}

}
