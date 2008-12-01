package test.concurrent.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
	
	private static ExecutorService pool1 = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	private static ExecutorService pool2 = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static ExecutorService pool3 = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	private static ExecutorService pool4 = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static ExecutorService pool5 = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	private static ExecutorService pool6 = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private static ExecutorService pool7 = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1));
	
	public void execute(ExecutorService pool) {
		for (int i = 0; i < 100; i++) {
			System.out.println("add " + i);
			pool.execute(new Runner(i));
		}
		pool.shutdown();
	}
	
	class Runner implements Runnable {
		private int i;
		
		Runner(int i) {
			this.i = i;
		}
		
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName() + " - " + i);
		}
	}
	
	public static void main(String[] args) {
		TestThreadPool test = new TestThreadPool();
		test.execute(pool6);
	}
}
