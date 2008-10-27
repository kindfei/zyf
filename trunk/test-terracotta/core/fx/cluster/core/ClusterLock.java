package fx.cluster.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cluster Lock using in ACTIVE-STANDBY mode.
 * @author zhangyf
 *
 */
public class ClusterLock extends ReentrantLock {
	private static final long serialVersionUID = 3165239504946320587L;

	@Override
	public void lockInterruptibly() throws InterruptedException {
		while (!Thread.interrupted()) {
			if (super.tryLock(5, TimeUnit.SECONDS)) {
				return;
			}
		}
		throw new InterruptedException();
	}
}
