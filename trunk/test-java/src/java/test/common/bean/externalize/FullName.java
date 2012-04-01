package test.common.bean.externalize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FullName implements Externalizable {
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

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		System.out.println("writeExternal...");
		
		out.writeObject(firstName);
		out.writeObject(middleName);
		out.writeObject(lastName);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		System.out.println("readExternal...");
		
		firstName = (String) in.readObject();
		middleName = (String) in.readObject();
		lastName = (String) in.readObject();
	}
}
