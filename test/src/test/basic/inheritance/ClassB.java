package test.basic.inheritance;

public class ClassB extends ClassA {
	static ClassTest sa = new ClassTest("ClassB parameter [sa] initialize");
	ClassTest a = new ClassTest("ClassB parameter [a] initialize");
	
	static {
		System.out.println("ClassB static block1 running");
	}
	
	static ClassTest sb = new ClassTest("ClassB parameter [sb] initialize");
	ClassTest b = new ClassTest("ClassB parameter [b] initialize");
	
	{
		System.out.println("ClassB initialize block running");
	}
	
	static ClassTest sc = new ClassTest("ClassB parameter [sc] initialize");
	ClassTest c = new ClassTest("ClassB parameter [c] initialize");
	
	static {
		System.out.println("ClassB static block2 running");
	}
	
	static ClassTest sd = new ClassTest("ClassB parameter [sd] initialize");
	ClassTest d = new ClassTest("ClassB parameter [d] initialize");
	
	public ClassB() {
		super();
		System.out.println("ClassB instance method running");
	}
	
	static ClassTest getSa() {
		return sa;
	}
}
