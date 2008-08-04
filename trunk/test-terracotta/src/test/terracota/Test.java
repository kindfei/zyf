package test.terracota;

import zyf.helper.CmdHelper;

public class Test {
	public static Test instance = new Test();
	
	int i = 0;
	
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
				break a;

			default:
				break;
			}
			
		}
	}
	
}
