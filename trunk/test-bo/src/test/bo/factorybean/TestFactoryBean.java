package test.bo.factorybean;

import jp.emcom.adv.bo.test.ContextLoader;

import org.springframework.beans.factory.FactoryBean;

public class TestFactoryBean implements FactoryBean {
	private boolean singleton;

	@Override
	public Object getObject() throws Exception {
		return new Object();
	}

	@Override
	public Class<Object> getObjectType() {
		return Object.class;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(TestFactoryBean.class);
		
		Object test1 = (Object) loader.getBean("test");
		Object test2 = (Object) loader.getBean("test");
		
		System.out.println(test1 == test2);
	}
}
