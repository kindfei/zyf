package test.basic.thread;

import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;

public class TestProducerAndConsumer {
	
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();

	final Object[] items = new Object[10];
	int putptr, takeptr, count;

	public void put(Object x) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length)
				notFull.await();
			items[putptr] = x;
			if (++putptr == items.length)
				putptr = 0;
			++count;
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	public Object take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0)
				notEmpty.await();
			Object x = items[takeptr];
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			notFull.signal();
			return x;
		} finally {
			lock.unlock();
		}
	}
	
	
	Object mutex = new Object();
	
	public void set(Object o) throws InterruptedException {
		synchronized (mutex) {
			while (count == items.length) {
				System.out.println("set wait");
				mutex.wait();
				System.out.println("set run");
			}
			items[putptr] = o;
			if (++putptr == items.length)
				putptr = 0;
			++count;
			mutex.notifyAll();
			System.out.println("+++add:" + o);
		}
	}
	
	public Object get() throws InterruptedException {
		synchronized (mutex) {
			while (count == 0) {
				System.out.println("get wait");
				mutex.wait();
				System.out.println("get run");
			}
			Object o = items[takeptr];
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			mutex.notifyAll();
			System.out.println("---get:" + o);
			return o;
		}
	}
	
	
	Object hasSpace = new Object();
	Object hasItem = new Object();
	
	public void produce(Object o) throws InterruptedException {
		synchronized (mutex) {
			while (count == items.length)
				hasSpace.wait();
			items[putptr] = o;
			System.out.println("+++add:" + o);
			if (++putptr == items.length)
				putptr = 0;
			++count;
			hasItem.notifyAll();
		}
	}
	
	public Object consume() throws InterruptedException {
		synchronized (mutex) {
			while (count == 0)
				hasItem.wait();
			Object o = items[takeptr];
			System.out.println("---get:" + o);
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			hasSpace.notifyAll();
			return o;
		}
	}
	
	public static void main(String[] args) {
		final TestProducerAndConsumer inst = new TestProducerAndConsumer();
		
		new Thread(new Runnable() {

			public void run() {
				try {
					for (int i = 0; i < 1000; i++) {
						inst.set(i + "");
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}).start();
		
		new Thread(new Runnable() {

			public void run() {
				try {
					for (int i = 0; i < 1000; i++) {
						inst.get();
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
}
