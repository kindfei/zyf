package test;

import java.util.ArrayList;
import java.util.List;

import test.cluster.core.ExecuteMode;
import test.cluster.core.Task;
import test.cluster.core.TimerProcessor;
import test.cluster.tc.tasks.TestTask;

public class TestTimerProcessor extends TimerProcessor {
	private int i;

	public List<Task> masterProcess() {
		
		Task task = new Task(ExecuteMode.TASK_QUEUE, new TestTask(i++ + ""));
		
		List<Task> list = new ArrayList<Task>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}
	
}