package test.terracota;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import common.CmdHelper;



public class TestHa {
	public static final TestHa instance = new TestHa();
	
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private ReentrantLock lock = new ReentrantLock();
	
	public void put(String str) {
		try {
			queue.put(str);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String take() {
		String str = null;
		try {
			str = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	public void lock() {
		try {
			lock.lockInterruptibly();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unlock() {
		lock.unlock();
	}
	
	public void dmiMethod() {
		try {
			Thread.sleep(2 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		a:while (true) {
			int opt = CmdHelper.options(new String[] {
					"put",
					"take",
					"lock",
					"unlock",
					"dmiMethod",
			});
			
			switch (opt) {
			case 0:
				instance.put(CmdHelper.input("Input the value"));
				break;

			case 1:
				System.out.println(instance.take());
				break;

			case 2:
				instance.lock();
				break;

			case 3:
				instance.unlock();
				break;

			case 4:
				instance.dmiMethod();
				break;

			case 5:
				break a;

			default:
				break;
			}
			
		}
	}
}
