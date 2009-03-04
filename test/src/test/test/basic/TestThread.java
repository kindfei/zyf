package test.basic;

public class TestThread {
	void makeThread() {
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	void countThread() {
		for (int i=0; i<5; i++) {
			makeThread();
		}
	}
	
	public static void main(String[] args) {
		TestThread t = new TestThread();
		t.countThread();
	}
}
