package test.cluster;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.MessageProcessor;
import fx.cluster.core.ClusterTask;

import test.cluster.tasks.TestTaskBean;

public class TestMessageProcessor extends MessageProcessor {

	public List<ClusterTask> masterProcess(Message msg) {
		List<ClusterTask> list = null;
		
		try {
			Object obj = ((ObjectMessage) msg).getObject();
			
			ClusterTask task = new ClusterTask(ExecuteMode.TASK_QUEUE, obj);
			
			list = new ArrayList<ClusterTask>();
			list.add(task);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public void workerProcess(ClusterTask task) {
		TestTaskBean bean = (TestTaskBean) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}

}
