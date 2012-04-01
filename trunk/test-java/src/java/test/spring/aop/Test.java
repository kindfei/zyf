package test.spring.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.util.Utils;


public class Test {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = Utils.loadContext(Test.class, "context.xml");
		TestService ts = (TestService) c.getBean("testService");
		
		ts.sayHello("world");
		
		try {
			ts.sayHello(null);
		} catch (Exception e) {
		}
		
	}
}
