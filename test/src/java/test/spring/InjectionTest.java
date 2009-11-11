package test.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InjectionTest {
	private String[] strs;
	private List<String> list = new ArrayList<String>();
	private Map<String,String> map = new HashMap<String, String>();
	
	public void setStrs(String[] strs) {
		this.strs = strs;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("strs", strs);
		tsb.append("list", list.toArray());
		tsb.append("map", map.entrySet().toArray());
		return tsb.toString();
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("test/spring/InjectionTest.xml");
		
		InjectionTest t = (InjectionTest) c.getBean("injectionTest");
		
		System.out.println(t.toString());
	}
}
