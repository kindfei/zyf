package test.spring.prop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.util.Utils;


public class Test {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = Utils.loadContext(Test.class, "context.xml");
		
		System.out.println(c.getBean("str1"));
		System.out.println(c.getBean("str2"));
	}
}
