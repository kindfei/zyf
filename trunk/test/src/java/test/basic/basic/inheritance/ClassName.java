package test.basic.basic.inheritance;

public class ClassName {
	public static void main(String[] args) {
		new ClassName.C().showAllName();
	}
	
	static class P {
		void showName() {
			System.out.println(this.getClass().getName());
		}
	}

	static class C extends P {
		void showAllName() {
			showName();
			System.out.println(this.getClass().getName());
		}
	}
}