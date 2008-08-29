package test.cluster.core.tc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class GroupTaskDispatcher {
	private GroupTaskQueue taskQueue;
	
	private LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	
	private ReentrantLock groupLock = new ReentrantLock(true);
	
	public boolean add(Task task) {
		return queue.add(task);
	}
}
