package test.hibernate.tables.mysql;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Group implements Serializable {

    /** identifier field */
    private String groupId;

    /** nullable persistent field */
    private Integer rebate;

    /** full constructor */
    public Group(String groupId, Integer rebate) {
        this.groupId = groupId;
        this.rebate = rebate;
    }

    /** default constructor */
    public Group() {
    }

    /** minimal constructor */
    public Group(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getRebate() {
        return this.rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupId", getGroupId())
            .toString();
    }

}
