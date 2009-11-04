package test.terracotta.cluster;

import incubation.cluster.ClusterTask;
import incubation.cluster.ExecuteMode;
import incubation.cluster.TimerProcessor;

import java.util.ArrayList;
import java.util.List;


import test.terracotta.cluster.tasks.TestTaskBean;

public class TestDmiProcessor extends TimerProcessor {
	private int i;

	public List<ClusterTask> masterProcess() {
		
		ClusterTask task = new ClusterTask(ExecuteMode.ALL_INVOKE, new TestTaskBean(i++ + ""));
		
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
