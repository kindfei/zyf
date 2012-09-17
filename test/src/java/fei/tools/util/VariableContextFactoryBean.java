package fei.tools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class VariableContextFactoryBean implements FactoryBean {
	private String[] configLocations;
	
	private Collection<SystemPropertyOptions> optionsBeans;
	
	private List<AbstractApplicationContext> contexts = new ArrayList<AbstractApplicationContext>();

	@Override
	public Object getObject() throws Exception {
		for (SystemPropertyOptions options : optionsBeans) {
			options.select();
		}
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		contexts.add(context);
		return context;
	}

	@Override
	public Class<?> getObjectType() {
		return ApplicationContext.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	public void close() {
		for (AbstractApplicationContext context : contexts) {
			context.close();
		}
	}

	public String[] getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(String[] configLocations) {
		this.configLocations = configLocations;
	}

	public Collection<SystemPropertyOptions> getOptionsBeans() {
		return optionsBeans;
	}

	public void setOptionsBeans(Collection<SystemPropertyOptions> optionsBeans) {
		this.optionsBeans = optionsBeans;
	}

}
