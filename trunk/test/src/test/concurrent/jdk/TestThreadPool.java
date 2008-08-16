package test.concurrent.jdk;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
	
	private Executor pool1 = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	private Executor pool2 = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private Executor pool3 = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	private Executor pool4 = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	
	public void execute() {
		for (int i = 0; i < 100; i++) {
			pool4.execute(new Runner());
		}
	}
	
	class Runner implements Runnable {
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName());
		}
	}
	
	public static void main(String[] args) {
		TestThreadPool test = new TestThreadPool();
		test.execute();
	}
}
