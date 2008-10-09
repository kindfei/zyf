package test.terracota;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestCachePerf {
	public static final Log log = LogFactory.getLog(TestCachePerf.class);
	
	public static final TestCachePerf instance = new TestCachePerf();
	
	private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
	
	public Object set(String key, Object value) {
		return map.put(key, value);
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public Object delete(String key) {
		return map.remove(key);
	}
	
	public static void main(String[] args) {
		
		int threads = Integer.parseInt(args[0]);
		int runs = Integer.parseInt(args[1]);
		int size = 1024 * Integer.parseInt(args[2]);
		
		// get object to store
		int[] obj = new int[size];
		for (int i = 0; i < size; i++) {
			obj[i] = i;
		}

		String[] keys = new String[size];
		for (int i = 0; i < size; i++) {
			keys[i] = "test_key" + i;
		}
		
		List<Runner> tasks = new ArrayList<Runner>();
		for (int i = 0; i < threads; i++) {
			Runner r = new Runner(runs, obj, keys);
			tasks.add(r);
		}
		
		ExecutorService pool = Executors.newCachedThreadPool();
		try {
			List<Future<StringBuilder>> futures = pool.invokeAll(tasks);
			for (Future<StringBuilder> future : futures) {
				StringBuilder result = future.get();
				log.info(result);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			log.error(e.getMessage(), e);
		}
		
		System.exit(1);
	}
	
	private static class Runner implements Callable<StringBuilder> {
		private int runs;
		private int[] object;
		private String[] keys;
		private int size;
		
		public Runner(int runs, int[] object, String[] keys) {
			this.runs = runs;
			this.object = object;
			this.keys = keys;
			this.size = object.length;
		}

		public StringBuilder call() throws Exception {
			StringBuilder result = new StringBuilder();
			String name = Thread.currentThread().getName();
			
			// time deletes
			long start = System.currentTimeMillis();
			for (int i = 0; i < runs; i++) {
				instance.delete(keys[i]);
			}
			long elapse = System.currentTimeMillis() - start;
			float avg = (float) elapse / runs;
			result.append("\nthread " + name + ": runs: " + runs + " deletes of obj " + (size/1024) + "KB -- avg time per req " + avg + " ms (total: " + elapse + " ms)");

			// time stores
			start = System.currentTimeMillis();
			for (int i = 0; i < runs; i++) {
				instance.set(keys[i], object);
			}
			elapse = System.currentTimeMillis() - start;
			avg = (float) elapse / runs;
			result.append("\nthread " + name + ": runs: " + runs + " stores of obj " + (size/1024) + "KB -- avg time per req " + avg + " ms (total: " + elapse + " ms)");
			
			// time get
			start = System.currentTimeMillis();
			for (int i = 0; i < runs; i++) {
				instance.get(keys[i]);
			}
			elapse = System.currentTimeMillis() - start;
			avg = (float) elapse / runs;
			result.append("\nthread " + name + ": runs: " + runs + " gets of obj " + (size/1024) + "KB -- avg time per req " + avg + " ms (total: " + elapse + " ms)");

			return result;
		}
	}
}
