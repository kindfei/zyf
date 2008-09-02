package test.cluster.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TaskQueue
 * @author zhangyf
 *
 */
public class TaskQueue extends LinkedBlockingQueue<Task> {
	private static final long serialVersionUID = -3202737378383193112L;
	
	private ReentrantLock lock = new ReentrantLock(true);
	
	private Map<String, GroupTaskFilter> map = new ConcurrentHashMap<String, GroupTaskFilter>();
	
	public Task fairTake() throws InterruptedException {
		lock.lockInterruptibly();
		
		try {
			return super.take();
		} finally {
			lock.unlock();
		}
	}
	
	public boolean add(Task task) {
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
			return addTask(task);
		}
	}
	
	boolean addTask(Task task) {
		return super.add(task);
	}
}
