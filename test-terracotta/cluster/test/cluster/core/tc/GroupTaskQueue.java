package test.cluster.core.tc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupTaskQueue extends TaskQueue {

	private static final long serialVersionUID = -2484774333457191353L;
	
	private Map<String, GroupTaskDispatcher> map = new ConcurrentHashMap<String, GroupTaskDispatcher>();
	
	public boolean add(Task task) {
		return super.add(task);
	}
	
	protected boolean addTask(Task task) {
		return super.add(task);
	}
}
