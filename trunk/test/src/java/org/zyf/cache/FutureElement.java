package org.zyf.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class FutureElement<K, V> extends Element<K, V> {

	private Cache<K, V> cache;
	private FutureTask<V> task;
	
	private AtomicBoolean run = new AtomicBoolean(false);
	
	FutureElement(K key, Cache<K, V> cache) {
		super(key, null);
		
		this.cache = cache;
		
		task = new FutureTask<V>(new LoadCall());
		
		task.run();
	}
	
	public void run() {
		if (run.compareAndSet(false, true)) task.run();
	}
	
	@Override
	public V getValue() {
		V value = null;
		try {
			value = task.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		
		if (value == null) {
			cache.remove(getKey());
		} else {
			cache.put(getKey(), value);
		}
		
		return value;
	}
	
	private class LoadCall implements Callable<V> {

		@Override
		public V call() throws Exception {
			return cache.getCacheLoader().load(getKey());
		}
		
	}
}
