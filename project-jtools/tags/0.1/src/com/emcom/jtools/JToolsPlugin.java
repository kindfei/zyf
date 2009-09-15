package com.emcom.jtools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 */
public class JToolsPlugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "com.emcom.jtools";

	/**
	 * The shared instance.
	 */
	private static JToolsPlugin plugin;

	/**
	 * Resource bundle.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * Constructs the plugin and initializes the resource bundle.
	 */
	public JToolsPlugin() {
		super();

		plugin = this;

		try {
			resourceBundle = ResourceBundle.getBundle("com.emcom.jtools.JToolsPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return an instance of <tt>JToolsPlugin</tt>.
	 */
	public static JToolsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 * 
	 * @return a <tt>String</tt> containing the value.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = JToolsPlugin.getDefault().getResourceBundle();

		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle.
	 * 
	 * @return an instance of <tt>ResourceBundle</tt>.
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
