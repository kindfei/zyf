package test.terracotta.cluster;

import incubation.cluster.ClusterTask;
import incubation.cluster.ExecuteMode;
import incubation.cluster.MessageProcessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import test.terracotta.cluster.tasks.TestTaskBean;

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
