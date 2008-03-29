package test.basic.inheritance;

public class ClassTest {
	
	public ClassTest() {
		System.out.println("No parameter specify");
	}
	
	public ClassTest(String str) {
		System.out.println(str);
	}
	
	public static void main(String[] argv) {
		//ClassB test = new ClassB();
		ClassB.getSa();
	}
}
