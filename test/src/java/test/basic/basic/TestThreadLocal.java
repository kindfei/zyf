package test.basic.basic;

public class TestThreadLocal implements Runnable{
	
	static int cnt;
	
	static ThreadLocal t1 = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new Integer(cnt++);
		}
	};
	
	static ThreadLocal t2 = new ThreadLocal();
	static ThreadLocal t3 = new ThreadLocal();

	public static void main(String[] args) {
		Thread thread0 = new Thread(new TestThreadLocal());
		thread0.start();
		Thread thread1 = new Thread(new TestThreadLocal());
		thread1.start();
		Thread thread2 = new Thread(new TestThreadLocal());
		thread2.start();
		Thread thread3 = new Thread(new TestThreadLocal());
		thread3.start();
	}
	
	public void run() {
		exe();
		exe();
	}
	
	private void exe() {
		String  s = t1.get().toString();
		
		String str = (String)t2.get();
		if (str == null) {
			t2.set("test");
			t3.set(new Integer(1));
			System.out.println("<" + s + "> " + "t2.get() == null");
		} else {
			t3.set(new Integer(((Integer)t3.get()).intValue() + 1));
			System.out.println("<" + s + "> " + "t2.get() != null");
		}
		System.out.println("<" + s + "> " + "t2: " + t2.get().toString());
		System.out.println("<" + s + "> " + "t3: " + t3.get().toString());
	}
}
