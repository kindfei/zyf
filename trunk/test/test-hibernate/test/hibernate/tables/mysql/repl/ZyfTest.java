package test.hibernate.tables.mysql.repl;

import java.io.Serializable;
import java.util.Date;


/** @author Hibernate CodeGenerator */
public class ZyfTest implements Serializable {

    /** identifier field */
    private String testId;

    /** identifier field */
    private String testName;

    /** identifier field */
    private Date inputDate;

    /** full constructor */
    public ZyfTest(String testId, String testName, Date inputDate) {
        this.testId = testId;
        this.testName = testName;
        this.inputDate = inputDate;
    }

    /** default constructor */
    public ZyfTest() {
    }

    public String getTestId() {
        return this.testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return this.testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getInputDate() {
        return this.inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

}
