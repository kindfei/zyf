package test.common.bean.serialize.override;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Name implements Serializable {
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
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		System.out.println("writeObject...");

		out.writeObject(firstName);
		out.writeObject(lastName);
		
//		out.defaultWriteObject();
	}
 
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		System.out.println("readObject...");

		firstName = (String) in.readObject();
		lastName = (String) in.readObject();
		
//		in.defaultReadObject();
	}
}
