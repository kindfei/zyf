package com.emcom.jtools.preferences;

import com.emcom.jtools.Messages;

/**
 * Contains all constants related to the preferences.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 * 
 * @version 3.0, 10th December 2006
 */
public class PreferenceConstants {
	/**
	 * Indicates the bundle name for preferences
	 */
	public static final String PREFERENCE_MESSAGES_BUNDLE = "org.adarsh.jtools.preferences.preference_messages";

	/**
	 * The java partitioning string.
	 */
	public static final String PARTITIONING = "___java_partitioning";

	/**
	 * The source viewer width.
	 */
	public static final int WIDTH = 3;

	/**
	 * The source viewer height.
	 */
	public static final int HEIGHT = 5;

	/**
	 * The source viewer style.
	 */
	public static final int SOURCEVIEWER_STYLE = 2816;

	/**
	 * The source viewer grid style.
	 */
	public static final int GRID_STYLE = 1296;

	/**
	 * The source viewer java font.
	 */
	public static final String JAVA_FONT = "org.eclipse.jdt.ui.editors.textfont";

	/**
	 * <tt>JTools</tt> preference page description.
	 */
	public static final String JTOOLS_PAGE_DESCRIPTION = Messages.getString("preferences.jtools.page.description");

	/**
	 * Represents attribute.
	 */
	public static final String ATTRIBUTE = Messages.getString("preferences.attribute");

	/**
	 * Represents attribute description.
	 */
	public static final String ATTRIBUTE_DESCRIPTION = Messages.getString("preferences.attribute.description");

	/**
	 * Represents class instance.
	 */
	public static final String CLASS_INSTANCE = Messages.getString("preferences.class.instance");

	/**
	 * Represents class instance description.
	 */
	public static final String CLASS_INSTANCE_DESCRIPTION = Messages
			.getString("preferences.class.instance.description");

	/**
	 * Represents class name.
	 */
	public static final String CLASS_NAME = Messages.getString("preferences.class.name");

	/**
	 * Represents class name description.
	 */
	public static final String CLASS_NAME_DESCRIPTION = Messages.getString("preferences.class.name.description");

	/**
	 * <tt>JTools -> toString()</tt> preference page description.
	 */
	public static final String TOSTRING_PAGE_DESCRIPTION = Messages.getString("preferences.tostring.page.description");

	/**
	 * <tt>JTools -> externalizable</tt> preference page description.
	 */
	public static final String EXTERNALIZABLE_PAGE_DESCRIPTION = Messages.getString("preferences.externalizable.page.description");

	/**
	 * <tt>JTools -> enumeration</tt> preference page description.
	 */
	public static final String ENUMERATION_PAGE_DESCRIPTION = Messages.getString("preferences.enumeration.page.description");

	/**
	 * Label for JavaDoc.
	 */
	public static final String JAVADOC_LABEL = Messages.getString("preferences.javadoc");

	/**
	 * Label for method body.
	 */
	public static final String METHOD_BODY_LABEL = Messages.getString("preferences.method.body");

	/**
	 * Label for externalizable body.
	 */
	public static final String EXTERNALIZABLE_BODY_LABEL = Messages.getString("preferences.externalizable.body");

	/**
	 * Label for enumeration body.
	 */
	public static final String ENUMERATION_BODY_LABEL = Messages.getString("preferences.enumeration.body");

	/**
	 * Store key for toString JavaDoc.
	 */
	public static final String TOSTRING_JAVADOC_STORE_KEY = "tostring.javadoc";

	/**
	 * Store key for toString body.
	 */
	public static final String TOSTRING_BODY_STORE_KEY = "tostring.body";

	/**
	 * Store key for externalizable JavaDoc.
	 */
	public static final String EXTERNALIZABLE_JAVADOC_STORE_KEY = "externalizable.javadoc";

	/**
	 * Store key for externalizable body.
	 */
	public static final String EXTERNALIZABLE_BODY_STORE_KEY = "externalizable.body";

	/**
	 * Store key for enumeration JavaDoc.
	 */
	public static final String ENUMERATION_JAVADOC_STORE_KEY = "enumeration.javadoc";

