package test.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.cluster.ClusterTask;
import core.cluster.ExecuteMode;
import core.cluster.MessageProcessor;

import test.cluster.tasks.TestTaskBean;

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
