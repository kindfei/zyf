package test.basic.basic.initialization;

public class ClassA {
	static ClassTest sa = new ClassTest("ClassA parameter [sa] initialize");
	ClassTest a = new ClassTest("ClassA parameter [a] initialize");
	
	static {
		System.out.println("ClassA static block1 running");
	}
	
	static ClassTest sb = new ClassTest("ClassA parameter [sb] initialize");
	ClassTest b = new ClassTest("ClassA parameter [b] initialize");
	
	{
		System.out.println("ClassA initialize block running");
	}
	
	static ClassTest sc = new ClassTest("ClassA parameter [sc] initialize");
	ClassTest c = new ClassTest("ClassA parameter [c] initialize");
	
	static {
		System.out.println("ClassA static block2 running");
	}
	
	static ClassTest sd = new ClassTest("ClassA parameter [sd] initialize");
	ClassTest d = new ClassTest("ClassA parameter [d] initialize");
	
	public ClassA() {
		System.out.println("ClassA instance method running");
	}
}
