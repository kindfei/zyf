package jp.emcom.adv.n225.core.component;

import jp.emcom.adv.n225.core.config.GenericConfigException;

/**
 * 
 * @author alex
 *
 */
public class ComponentException extends GenericConfigException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7532803602734549703L;

	public ComponentException() {
        super();
    }

    public ComponentException(String str) {
        super(str);
    }

    public ComponentException(Throwable nested) {
        super(nested);
    }

    public ComponentException(String str, Throwable nested) {
        super(str, nested);
    }
}