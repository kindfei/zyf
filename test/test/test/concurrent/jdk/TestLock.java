package test.concurrent.jdk;

import java.util.concurrent.locks.ReentrantLock;

public class TestLock {
	private static ReentrantLock lock = new ReentrantLock();
	
	static class LockThread extends Thread {
		public void run() {
			System.out.println(getName() + "-LockThread lock begin.");
			lock.lock();
			System.out.println(getName() + "-LockThread lock end.");
		}
	}
	
	static class InterLockThread extends Thread {
		public void run() {
			try {
				System.out.println(getName() + "-InterLockThread lock begin.");
				lock.lockInterruptibly();
				System.out.println(getName() + "-InterLockThread lock end.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	static void testLcok() {
		LockThread l1 = new LockThread();
		LockThread l2 = new LockThread();
		l1.start();
		l2.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("l1 isAlive=" + l1.isAlive());
		System.out.println("l2 isAlive=" + l2.isAlive());
		
		l2.interrupt();
		System.out.println("Interrupted " + l2.getName());
	}
	
	static void testInterLcok() {
		InterLockThread l1 = new InterLockThread();
		InterLockThread l2 = new InterLockThread();
		l1.start();
		l2.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("l1 isAlive=" + l1.isAlive());
		System.out.println("l2 isAlive=" + l2.isAlive());
		
		l2.interrupt();
		System.out.println("Interrupted " + l2.getName());
	}
	
	
	public static void main(String[] args) {
//		testLcok();
		testInterLcok();
	}
}
