package test.common.bean.serialize;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Name name;
	private Integer age;
	private Double height;
	private Boolean married;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Boolean getMarried() {
		return married;
	}

	public void setMarried(Boolean married) {
		this.married = married;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
