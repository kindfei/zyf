package test.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class JmsProvider implements Serializable {

    /** identifier field */
    private String providerId;

    /** persistent field */
    private String providerType;

    /** persistent field */
    private int manualHa;

    /** persistent field */
    private String url;

    /** nullable persistent field */
    private String user;

    /** nullable persistent field */
    private String password;

    /** full constructor */
    public JmsProvider(String providerId, String providerType, int manualHa, String url, String user, String password) {
        this.providerId = providerId;
        this.providerType = providerType;
        this.manualHa = manualHa;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /** default constructor */
    public JmsProvider() {
    }

    /** minimal constructor */
    public JmsProvider(String providerId, String providerType, int manualHa, String url) {
        this.providerId = providerId;
        this.providerType = providerType;
        this.manualHa = manualHa;
        this.url = url;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderType() {
        return this.providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public int getManualHa() {
        return this.manualHa;
    }

    public void setManualHa(int manualHa) {
        this.manualHa = manualHa;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("providerId", getProviderId())
            .toString();
    }

}
