package test.basic;

import java.util.LinkedList;

public class TestSynchronized extends Thread {

	public static void main(String[] args) {
		
	}
	
	private LinkedList list = new LinkedList();
	
	public void init() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		list.add("A");
	}
	
	public void run() {
		invoke();
	}
	
	public void invoke() {
		synchronized (list) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(list.removeFirst());
		}
	}
}
