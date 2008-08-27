package test.cluster.core.tc;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TaskQueue
 * @author Zhangyf
 *
 */
public class TaskQueue extends LinkedBlockingQueue<Task> {
	private static final long serialVersionUID = -3202737378383193112L;
	
	private ReentrantLock lock = new ReentrantLock(true);
	
	public Task take() throws InterruptedException {
		lock.lockInterruptibly();
		
		try {
			return super.take();
		} finally {
			lock.unlock();
		}
	}
}
