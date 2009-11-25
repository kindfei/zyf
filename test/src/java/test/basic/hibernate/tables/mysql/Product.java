package test.basic.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Product implements Serializable {

    /** identifier field */
    private String productId;

    /** nullable persistent field */
    private String productName;

    /** nullable persistent field */
    private Integer leverage;

    /** full constructor */
    public Product(String productId, String productName, Integer leverage) {
        this.productId = productId;
        this.productName = productName;
        this.leverage = leverage;
    }

    /** default constructor */
    public Product() {
    }

    /** minimal constructor */
    public Product(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getLeverage() {
        return this.leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("productId", getProductId())
            .toString();
    }

}
