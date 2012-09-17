package fei.tools.mh;

import org.springframework.beans.factory.FactoryBean;

public class PubSubDomainValue implements FactoryBean {
	private String model;
	private String topicValue;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTopicValue() {
		return topicValue;
	}

	public void setTopicValue(String topicValue) {
		this.topicValue = topicValue;
	}

	@Override
	public Boolean getObject() throws Exception {
		return model.equalsIgnoreCase(topicValue);
	}

	@Override
	public Class<?> getObjectType() {
		return Boolean.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
