package fx.cluster.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ClusterLock extends ReentrantLock {
	private static final long serialVersionUID = 3165239504946320587L;
	
	ClusterLock() {
		super();
	}
	
	ClusterLock(boolean fair) {
		super(fair);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		while (!Thread.currentThread().isInterrupted()) {
			if (super.tryLock(5, TimeUnit.SECONDS)) {
				return;
			}
		}
		throw new InterruptedException();
	}
}
