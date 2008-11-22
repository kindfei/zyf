package test.core.jms;

public class MessageException extends Exception {

	private static final long serialVersionUID = 8442916737080173384L;
	
	public MessageException(String message) {
		super(message);
	}
	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}
}
