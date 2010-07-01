package test.bo.aspect;

public class TestBean1 implements TestBean {
	
	@Test1(property11="TestBean1-11")
	@Test2(property21="TestBean1-21")
	public void test1() {
		System.out.println("invoke TestBean1 test1");
	}
	
	@Test1(property12="TestBean1-12")
	@Test2(property22="TestBean1-22")
	public void test2() {
		System.out.println("invoke TestBean1 test2");
	}
	
	@Test1(property13="TestBean1-13")
	@Test2(property23="TestBean1-23")
	public void test3() {
		System.out.println("invoke TestBean1 test3");
	}
}
