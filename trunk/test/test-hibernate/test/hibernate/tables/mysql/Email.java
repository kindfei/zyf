package test.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Email implements Serializable {

    /** identifier field */
    private String emailId;

    /** nullable persistent field */
    private String customerId;

    /** nullable persistent field */
    private String email;

    /** full constructor */
    public Email(String emailId, String customerId, String email) {
        this.emailId = emailId;
        this.customerId = customerId;
        this.email = email;
    }

    /** default constructor */
    public Email() {
    }

    /** minimal constructor */
    public Email(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("emailId", getEmailId())
            .toString();
    }

}
