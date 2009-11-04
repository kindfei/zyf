package test.terracotta.cluster;

import incubation.cluster.ClusterTask;
import incubation.cluster.ExecuteMode;
import incubation.cluster.QuartzProcessor;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;

import test.terracotta.cluster.tasks.TestTaskBean;

public class TestQuartzProcessor extends QuartzProcessor implements StatefulJob {
	private int i;

	public List<ClusterTask> masterProcess(JobExecutionContext context) {
		
		ClusterTask task = new ClusterTask(ExecuteMode.TASK_QUEUE, new TestTaskBean(i++ + ""));
		
		List<ClusterTask> list = new ArrayList<ClusterTask>();
		list.add(task);
		
		return list;
	}

	public void workerProcess(ClusterTask task) {
		TestTaskBean bean = (TestTaskBean) task.getContent();
		String content = bean.getStr();
		System.out.println(Thread.currentThread().getName() + " - " + content);}
	
}
