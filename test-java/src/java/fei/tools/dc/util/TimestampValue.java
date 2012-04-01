package fei.tools.dc.util;

import java.sql.Timestamp;

import org.springframework.beans.factory.FactoryBean;

public class TimestampValue implements FactoryBean {
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Object getObject() throws Exception {
		return Timestamp.valueOf(value);
	}

	@Override
	public Class<?> getObjectType() {
		return Timestamp.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
