package jp.emcom.adv.n225.core.component;

/**
 * 
 * @author alex
 *
 */
public class AlreadyLoadedException extends ComponentException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8894825524902211570L;

	public AlreadyLoadedException() {
        super();
    }

    public AlreadyLoadedException(String str) {
        super(str);
    }

    public AlreadyLoadedException(Throwable nested) {
        super(nested);
    }

    public AlreadyLoadedException(String str, Throwable nested) {
        super(str, nested);
    }
}

