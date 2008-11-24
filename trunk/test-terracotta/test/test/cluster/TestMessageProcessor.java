package test.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import test.cluster.tasks.TestTaskBean;
import test.core.cluster.ClusterTask;
import test.core.cluster.ExecuteMode;
import test.core.cluster.MessageProcessor;

public class TestMessageProcessor extends MessageProcessor {

	public List<ClusterTask> masterProcess(Serializable msg) {
		ClusterTask task = new ClusterTask(ExecuteMode.TASK_QUEUE, msg);
		List<ClusterTask> list = new ArrayList<ClusterTask>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(ClusterTask task) {
		TestTaskBean bean = (TestTaskBean) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}

}
