package test.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.Task;
import fx.cluster.core.TimerProcessor;

public class TestPerformanceProcessor extends TimerProcessor {
	private static final Log log = LogFactory.getLog(TestPerformanceProcessor.class);

	@Override
	public List<Task> masterProcess() {
		int amount = 0;
		try {
			Properties prop = new Properties();
			prop.load(ClassLoader.getSystemResourceAsStream("TestPerformanceProcessor.properties"));
			amount = Integer.parseInt(prop.getProperty("MESSAGE_AMOUNT"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Task> list = new ArrayList<Task>();
		for (int i = 0; i < amount; i++) {
			Long st = Calendar.getInstance().getTimeInMillis();
			Task task = new Task(ExecuteMode.TASK_QUEUE, new Long(st));
			list.add(task);
		}
		return list;
	}

	@Override
	public void workerProcess(Task task) {
		long st = ((Long) task.getContent()).longValue();
		long et = Calendar.getInstance().getTimeInMillis();
		log.info("Dispatch end time=" + et + " cost=" + (et - st) + "ms");
	}

}
