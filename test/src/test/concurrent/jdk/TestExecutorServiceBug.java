package test.concurrent.jdk;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutorServiceBug {
	private ExecutorService pool;
	
	class CreateThread extends Thread {
		
		public void run() {
			pool = Executors.newCachedThreadPool();
			Timer timer = new Timer();
			timer.schedule(new Task(), 0, 1000);
			
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			timer.cancel();
			pool.shutdown();
		}
	}
	
	class Task extends TimerTask {
		int i;
		public void run() {
			pool.execute(new Runnable() {

				public void run() {
					System.out.println(i++);
				}
				
			});
			
		}
		
	}
	
	public void test() {
		Thread t = new CreateThread();
		t.start();
		
		try {
			Thread.sleep(1 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t.interrupt();
	}
	
	public static void main(String[] args) {
		TestExecutorServiceBug test = new TestExecutorServiceBug();
		
		test.test();
	}
}
