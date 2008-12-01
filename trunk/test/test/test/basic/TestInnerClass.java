package test.basic;

public class TestInnerClass {
	public static void main(String[] args) {
		OuterClass.StaticInnerClass.staticAdd();
		
		OuterClass.StaticInnerClass t1 = new OuterClass.StaticInnerClass();
		t1.add();
		t1.staticAdd();
		
		OuterClass t = new OuterClass();
		
		OuterClass.InnerClass t2 = t.new InnerClass();
		t2.add();
	}
}

class OuterClass {
	int i1;
	static int i2;
	
	void addI1() {
		i1++;
	}
	
	static void addI2() {
		i2++;
	}
	
	
	static class StaticInnerClass {
		
		void add() {
			addI2();
		}
		
		static void staticAdd() {
			addI2();
		}
	}
	
	class InnerClass {
		
		void add() {
			addI1();
			addI2();
		}
	}
}
