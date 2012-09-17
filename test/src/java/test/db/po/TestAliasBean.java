package test.db.po;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TestAliasBean {
	private String content;
	private Timestamp date_time;
	private int id;
	private int version;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getDate_time() {
		return date_time;
	}
	public void setDate_time(Timestamp date_time) {
		this.date_time = date_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("content", content);
		builder.append("date_time", date_time);
		builder.append("id", id);
		builder.append("version", version);
		return builder.toString();
	}
}