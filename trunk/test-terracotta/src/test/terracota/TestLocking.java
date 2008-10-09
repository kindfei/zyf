package test.terracota;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import zyf.helper.CmdHelper;

public class TestLocking {
	public static TestLocking instance = new TestLocking();
	
	int i = 0;
	
	Object lock1 = new Object();
	Object lock2 = new Object();
	
	Lock lockA = new ReentrantLock();
	Lock lockB = new ReentrantLock();
	
	public synchronized void writeLock() {
		CmdHelper.pause("writeLock");
	}
	
	public synchronized void readLock() {
		CmdHelper.pause("readLock");
	}
	
	public synchronized void concurrentLock() {
		CmdHelper.pause("concurrentLock");
	}
	
	public synchronized void noLock() {
		CmdHelper.pause("noLock");
	}
	
	public void asWriteLock() {
		CmdHelper.pause("asWriteLock");
	}
	
	public void asReadLock() {
		CmdHelper.pause("asReadLock");
	}
	
	public void asConcurrentLock() {
		CmdHelper.pause("asConcurrentLock");
	}
	
	public void writeLock1() {
		synchronized (lock1) {
			CmdHelper.pause("writeLock1");
		}
	}
	
	public void writeLock2() {
		synchronized (lock2) {
			CmdHelper.pause("writeLock2");
		}
	}
	
	public void lockA() {
		lockA.lock();
		CmdHelper.pause("lockA");
		lockA.unlock();
	}
	
	public void lockB() {
		lockB.lock();
		CmdHelper.pause("lockB");
		lockB.unlock();
	}
	
	public static void main(String[] args) {
		a:while (true) {
			int opt = CmdHelper.options(new String[] {
					"writeLock",
					"readLock",
					"concurrentLock",
					"noLock",
					"asWriteLock",
					"asReadLock",
					"asConcurrentLock",
					"writeLock1",
					"writeLock2",
					"lockA",
					"lockB",
					"cancel",
			});
			
			switch (opt) {
			case 0:
				instance.writeLock();
				break;

			case 1:
				instance.readLock();
				break;

			case 2:
				instance.concurrentLock();
				break;

			case 3:
				instance.noLock();
				break;

			case 4:
				instance.asWriteLock();
				break;

			case 5:
				instance.asReadLock();
				break;

			case 6:
				instance.asConcurrentLock();
				break;

			case 7:
				instance.writeLock1();
				break;

			case 8:
				instance.writeLock2();
				break;

			case 9:
				instance.lockA();
				break;

			case 10:
				instance.lockB();
				break;

			case 11:
				break a;

			default:
				break;
			}
			
		}
	}
	
}
