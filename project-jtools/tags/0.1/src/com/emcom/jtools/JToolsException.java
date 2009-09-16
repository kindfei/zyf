package com.emcom.jtools;

/**
 * A JTools specific runtime exception.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 10th December 2006
 */
public class JToolsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3910659982938746497L;

	/**
	 * Constructs a <tt>JToolsException</tt> instance.
	 */
	public JToolsException() {
		super();
	}

	/**
	 * Constructs a <tt>JToolsException</tt> instance using the message.
	 * 
	 * @param message
	 *            the message to wrap over.
	 */
	public JToolsException(String message) {
		super(message);
	}

	/**
	 * Constructs a <tt>JToolsException</tt> instance using the
	 * <tt>Throwable</tt> instance.
	 * 
	 * @param throwable
	 *            the <tt>Throwable</tt> instance to wrap over.
	 */
	public JToolsException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs a <tt>JToolsException</tt> instance using the message and the
	 * <tt>Throwable</tt> instance.
	 * 
	 * @param message
	 *            the message for the exception.
	 * 
	 * @param throwable
	 *            the <tt>Throwable</tt> instance to wrap over.
	 */
	public JToolsException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