	/**
	 * Store key for externalizable body.
	 */
	public static final String ENUMERATION_BODY_STORE_KEY = "enumeration.body";

	/**
	 * Represents the mode selection radio control.
	 */
	public static final String MODE = "mode";

	// /// Check Boxes End /////

	/**
	 * Represents the sort check box for toString.
	 */
	public static final String TOSTRING_SORT = "tostring.sort";

	/**
	 * Represents the overwrite check box for toString.
	 */
	public static final String TOSTRING_OVERWRITE = "tostring.overwrite";

	/**
	 * Represents the autosave check box for toString.
	 */
	public static final String TOSTRING_AUTOSAVE = "tostring.autosave";

	/**
	 * Represents the sort check box for externalizable.
	 */
	public static final String EXTERNALIZABLE_SORT = "externalizable.sort";

	/**
	 * Represents the overwrite check box for externalizable.
	 */
	public static final String EXTERNALIZABLE_OVERWRITE = "externalizable.overwrite";

	/**
	 * Represents the autosave check box for externalizable.
	 */
	public static final String EXTERNALIZABLE_AUTOSAVE = "externalizable.autosave";

	/**
	 * Represents the autosave check box for enumeration.
	 */
	public static final String ENUMERATION_AUTOSAVE = "enumeration.autosave";

	/**
	 * Represents the sort check box label for toString.
	 */
	public static final String TOSTRING_SORT_LABEL = Messages.getString("preferences.tostring.sort");

	/**
	 * Represents the overwrite check box label for toString.
	 */
	public static final String TOSTRING_OVERWRITE_LABEL = Messages.getString("preferences.tostring.overwrite");

	/**
	 * Represents the autosave check box label for toString.
	 */
	public static final String TOSTRING_AUTOSAVE_LABEL = Messages.getString("preferences.tostring.autosave");

	/**
	 * Represents the sort check box label for externalizable.
	 */
	public static final String EXTERNALIZABLE_SORT_LABEL = Messages.getString("preferences.externalizable.sort");

	/**
	 * Represents the overwrite check box label for externalizable.
	 */
	public static final String EXTERNALIZABLE_OVERWRITE_LABEL = Messages.getString("preferences.externalizable.overwrite");

	/**
	 * Represents the autosave check box label for externalizable.
	 */
	public static final String EXTERNALIZABLE_AUTOSAVE_LABEL = Messages.getString("preferences.externalizable.autosave");

	/**
	 * Represents the sort check box label for enumeration.
	 */
	public static final String ENUMERATION_SORT_LABEL = Messages.getString("preferences.enumeration.sort");

	/**
	 * Represents the overwrite check box label for enumeration.
	 */
	public static final String ENUMERATION_OVERWRITE_LABEL = Messages.getString("preferences.enumeration.overwrite");

	/**
	 * Represents the autosave check box label for enumeration.
	 */
	public static final String ENUMERATION_AUTOSAVE_LABEL = Messages.getString("preferences.enumeration.autosave");

	// /// Check Boxes End /////

	/**
	 * Mode constant for string.
	 */
	public static final String TO_STRING_BUILDER = "tostringbuilder";

	/**
	 * Mode constant for string buffer.
	 */
	public static final String STRING_BUFFER = "stringbuffer";

	/**
	 * Mode constant for string builder.
	 */
	public static final String STRING_BUILDER = "stringbuilder";

	/**
	 * Label for mode.
	 */
	public static final String MODE_LABEL = Messages.getString("preferences.tostring.mode");

	/**
	 * Label for string.
	 */
	public static final String TO_STRING_BUILDER_LABEL = Messages.getString("preferences.tostring.tostringbulder");

	/**
	 * Label for string buffer.
	 */
	public static final String STRING_BUFFER_LABEL = Messages.getString("preferences.tostring.stringbuffer");

	/**
	 * Label for string builder.
	 */
	public static final String STRING_BUILDER_LABEL = Messages.getString("preferences.tostring.stringbuilder");
}
