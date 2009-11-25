package test.basic.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class CustomerProduct implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String customerId;

    /** nullable persistent field */
    private String productId;

    /** full constructor */
    public CustomerProduct(Integer id, String customerId, String productId) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
    }

    /** default constructor */
    public CustomerProduct() {
    }

    /** minimal constructor */
    public CustomerProduct(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
