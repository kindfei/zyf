package test.basic.concurrent.jdk;

import incubation.utils.CommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {
	
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
			
			System.out.println(Thread.currentThread().getName() + " - task " + i);
		}
	}
	
	private void execute(ExecutorService es, int taskSum, boolean wait) {
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (int i = 0; i < taskSum; i++) {
			System.out.println("add task " + i);
			try {
				futures.add(es.submit(new Runner(i)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (wait) {
			for (Future<?> future : futures) {
				try {
					future.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	void test1(ExecutorService es) {
		for (;;) {
			execute(es, 10, true);
			if (!CommandUtils.confirm("execute again?")) {
				break;
			}
		}
		
		es.shutdown();
	}
	
	public static void main(String[] args) {
		ExecutorService es = new ThreadPoolExecutor(
//				5, 5, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
				2, 5, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(8));
//				5, Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//				5, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		TestThreadPool test = new TestThreadPool();
		test.test1(es);
	}
}
