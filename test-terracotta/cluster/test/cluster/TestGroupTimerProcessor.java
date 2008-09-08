package test.cluster;

import java.util.ArrayList;
import java.util.List;

import fx.cluster.core.Task;
import fx.cluster.core.TimerProcessor;

import test.cluster.tasks.TestTask;

public class TestGroupTimerProcessor extends TimerProcessor {
	private int i;

	public List<Task> masterProcess() {
		int counter = i++;
		
		Task task = new Task(counter % 2 + "", new TestTask(counter + ""));
		
		List<Task> list = new ArrayList<Task>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " - " + content);
	}

}
