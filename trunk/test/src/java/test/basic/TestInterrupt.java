package test.basic;

public class TestInterrupt implements Runnable {
	public static void main(String[] args) {
		TestInterrupt test = new TestInterrupt();
		Thread t = new Thread(test);
		t.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
		System.out.println("interrupt1 - " + t.isInterrupted());
		t.interrupt();
		System.out.println("interrupt2 - " + t.isInterrupted());
	}
	
	public void run() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("wait1 - " + Thread.interrupted());
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("wait2 - " + Thread.interrupted());
		}
	}
}
