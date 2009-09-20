package jp.emcom.adv.n225.core.config;

import jp.emcom.adv.n225.core.util.GeneralException;

public class GenericConfigException extends GeneralException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 816582694592444789L;

	public GenericConfigException() {
        super();
    }

    public GenericConfigException(String str) {
        super(str);
    }

    public GenericConfigException(Throwable nested) {
        super(nested);
    }

    public GenericConfigException(String str, Throwable nested) {
        super(str, nested);
    }
}
