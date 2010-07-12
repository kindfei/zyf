package jp.emcom.adv.bo.test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextLoader {
	private AbstractApplicationContext context;
	
	public ContextLoader() {
		context = new ClassPathXmlApplicationContext(parse(getClass().getName()));
	}
	
	public ContextLoader(Class<?> clz) {
		context = new ClassPathXmlApplicationContext(parse(clz.getName()));
	}
	
	public ContextLoader(String[] files) {
		context = new ClassPathXmlApplicationContext(files) {
			protected DefaultListableBeanFactory createBeanFactory() {
				DefaultListableBeanFactory f = super.createBeanFactory();
				f.setAllowBeanDefinitionOverriding(true);
				return f;
			}
		};
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
