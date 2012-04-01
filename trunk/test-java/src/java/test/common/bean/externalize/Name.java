package test.common.bean.externalize;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Name implements Externalizable {
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	
	public Name() {
	}
	
	public Name(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
		out.writeObject(lastName);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		System.out.println("readExternal...");
		
		firstName = (String) in.readObject();
		lastName = (String) in.readObject();
	}
}
