package test.basic.concurrent.jdk;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.zyf.utils.CmdHelper;



public class TestReadWriteLock {
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	void LockRead() {
		lock.readLock().lock();
		System.out.println("LockRead");
	}
	
	void unlockRead() {
		lock.readLock().unlock();
		System.out.println("unlockRead");
	}
	
	void lockWirte() {
		lock.writeLock().lock();
		System.out.println("lockWirte");
	}
	
	void unlockWrite() {
		lock.writeLock().unlock();
		System.out.println("unlockWrite");
	}
	
	
	public static void main(String[] args) {
		TestReadWriteLock inst = new TestReadWriteLock();
		
		while (true) {
			int opt = CmdHelper.options(new String[] {
					"LockRead",
					"unlockRead",
					"lockWirte",
					"unlockWrite",
			});
			
			switch (opt) {
			case 0:
				inst.LockRead();
				break;

			case 1:
				inst.unlockRead();
				break;

			case 2:
				inst.lockWirte();
				break;

			case 3:
				inst.unlockWrite();
				break;

			default:
				break;
			}
		}
	}
}
