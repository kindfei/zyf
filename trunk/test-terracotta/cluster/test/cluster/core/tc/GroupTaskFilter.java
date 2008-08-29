package test.cluster.core.tc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GroupTaskFilter implements Runnable {
	
	private TaskQueue taskQueue;
	private String groupId;
	
	private LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	private ReentrantLock groupLock = new ReentrantLock(true);
	private Condition finish = groupLock.newCondition();
	
	private transient Thread thread;
	
	public GroupTaskFilter(String groupId, TaskQueue taskQueue) {
		this.groupId = groupId;
		this.taskQueue = taskQueue;
		onload();
	}
	
	private void onload() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public boolean add(Task task) {
		return queue.add(task);
	}
	
	public synchronized void run() {
		try {
			while(true) {
				groupLock.lock();
				try {
					Task task = queue.take();
					task.setGroupLock(groupLock);
					task.setFinish(finish);
					
					taskQueue.addTask(task);
					
					finish.await();
				} finally {
					groupLock.unlock();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
