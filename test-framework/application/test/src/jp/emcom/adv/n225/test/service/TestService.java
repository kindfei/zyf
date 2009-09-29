package jp.emcom.adv.n225.test.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestService {
	private TestService parent;
	private String name;

	public void setName(String name) {
		this.name = name;
	}
	public void setParent(TestService parent) {
		this.parent = parent;
	}


	public TestService getParent() {
		return parent;
	}

	public String sayHello() {
		return "Hello " + name;
	}

	public String sayHello(String name) {
		return "Hello " + name;
	}

	public String sayHi() {
		return "Hi " + name;
	}

	public String sayHi(String name) {
		return "Hi " + name;
	}

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext("E:/workspaces/workspace-zyf/test-framework/application/test/service/services.xml");

		TestService t = (TestService) context.getBean("sayHiAdapter");

		System.out.println("result: " + t.sayHello());
		System.out.println("result: " + t.sayHello("AOP"));
		System.out.println("result: " + t.sayHi());
		System.out.println("result: " + t.sayHi("AOP"));
	}
}
