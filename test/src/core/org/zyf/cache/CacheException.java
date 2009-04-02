package org.zyf.cache;

public class CacheException extends Exception {

	private static final long serialVersionUID = -5423456726522328137L;

	public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
