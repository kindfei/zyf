package test.terracota;

public class Test {
	public static Test instance = new Test();
	
	int i = 0;
	
	public synchronized void count() {
		i++;
		System.out.println("Current counter: " + i);
	}
	
	public static void main(String[] args) {
		instance.count();
	}
	
}
