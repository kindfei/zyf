package test.cluster;

import java.util.ArrayList;
import java.util.List;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.ClusterTask;
import fx.cluster.core.TimerProcessor;

import test.cluster.tasks.TestTaskBean;

public class TestTimerProcessor extends TimerProcessor {
	private int i;

	public List<ClusterTask> masterProcess() {
		
		ClusterTask task = new ClusterTask(ExecuteMode.TASK_QUEUE, new TestTaskBean(i++ + ""));
		
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