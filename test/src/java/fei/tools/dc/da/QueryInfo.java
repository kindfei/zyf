package fei.tools.dc.da;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class QueryInfo {
	
	private String sql;
	
	private List<Object> parameters;
	
	public QueryInfo() {
	}
	
	public QueryInfo(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("sql", sql);
		builder.append("parameters", parameters);
		return builder.toString();
	}
}
