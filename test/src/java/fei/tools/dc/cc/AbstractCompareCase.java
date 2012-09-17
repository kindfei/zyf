package fei.tools.dc.cc;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import fei.tools.dc.da.QueryInfo;




public abstract class AbstractCompareCase implements CompareCase {
	
	private QueryInfo queryInfo1;
	
	private QueryInfo queryInfo2;
	
	private String dataAccessorName;
	
	public QueryInfo getQueryInfo1() {
		return queryInfo1;
	}
	public void setQueryInfo1(QueryInfo queryInfo1) {
		this.queryInfo1 = queryInfo1;
	}
	
	public QueryInfo getQueryInfo2() {
		return queryInfo2;
	}
	public void setQueryInfo2(QueryInfo queryInfo2) {
		this.queryInfo2 = queryInfo2;
	}

	public String getDataAccessorName() {
		return dataAccessorName;
	}
	public void setDataAccessorName(String dataAccessorName) {
		this.dataAccessorName = dataAccessorName;
	}
	
	public void setSql(String sql) {
		setSql1(sql);
		setSql2(sql);
	}
	public void setSql1(String sql) {
		if (queryInfo1 == null) queryInfo1 = new QueryInfo();
		queryInfo1.setSql(sql);
	}
	public void setSql2(String sql) {
		if (queryInfo2 == null) queryInfo2 = new QueryInfo();
		queryInfo2.setSql(sql);
	}
	
	public void setParameters(List<Object> parameters) {
		setParameters1(parameters);
		setParameters2(parameters);
	}
	public void setParameters1(List<Object> parameters) {
		if (queryInfo1 == null) queryInfo1 = new QueryInfo();
		queryInfo1.setParameters(parameters);
	}
	public void setParameters2(List<Object> parameters) {
		if (queryInfo2 == null) queryInfo2 = new QueryInfo();
		queryInfo2.setParameters(parameters);
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("queryInfo1", queryInfo1);
		builder.append("queryInfo2", queryInfo2);
		builder.append("dataAccessorName", dataAccessorName);
		return builder.toString();
	}
}
