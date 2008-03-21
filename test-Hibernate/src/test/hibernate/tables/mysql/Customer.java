package test.hibernate.tables.mysql;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
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

    /** nullable persistent field */
    private test.hibernate.tables.mysql.Group GroupBean;

    /** persistent field */
    private Map emailMap;

    /** persistent field */
    private Map productBeanMap;

    /** persistent field */
    private Set emailSet;

    /** persistent field */
    private Set emailBeanSet;

    /** persistent field */
    private test.hibernate.tables.mysql.Product[] productBeans;

    /** full constructor */
    public Customer(String customerId, String customerName, String address, String groupId, test.hibernate.tables.mysql.Group GroupBean, Map emailMap, Map productBeanMap, Set emailSet, Set emailBeanSet, test.hibernate.tables.mysql.Product[] productBeans) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.groupId = groupId;
        this.GroupBean = GroupBean;
        this.emailMap = emailMap;
        this.productBeanMap = productBeanMap;
        this.emailSet = emailSet;
        this.emailBeanSet = emailBeanSet;
        this.productBeans = productBeans;
    }

    /** default constructor */
    public Customer() {
    }

    /** minimal constructor */
    public Customer(String customerId, Map emailMap, Map productBeanMap, Set emailSet, Set emailBeanSet, test.hibernate.tables.mysql.Product[] productBeans) {
        this.customerId = customerId;
        this.emailMap = emailMap;
        this.productBeanMap = productBeanMap;
        this.emailSet = emailSet;
        this.emailBeanSet = emailBeanSet;
        this.productBeans = productBeans;
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

    public test.hibernate.tables.mysql.Group getGroupBean() {
        return this.GroupBean;
    }

    public void setGroupBean(test.hibernate.tables.mysql.Group GroupBean) {
        this.GroupBean = GroupBean;
    }

    public Map getEmailMap() {
        return this.emailMap;
    }

    public void setEmailMap(Map emailMap) {
        this.emailMap = emailMap;
    }

    public Map getProductBeanMap() {
        return this.productBeanMap;
    }

    public void setProductBeanMap(Map productBeanMap) {
        this.productBeanMap = productBeanMap;
    }

    public Set getEmailSet() {
        return this.emailSet;
    }

    public void setEmailSet(Set emailSet) {
        this.emailSet = emailSet;
    }

    public Set getEmailBeanSet() {
        return this.emailBeanSet;
    }

    public void setEmailBeanSet(Set emailBeanSet) {
        this.emailBeanSet = emailBeanSet;
    }

    public test.hibernate.tables.mysql.Product[] getProductBeans() {
        return this.productBeans;
    }

    public void setProductBeans(test.hibernate.tables.mysql.Product[] productBeans) {
        this.productBeans = productBeans;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("customerId", getCustomerId())
            .toString();
    }

}
