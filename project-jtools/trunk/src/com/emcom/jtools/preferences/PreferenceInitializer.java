package com.emcom.jtools.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import com.emcom.jtools.JToolsPlugin;

/**
 * Class used to initialize default preference values.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	/**
	 * {@inheritDoc}
	 */
	public void initializeDefaultPreferences() {
		JToolsPlugin.getDefault().getPreferenceStore();
	}
}
