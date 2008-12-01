package test.terracota;

public class TestTransient {
	public static TestTransient instance = new TestTransient();
	
	private Counter counter = new Counter();
	
	public static void main(String[] args) {
		System.out.println(instance.counter.i);
	}
	
	class Counter {
		private transient int i;
		
		private void onload() {
			i = i + 1;
		}
	}
}
