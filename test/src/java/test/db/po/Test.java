package test.db.po;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.sql.Timestamp;


/**
 * The persistent class for the test database table.
 * 
 */
@Entity
@Table(name="test")
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;

	private String content;

	@Column(name="date_time")
	private Timestamp dateTime;

	@Id
	private int id;

	private int version;

    public Test() {
    }

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("content", content);
		builder.append("dateTime", dateTime);
		builder.append("id", id);
		builder.append("version", version);
		return builder.toString();
	}
}