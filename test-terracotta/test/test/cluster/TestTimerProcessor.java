package test.cluster;

import java.util.ArrayList;
import java.util.List;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.Task;
import fx.cluster.core.TimerProcessor;

import test.cluster.tasks.TestTaskBean;

public class TestTimerProcessor extends TimerProcessor {
	private int i;

	public List<Task> masterProcess() {
		
		Task task = new Task(ExecuteMode.TASK_QUEUE, new TestTaskBean(i++ + ""));
		
		List<Task> list = new ArrayList<Task>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTaskBean bean = (TestTaskBean) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}
	
}