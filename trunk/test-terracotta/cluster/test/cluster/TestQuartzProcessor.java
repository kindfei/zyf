package test.cluster;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.QuartzProcessor;
import fx.cluster.core.Task;

import test.cluster.tasks.TestTask;

public class TestQuartzProcessor extends QuartzProcessor implements StatefulJob {
	private int i;

	public List<Task> masterProcess(JobExecutionContext context) {
		
		Task task = new Task(ExecuteMode.TASK_QUEUE, new TestTask(i++ + ""));
		
		List<Task> list = new ArrayList<Task>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(Task task) {
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);}
	
}
