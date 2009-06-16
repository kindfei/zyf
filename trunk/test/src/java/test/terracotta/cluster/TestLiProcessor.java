package test.terracotta.cluster;

import java.util.ArrayList;
import java.util.List;


import test.terracotta.cluster.tasks.TestTaskBean;
import zyf.cluster.ClusterTask;
import zyf.cluster.ExecuteMode;
import zyf.cluster.TimerProcessor;

public class TestLiProcessor extends TimerProcessor {
	private int i;

	public List<ClusterTask> masterProcess() {
		
		ClusterTask task = new ClusterTask(ExecuteMode.LOCAL_INVOKE, new TestTaskBean(i++ + ""));
		
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
