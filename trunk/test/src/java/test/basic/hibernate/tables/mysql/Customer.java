package test.basic.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Customer implements Serializable {

    /** identifier field */
    private String customerId;

    /** nullable persistent field */
    private String customerName;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String groupId;

    /** full constructor */
    public Customer(String customerId, String customerName, String address, String groupId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.groupId = groupId;
    }

    /** default constructor */
    public Customer() {
    }

    /** minimal constructor */
    public Customer(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("customerId", getCustomerId())
            .toString();
    }

}
