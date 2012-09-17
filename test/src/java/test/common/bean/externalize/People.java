package test.common.bean.externalize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang.builder.ToStringBuilder;

public class People implements Externalizable {
	private static final long serialVersionUID = 1L;
	
	private FullName name;
	private Integer age;
	private Double height;
	private Boolean married;
	private String address;

	public FullName getName() {
		return name;
	}

	public void setName(FullName name) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		System.out.println("writeExternal...");
		
		out.writeObject(name);
		out.writeObject(age);
		out.writeObject(height);
		out.writeObject(married);
		out.writeObject(address);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		System.out.println("readExternal...");
		
		name = (FullName) in.readObject();
		age = (Integer) in.readObject();
		height = (Double) in.readObject();
		married = (Boolean) in.readObject();
		address = (String) in.readObject();
	}
	
}
