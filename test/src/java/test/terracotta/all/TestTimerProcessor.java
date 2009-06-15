package test.terracotta.all;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zyf.cluster.ClusterTask;
import org.zyf.cluster.ExecuteMode;
import org.zyf.cluster.TimerProcessor;
import org.zyf.jmx.ATTRIBUTE;
import org.zyf.jmx.OPERATION;


public class TestTimerProcessor extends TimerProcessor {
	
	@ATTRIBUTE(isWritable = true)
	private long counter;

	@ATTRIBUTE
	private List<String> list = new ArrayList<String>();

	@ATTRIBUTE
	private String[] array = new String[4];

	@ATTRIBUTE
	private Map<String, String> map = new HashMap<String, String>();
	
	TestTimerProcessor() {
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		
		array[0] = "a";
		array[1] = "b";
		array[2] = "c";
		array[3] = "d";
		
		map.put("1", "a");
		map.put("2", "b");
		map.put("3", "c");
		map.put("4", "d");
	}

	@Override
	public List<ClusterTask> masterProcess() {
		counter++;
		List<ClusterTask> tasks = new ArrayList<ClusterTask>();
		tasks.add(new ClusterTask(ExecuteMode.TASK_QUEUE, counter));
		return tasks;
	}

	@Override
	public void workerProcess(ClusterTask task) {
		System.out.println(task.getContent());
	}
	
	@OPERATION
	public void setValue(long value) {
		counter = value;
	}
	
	@OPERATION
	public void refresh() {
		counter = 0;
	}

}
