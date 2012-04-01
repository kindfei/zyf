package test.quartz;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import test.util.Utils;


public class TestQuartz {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = Utils.loadContext(TestQuartz.class, "context.xml");
		
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.close();
	}
}
