package fei.tools.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyOptionSelector implements BeanFactoryPostProcessor {
	private Map<String, List<String>> options;
	
	public Map<String, List<String>> getOptions() {
		return options;
	}

	public void setOptions(Map<String, List<String>> options) {
		this.options = options;
	}
	
	public String select(String key) {
		List<String> option = options.get(key);
		return (String) InteractionHandler.selectOption("Select a value for [" + key + "]", option.toArray());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Properties porp = new Properties();
		for (String key : options.keySet()) {
			porp.setProperty(key, select(key));
		}
		
		Map<String, PropertyPlaceholderConfigurer> configurers = beanFactory.getBeansOfType(PropertyPlaceholderConfigurer.class);
		for (PropertyPlaceholderConfigurer e : configurers.values()) {
			e.setProperties(porp);
			e.postProcessBeanFactory(beanFactory);
		}
	}
}
