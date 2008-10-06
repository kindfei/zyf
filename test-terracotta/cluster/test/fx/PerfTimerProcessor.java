package test.fx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.cluster.tasks.PerfBean;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.Task;
import fx.cluster.core.TimerProcessor;

public class PerfTimerProcessor extends TimerProcessor {
	private static final Log log = LogFactory.getLog(PerfTimerProcessor.class);
	
	private boolean isFair;
	private int takerSize;
	private int counter;
	private String nodeId = System.getProperty("nodeId");
	
	PerfTimerProcessor(boolean isFair, int takerSize) {
		this.isFair = isFair;
		this.takerSize = takerSize;
	}

	@Override
	public List<Task> masterProcess() {
		int amount = 0;
		
		switch (counter % 3) {
		case 0:
			amount = 10;
			break;

		case 1:
			amount = 100;
			break;

		case 2:
			amount = 1000;
			break;
		}
		
		counter++;
		
		List<Task> tasks = new ArrayList<Task>();
		PerfBean bean = null;
		for (int i = 0; i < amount; i++) {
			bean = new PerfBean();
			bean.setContent("123456789012345678901234567890");
			tasks.add(new Task(ExecuteMode.TASK_QUEUE, bean));
		}
		long st = Calendar.getInstance().getTimeInMillis();
		bean.setTime(st);
		bean.setLast(true);
		bean.setAmount(amount);
		return tasks;
	}

	@Override
	public void workerProcess(Task task) {
		PerfBean bean = (PerfBean) task.getContent();
		if (bean.isLast()) {
			long et = Calendar.getInstance().getTimeInMillis();
			long st = bean.getTime();
			int amount = bean.getAmount();
			log.info("nodeId=" + nodeId + " isFair=" + isFair + " amount=" + amount + " takerSize=" + takerSize + " time cost=" + (et - st) + "ms");
		}
	}

}
