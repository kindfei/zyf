package test.common.bean.serialize;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FullName implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String middleName;
	private String lastName;
	
	public FullName() {
	}
	
	public FullName(String firstName, String middleName, String lastName) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
