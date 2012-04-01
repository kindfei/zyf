package fei.tools.dc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.FactoryBean;

public class DateValue implements FactoryBean {
	private String value;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Object getObject() throws Exception {
		return sdf.parse(value);
	}

	@Override
	public Class<?> getObjectType() {
		return Date.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
