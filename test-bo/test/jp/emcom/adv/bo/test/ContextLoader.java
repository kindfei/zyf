package jp.emcom.adv.bo.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContextLoader {
	private AbstractApplicationContext context;
	
	public ContextLoader() {
		context = new ClassPathXmlApplicationContext(parse(getClass().getName()));
	}
	
	public ContextLoader(Class<?> clz) {
		context = new ClassPathXmlApplicationContext(parse(clz.getName()));
	}
	
	public ContextLoader(String[] files) {
		try {
			context = new ClassPathXmlApplicationContext(files);
		} catch (BeansException e) {
			context = new FileSystemXmlApplicationContext(files);
		}
	}
	
	private String parse(String className) {
		return className.replaceAll("\\.", "/") + ".xml";
	}
	
	public Object getBean(String name) {
		return context.getBean(name);
	}
	
	public void close() {
		context.close();
	}
	
	public ApplicationContext getContext() {
		return context;
	}
}
