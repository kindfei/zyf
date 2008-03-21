package test.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.Callable;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;

public class Test {
	private static int i;
	
	public static void main(String[] args) {
		Callable action = Executors.callable(new Runnable() {
			public void run() {
				i++;
			}
		});
		
		Executors.defaultThreadFactory();
	}
	
	
}
