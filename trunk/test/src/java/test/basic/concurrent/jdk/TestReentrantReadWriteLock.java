package test.basic.concurrent.jdk;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.zyf.utils.CmdHelper;


public class TestReentrantReadWriteLock {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	void lockRead() {
		lock.readLock().lock();
		System.out.println(Thread.currentThread().getName() + " - lockRead.");
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}

		lock.readLock().unlock();
		System.out.println(Thread.currentThread().getName() + " - unlockRead.");
	}
	
	void lockWrite() {
		lock.writeLock().lock();
		System.out.println(Thread.currentThread().getName() + " - lockWrite.");

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		
		lock.writeLock().unlock();
		System.out.println(Thread.currentThread().getName() + " - unlockWrite.");
	}
	
	public static void main(String[] args) {
		final TestReentrantReadWriteLock inst = new TestReentrantReadWriteLock();
		
		for(;;) {
			int opt = CmdHelper.options(new String[] {
					"lockRead",
					"lockWrite",
			});
			
			switch (opt) {
			case 0:
				new Thread() {
					@Override
					public void run() {
						inst.lockRead();
					}
				}.start();
				break;
			case 1:
				new Thread() {
					@Override
					public void run() {
						inst.lockWrite();
					}
				}.start();
				break;
			}
		}
	}
}
