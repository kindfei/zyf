package test.bo.aspect;

public class TestBean2 extends TestBean1 {
	
	@Test1(property11="TestBean2-11")
	public void test1() {
		System.out.println("invoke TestBean2 test1");
	}
	
	public void test2() {
		System.out.println("invoke TestBean2 test2");
	}
}
