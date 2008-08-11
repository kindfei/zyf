package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import test.cluster.core.Service;
import test.cluster.core.ServiceFactory;
import test.cluster.core.TimerProcessor;
import test.cluster.core.tc.Task;
import test.cluster.tc.caches.TestCache;
import test.cluster.tc.tasks.TestTask;

public class TestCluster {
	
	private Service service1;
	
	public TestCluster() {
		service1 = ServiceFactory.getTimerDrivenService(Service.SERVICE_MODE_HA, 2, true, new TestTimerProcessor(), 0, 2000);
	}
	
	public void startup() {
		try {
			service1.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		service1.shutdown();
	}
	
	public static void main(String[] args) {
		TestCluster test = new TestCluster();
		test.startup();
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		test.shutdown();
	}

}

class TestTimerProcessor extends TimerProcessor {

	public List<Task> masterProcess() {
		Date date = Calendar.getInstance().getTime();
		
		String time = new SimpleDateFormat("HH:mm:ss").format(date);
		
		Task t1 = new Task(Service.EXECUTE_MODE_P2P, new TestTask(time + "-task1"));
		Task t2 = new Task(Service.EXECUTE_MODE_P2P, new TestTask(time + "-task2"));
		Task t3 = new Task(Service.EXECUTE_MODE_P2P, new TestTask(time + "-task3"));
		
		List<Task> list = new ArrayList<Task>();
		list.add(t1);
		list.add(t2);
		list.add(t3);
		
		return list;
	}

	public void workerProcess(Task task) {
		
		TestCache cache = (TestCache) getCacheValue("test");
		
		if (cache == null) {
			cache = new TestCache();
			setCacheValue("test", cache);
		}
		
		System.out.println(cache.getValue());
		
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