package test.basic;

public class TestVolatileFields {
	static int i;
	static int j;
	
	static void add() {
		i++;
		j++;
	}
	
	static void show() {
		if (j > i) {
			System.out.println("i=" + i + " j=" + j);
		}
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					add();
				}
			}
		});
		
		t.start();
		
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			show();
		}
	}
}
