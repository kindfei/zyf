package test.jms.jboss.core;

public class FailoverException extends Exception {
	private static final long serialVersionUID = 6389429919983030179L;

	public FailoverException(String message, Throwable cause) {
		super(message, cause);
	}

}
