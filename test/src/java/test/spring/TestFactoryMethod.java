package test.spring;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFactoryMethod {
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private static TestFactoryMethod instance;
	
	public static TestFactoryMethod getInstance() {
		if (instance == null) {
			instance = new TestFactoryMethod();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		String path = TestFactoryMethod.class.getPackage().getName().replaceAll("\\.", "/") + File.separator + "context.xml";
		new ClassPathXmlApplicationContext(path);
		
		System.out.println(TestFactoryMethod.getInstance().getText());
	}
}
