package test.cluster.core.tc;

public class ClusterLock {
	public synchronized void acquire() {
		try {
			System.out.println("acquired mutex...");
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
