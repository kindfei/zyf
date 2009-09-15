package com.emcom.jtools.internal;

/**
 * A static utility class used across the project.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 */
public class Util {
	/**
	 * Determines if a string is an invalid string.
	 * 
	 * @param string
	 *            the input to be tested.
	 * @return <tt>true</tt> if invalid.
	 */
	public static boolean isNullString(String string) {
		return string == null || string.equals("");
	}

	/**
	 * Retrieves the default toString JavaDoc.
	 * 
	 * @return a <tt>String</tt> containing the JavaDoc.
	 */
	public static String getDefaultToStringJavaDoc() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.TOSTRING_TAG,
				ConfigurationConstants.JAVADOC_TAG);
	}

	/**
	 * Retrieves the default toString implementation.
	 * 
	 * @return a <tt>String</tt> containing the implementation.
	 */
	public static String getDefaultToStringImplementation() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.TOSTRING_TAG,
				ConfigurationConstants.IMPLEMENTATION_TAG);
	}

	/**
	 * Retrieves the default toString implementation based on the type.
	 * 
	 * @param type
	 *            a <tt>String</tt> containing the type.
	 * @return a <tt>String</tt> containing the implementation.
	 */
	public static String getDefaultToStringImplementation(String type) {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.TOSTRING_TAG,
				ConfigurationConstants.IMPLEMENTATION_TAG, type);
	}

	/**
	 * Retrieves the default externalizable JavaDoc.
	 * 
	 * @return a <tt>String</tt> containing the JavaDoc.
	 */
	public static String getDefaultExternalizableJavaDoc() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.EXTERNALIZABLE_TAG,
				ConfigurationConstants.JAVADOC_TAG);
	}

	/**
	 * Retrieves the default externalizable implementation.
	 * 
	 * @return a <tt>String</tt> containing the implementation.
	 */
	public static String getDefaultExternalizableImplementation() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.EXTERNALIZABLE_TAG,
				ConfigurationConstants.IMPLEMENTATION_TAG);
	}

	/**
	 * Retrieves the default enumeration JavaDoc.
	 * 
	 * @return a <tt>String</tt> containing the JavaDoc.
	 */
	public static String getDefaultEnumerationJavaDoc() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.ENUMERATION_TAG,
				ConfigurationConstants.JAVADOC_TAG);
	}

	/**
	 * Retrieves the default externalizable implementation.
	 * 
	 * @return a <tt>String</tt> containing the implementation.
	 */
	public static String getDefaultEnumerationImplementation() {
		return ConfigurationXMLParser.retrieveTagContents(ConfigurationConstants.ENUMERATION_TAG,
				ConfigurationConstants.IMPLEMENTATION_TAG);
	}

	/**
	 * Tests if child is a part of parent.
	 * 
	 * @param parent
	 *            the parent <tt>String</tt>.
	 * @param child
	 *            the child <tt>String</tt>.
	 * @return <tt>true</tt> if containment is found.
	 */
	public static boolean contains(String parent, String child) {
		return parent != null && child != null && parent.indexOf(child) > -1;
	}
}
