package jp.emcom.adv.n225.test.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestService {
	public String sayHello(String name) {
		return name + " Hello!";
	}
	
	public String sayHi(String name) {
		return name + " Hi!";
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("D:/workspaces/workspace-zyf/test-framework/application/test/service/services.xml");
		
		TestService t = (TestService) context.getBean("sayHelloAdapter");
		
		System.out.println("result: " + t.sayHello("AOP"));
		System.out.println("result: " + t.sayHi("AOP"));
	}
}
