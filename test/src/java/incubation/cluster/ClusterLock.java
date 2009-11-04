package incubation.cluster;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cluster Lock using in ACTIVE-STANDBY mode.
 * @author zhangyf
 *
 */
public class ClusterLock {
	private ReentrantLock lock = new ReentrantLock();

	public void acquireMutex() throws InterruptedException {
		while (!Thread.interrupted()) {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				return;
			}
		}
		throw new InterruptedException();
	}
	
	void releaseMutex() {
		lock.unlock();
	}
}
