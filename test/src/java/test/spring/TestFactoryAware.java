package test.spring;

import java.io.File;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFactoryAware implements BeanFactoryAware {

	private static TestFactoryAware instance = new TestFactoryAware();
	
	public static TestFactoryAware getInstance() {
		return instance;
	}
	
	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public Object getBean(String name) {
		return beanFactory.getBean(name);
	}
	
	public static void main(String[] args) {
		String path = TestFactoryMethod.class.getPackage().getName().replaceAll("\\.", "/") + File.separator + "context.xml";
		new ClassPathXmlApplicationContext(path);
		
		System.out.println(getInstance().getBean("str"));
	}
}
