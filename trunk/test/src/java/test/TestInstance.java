package test;

public class TestInstance extends Parent {
	static Property psp = new Property("static property");
	Property pp = new Property("property");
	
	static {
		System.out.println("static block init");
	}
	
	{
		System.out.println("instance block init");
	}
	
	TestInstance() {
		System.out.println("constructor init");
	}
	
	public static void main(String[] args) {
		new TestInstance();
	}
}

class Parent {
	static Property psp = new Property("static parent property");
	Property pp = new Property("parent property");
	
	static {
		System.out.println("static parent block init");
	}
	
	{
		System.out.println("parent instance block init");
	}
	
	Parent() {
		System.out.println("parent constructor init");
	}
}

class Property {
	Property(String name) {
		System.out.println(name + " init");
	}
}
