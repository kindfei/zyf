package test.cluster.core.tc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * GroupTaskFilter
 * @author zhangyf
 *
 */
public class GroupTaskFilter implements Runnable {
	
	private static final Log log = LogFactory.getLog(GroupTaskFilter.class);
	
	private TaskQueue taskQueue;
	private String groupId;
	
	private BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
	private ReentrantLock groupLock = new ReentrantLock();
	
	private transient Thread thread;
	
	public GroupTaskFilter(String groupId, TaskQueue taskQueue) {
		this.groupId = groupId;
		this.taskQueue = taskQueue;
	}
	
	void onload() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.setName("GroupTaskFilter. groupId=" + groupId);
			thread.start();
		}
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public boolean add(Task task) {
		queue.clear();
		return queue.add(task);
	}
	
	public synchronized void run() {
		try {
			while(true) {
				groupLock.lock();
				try {
					Task task = queue.take();
					task.setGroupLock(groupLock);
					Condition finish = groupLock.newCondition();
					task.setFinish(finish);
					
					taskQueue.addTask(task);
					
					finish.await(1, TimeUnit.SECONDS);
				} finally {
					groupLock.unlock();
				}
			}
		} catch (InterruptedException e) {
			log.error("GroupTaskFilter was interrupted. groupId=" + groupId, e);
		} catch (Throwable e) {
			log.error("GroupTaskFilter error. groupId=" + groupId, e);
		}
	}
}
