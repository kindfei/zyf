package test.jms.jboss;

public class ConnectException extends Exception {
	private static final long serialVersionUID = -381686124151450713L;
	private String failHost;
	
	public ConnectException(String message, Throwable cause, String failHost) {
		super(message, cause);
		this.failHost = failHost;
	}

	public String getFailHost() {
		return failHost;
	}

	public void setFailHost(String failHost) {
		this.failHost = failHost;
	}
}
