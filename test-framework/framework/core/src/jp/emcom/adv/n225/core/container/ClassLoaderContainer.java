package jp.emcom.adv.n225.core.container;

import java.net.URL;

import jp.emcom.adv.n225.core.start.Classpath;
import jp.emcom.adv.n225.core.util.CachedClassLoader;

/**
 * 
 * @author zhangyf
 * 
 */
public class ClassLoaderContainer implements Container {

	protected static CachedClassLoader cl = null;

	/**
     * 
     */
	public void init(String configFile) throws Exception {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = Classpath.class.getClassLoader();
		}
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}

		cl = new CachedClassLoader(new URL[0], parent);
		Thread.currentThread().setContextClassLoader(cl);
	}

	/**
     * 
     */
	public boolean start() throws Exception {
		return true;
	}

	/**
     * 
     */
	public void stop() throws Exception {
	}

	public static ClassLoader getClassLoader() {
		if (cl != null) {
			return cl;
		} else {
			return ClassLoader.getSystemClassLoader();
		}
	}
}
