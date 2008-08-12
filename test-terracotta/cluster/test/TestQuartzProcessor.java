package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;

import test.cluster.core.QuartzProcessor;
import test.cluster.core.Service;
import test.cluster.core.tc.Task;
import test.cluster.tc.caches.TestCache;
import test.cluster.tc.tasks.TestTask;

public class TestQuartzProcessor extends QuartzProcessor implements StatefulJob {

	public List<Task> masterProcess(JobExecutionContext t) {
		System.out.println("QuartzDataMap-" + t.getMergedJobDataMap().get("testkey"));
		
		Date date = Calendar.getInstance().getTime();
		
		String time = new SimpleDateFormat("HH:mm:ss").format(date);
		
		Task t1 = new Task(Service.MODE_EXECUTE_LOCAL_INVOKE, new TestTask("Quartz-" + time + "-task1"));
		Task t2 = new Task(Service.MODE_EXECUTE_TASK_QUEUE, new TestTask("Quartz-" + time + "-task2"));
		Task t3 = new Task(Service.MODE_EXECUTE_ALL_INVOKE, new TestTask("Quartz-" + time + "-task3"));
		
		List<Task> list = new ArrayList<Task>();
		list.add(t1);
		list.add(t2);
		list.add(t3);
		
		return list;
	}

	public void workerProcess(Task task) {
		String localValue = "";
		
		TestCache cache = (TestCache) getCacheValue("test");
		
		if (cache == null) {
			cache = new TestCache();
			setCacheValue("test", cache);
		}
		
		String cacheValue = cache.getValue();
		if (cacheValue != null && !localValue.equals(cacheValue)) {
			System.out.println("cacheValue - " + cacheValue);
			localValue = cacheValue;
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		TestTask bean = (TestTask) task.getContent();
		String content = bean.getStr();
		
		System.out.println(Thread.currentThread().getName() + " - " + content);
		
		cache.setValue(content);
	}
	
}
