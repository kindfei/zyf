package test;

import java.util.ArrayList;
import java.util.List;

import test.cluster.core.ExecuteMode;
import test.cluster.core.TimerProcessor;
import test.cluster.core.tc.Task;
import test.cluster.tc.tasks.TestTask;

public class TestGroupTimerProcessor extends TimerProcessor {
	private int i;

	public List<Task> masterProcess() {
		int counter = i++;
		
		Task task = new Task(ExecuteMode.TASK_QUEUE, new TestTask(counter + ""));
		task.setGroupId(counter % 2 + "");
		
		List<Task> list = new ArrayList<Task>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}

}
