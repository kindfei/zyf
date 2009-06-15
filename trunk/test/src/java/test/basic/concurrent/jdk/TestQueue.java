package test.basic.concurrent.jdk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestQueue {
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	class Producer extends Thread {
		private volatile boolean alive = true;
		
		public void run() {
			try {
				for (int i = 0; alive; i++) {
						Thread.sleep(1000);
						queue.put(i + "");
				}
				System.out.println(getName() + " is ended...");
			} catch (InterruptedException e) {
				System.out.println(getName() + " is interrupted for end...");
			}
		}
		
		public void end() {
			alive = false;
			interrupt();
		}
	}
	
	class Consumer extends Thread {
		private transient boolean alive = true;
		
		public void run() {
			try {
				while (alive) {
					String str = queue.take();
					System.out.println(getName() + " - " + str);
				}
				System.out.println(getName() + " is ended...");
			} catch (InterruptedException e) {
				System.out.println(getName() + " is interrupted for end...");
			}
		}
		
		public void end() {
			alive = false;
			interrupt();
		}
	}
	
	public void test() {
		Consumer[] cs = new Consumer[10];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = new Consumer();
			cs[i].setName("Consumer" + i);
			cs[i].start();
		}
		
		Producer p = new Producer();
		p.setName("Producer");
		p.start();
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		p.end();
		
		for (int i = 0; i < cs.length; i++) {
			cs[i].end();
		}
	}
	
	public static void main(String[] args) {
		TestQueue test = new TestQueue();
		test.test();
	}
}
