package jp.emcom.adv.n225.core.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class GeneralException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8863864819922182234L;

	public static <T> T checkException(Throwable t) throws GeneralException {
        return GeneralException.<T>checkException(t.getMessage(), t);
    }

    public static <T> T checkException(String message, Throwable t) throws GeneralException {
        if (t instanceof Error) throw (Error) t;
        if (t instanceof RuntimeException) throw (RuntimeException) t;
        if (t instanceof GeneralException) throw (GeneralException) t;
        throw (GeneralException) new GeneralException(message).initCause(t);
    }

    List<String> messages = null;

    public GeneralException() {
        super();
    }

    public GeneralException(String msg) {
        super(msg);
    }

    public GeneralException(String msg, Throwable nested) {
        super(msg, nested);
    }

    public GeneralException(Throwable nested) {
        super(nested);
    }

    public GeneralException(String msg, List<String> messages) {
        super(msg);
        this.messages = messages;
    }

    /**
     * Constructs an <code>GeneralException</code> with the specified detail message, list and nested Exception.
     * @param msg the detail message.
     * @param messages error message list.
     * @param nested the nexted exception
     */
    public GeneralException(String msg, List<String> messages, Throwable nested) {
        super(msg, nested);
        this.messages = messages;
    }

    /**
     * Constructs an <code>GeneralException</code> with the specified detail message list and nested Exception.
     * @param messages error message list.
     * @param nested the nested exception.
     */
    public GeneralException(List<String> messages, Throwable nested) {
        super(nested);
        this.messages = messages;
    }

    public GeneralException(List<String> messages) {
        super();
        this.messages = messages;
    }

    /** Returns the detail message, including the message from the nested exception if there is one. */
    public String getMessage() {
        Throwable nested = getCause();
        if (nested != null) {
            if (super.getMessage() == null) {
                return nested.getMessage();
            } else {
                return super.getMessage() + " (" + nested.getMessage() + ")";
            }
        } else {
            return super.getMessage();
        }
    }

    public List<String> getMessageList() {
        return this.messages;
    }

    /** Returns the detail message, NOT including the message from the nested exception. */
    public String getNonNestedMessage() {
        return super.getMessage();
    }

    /** Returns the nested exception if there is one, null if there is not. */
    public Throwable getNested() {
        Throwable nested = getCause();
        if (nested == null) {
            return this;
        }
        return nested;
    }

    /** Prints the composite message to System.err. */
    public void printStackTrace() {
        super.printStackTrace();
    }

    /** Prints the composite message and the embedded stack trace to the specified stream ps. */
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
    }

    /** Prints the composite message and the embedded stack trace to the specified print writer pw. */
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
    }
}

