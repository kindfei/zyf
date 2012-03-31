package test.spring3;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpEL {

	public static void main(String[] args) {
		ApplicationContext c = new ClassPathXmlApplicationContext("testContext.xml");
		Map<String, TestSpEL> beans = c.getBeansOfType(TestSpEL.class);
		
		for (Map.Entry<String, TestSpEL> b : beans.entrySet()) {
			System.out.println(b.getKey() + " = " + b.getValue().toString());
		}
	}
	
	private String stringProp;
	private List<String> listProp;
	private String[] arrayProp;
	private Map<String, String> mapProp;
	
	public String getStringProp() {
		return stringProp;
	}

	public void setStringProp(String stringProp) {
		this.stringProp = stringProp;
	}

	public List<String> getListProp() {
		return listProp;
	}

	public void setListProp(List<String> listProp) {
		this.listProp = listProp;
	}

	public String[] getArrayProp() {
		return arrayProp;
	}

	public void setArrayProp(String[] arrayProp) {
		this.arrayProp = arrayProp;
	}

	public Map<String, String> getMapProp() {
		return mapProp;
	}

	public void setMapProp(Map<String, String> mapProp) {
		this.mapProp = mapProp;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
