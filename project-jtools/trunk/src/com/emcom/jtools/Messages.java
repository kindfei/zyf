package com.emcom.jtools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Acts as a helper for fetching messages from the externalized resource file.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 10th December 2006
 */
public class Messages {
	/**
	 * The bundle name.
	 */
	private static final String BUNDLE_NAME = "com.emcom.jtools.messages";

	/**
	 * The <tt>ResourceBundle</tt> associated with the bundle.
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Fetches the string property given the key.
	 * 
	 * @param key
	 *            the key.
	 * 
	 * @return a <tt>String</tt> containing the value of the key.
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
