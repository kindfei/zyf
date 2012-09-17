package fei.tools.dc.da;

public abstract class AbstractDataAccessor implements DataAccessor {
	private String envName;
	
	private int fetchSize;
	
	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	
}
