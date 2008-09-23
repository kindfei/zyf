package fx.cluster.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TaskQueue
 * @author zhangyf
 *
 */
public class TaskQueue extends LinkedBlockingQueue<Task> {
	private static final long serialVersionUID = -3202737378383193112L;
	
	private ClusterLock lock = new ClusterLock(true);
	
	private Map<String, GroupTaskFilter> map = new ConcurrentHashMap<String, GroupTaskFilter>();
	
	public Task fairTake() throws InterruptedException {
		lock.lockInterruptibly();
		
		try {
			return super.take();
		} finally {
			lock.unlock();
		}
	}
	
	boolean addTask(Task task) {
		String groupId = task.getGroupId();
		if (groupId != null) {
			GroupTaskFilter filter = map.get(groupId);
			if (filter == null) {
				synchronized (map) {
					filter = map.get(groupId);
					if (filter == null) {
						filter = new GroupTaskFilter(groupId, this);
						map.put(groupId, filter);
						filter.onload();
					}
				}
			}
			return filter.add(task);
		} else {
			return add(task);
		}
	}
	
	boolean addAllTask(List<Task> tasks) {
		return addAll(tasks);
	}
}
