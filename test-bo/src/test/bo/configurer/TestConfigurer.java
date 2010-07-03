package test.bo.configurer;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import jp.emcom.adv.bo.test.ContextLoader;

public class TestConfigurer {
	private String p1;
	private String p2;
	private String p3;
	private List<String> props;
	private TestConfigurer info;
	
	public TestConfigurer() {
		
	}
	
	public TestConfigurer(TestConfigurer info) {
		this.info = info;
	}
	
	public void setP1(String p1) {
		this.p1 = p1;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public void setProps(List<String> props) {
		this.props = props;
	}

	public List<String> getProps() {
		return props;
	}

	public TestConfigurer getInfo() {
		return info;
	}

	public void setInfo(TestConfigurer info) {
		this.info = info;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("p1", p1);
		builder.append("p2", p2);
		builder.append("p3", p3);
		builder.append("props", props);
		builder.append("info", info);
		return builder.toString();
	}

	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(new String[] {
				"test/bo/configurer/TestConfigurer1.xml",
				"test/bo/configurer/TestConfigurer2.xml",
		});
		
		TestConfigurer test = (TestConfigurer) loader.getBean("test");
		System.out.println(test.toString());
	}
}
