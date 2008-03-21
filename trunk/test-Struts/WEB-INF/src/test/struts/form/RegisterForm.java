package test.struts.form;

import org.apache.struts.action.ActionForm;

public class RegisterForm extends ActionForm {
	
	private static final long serialVersionUID = -3375363583029645928L;
	
	private String userId;
	private String email;
	private String address;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
