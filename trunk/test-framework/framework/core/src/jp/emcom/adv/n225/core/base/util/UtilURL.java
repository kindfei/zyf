package jp.emcom.adv.n225.core.base.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author zhangyf
 * 
 */
public class UtilURL {

	public static URL fromResource(String resourceName) {
		return fromResource(resourceName, null);
	}

	public static URL fromResource(Class<?> contextClass, String resourceName) {
		if (contextClass == null)
			return fromResource(resourceName, null);
		else
			return fromResource(resourceName, contextClass.getClassLoader());
	}

	public static URL fromResource(String resourceName, ClassLoader loader) {
		URL url = null;

		if (loader != null && url == null)
			url = loader.getResource(resourceName);
		if (loader != null && url == null)
			url = loader.getResource(resourceName + ".properties");

		if (loader == null && url == null) {
			try {
				loader = Thread.currentThread().getContextClassLoader();
			} catch (SecurityException e) {
				UtilURL utilURL = new UtilURL();
				loader = utilURL.getClass().getClassLoader();
			}
		}

		if (url == null)
			url = loader.getResource(resourceName);
		if (url == null)
			url = loader.getResource(resourceName + ".properties");

		if (url == null)
			url = ClassLoader.getSystemResource(resourceName);
		if (url == null)
			url = ClassLoader.getSystemResource(resourceName + ".properties");

		if (url == null)
			url = fromFilename(resourceName);
		if (url == null)
			url = fromHomePath(resourceName);
		if (url == null)
			url = fromUrlString(resourceName);

		return url;
	}

	public static URL fromFilename(String filename) {
		if (filename == null)
			return null;
		File file = new File(filename);
		URL url = null;

		try {
			if (file.exists())
				url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			url = null;
		}
		return url;
	}

	public static URL fromUrlString(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
		}

		return url;
	}

	public static URL fromHomePath(String filename) {
		String home = System.getProperty("application.home");
		if (home == null) {
			return null;
		}
		String newFilename = home;
		if (!newFilename.endsWith("/") && !filename.startsWith("/")) {
			newFilename = newFilename + "/";
		}
		newFilename = newFilename + filename;
		return fromFilename(newFilename);
	}
}
