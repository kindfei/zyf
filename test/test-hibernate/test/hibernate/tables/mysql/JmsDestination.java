package test.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class JmsDestination implements Serializable {

    /** identifier field */
    private String destinationName;

    /** persistent field */
    private String providerId;

    /** full constructor */
    public JmsDestination(String destinationName, String providerId) {
        this.destinationName = destinationName;
        this.providerId = providerId;
    }

    /** default constructor */
    public JmsDestination() {
    }

    public String getDestinationName() {
        return this.destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("destinationName", getDestinationName())
            .toString();
    }

}
