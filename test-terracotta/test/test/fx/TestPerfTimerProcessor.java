package test.fx;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.cluster.tasks.TestPerfTaskBean;

import fx.cluster.core.ExecuteMode;
import fx.cluster.core.Task;
import fx.cluster.core.TimerProcessor;

public class TestPerfTimerProcessor extends TimerProcessor {
	private static final Log log = LogFactory.getLog(TestPerfTimerProcessor.class);
	
	private int takerSize;
	private int msgCount;
	private int msgSize;
	
	private int[] content;
	private String nodeId = System.getProperty("nodeId");
	
	TestPerfTimerProcessor(int takerSize, int msgCount, int msgSize) {
		this.takerSize = takerSize;
		this.msgCount = msgCount;
		this.msgSize = msgSize;
		
		int size = 1024 * msgSize;
		content = new int[size];
		for (int i = 0; i < size; i++) {
			content[i] = i;
		}
	}

	@Override
	public List<Task> masterProcess() {
		List<Task> tasks = new ArrayList<Task>();
		TestPerfTaskBean bean = null;
		
		for (int i = 0; i < msgCount; i++) {
			bean = new TestPerfTaskBean();
			bean.setSeq(i);
			bean.setContent(content);
			tasks.add(new Task(ExecuteMode.TASK_QUEUE, bean));
		}
		
		long st = System.currentTimeMillis();
		bean.setTime(st);
		
		return tasks;
	}

	@Override
	public void workerProcess(Task task) {
		TestPerfTaskBean bean = (TestPerfTaskBean) task.getContent();
		log.info(" Got task " + bean.getSeq());
		long st = bean.getTime();
		if (st != 0) {
			long cost = System.currentTimeMillis() - st;
			log.info("nodeId=" + nodeId + ", takerSize=" + takerSize + ", msgCount=" + msgCount + ", msgSize=" + msgSize + "kb, time cost=" + cost + "ms.");
		}
	}

}
