package fei.tools.dc.cc;

import java.util.List;
import java.util.Map;

public class FetchResult {
	
	private String envName;
	private List<Map<String, Object>> records;
	
	public FetchResult() {
		super();
	}
	
	public FetchResult(String envName, List<Map<String, Object>> records) {
		super();
		this.envName = envName;
		this.records = records;
	}
	
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public List<Map<String, Object>> getRecords() {
		return records;
	}
	public void setRecords(List<Map<String, Object>> records) {
		this.records = records;
	}
	
}
