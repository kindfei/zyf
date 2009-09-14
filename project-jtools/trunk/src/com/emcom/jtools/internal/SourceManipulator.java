package com.emcom.jtools.internal;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jface.preference.IPreferenceStore;

import com.emcom.jtools.JToolsException;
import com.emcom.jtools.JToolsPlugin;
import com.emcom.jtools.preferences.PreferenceConstants;

/**
 * This is the work horse of the plugin.
 * <p>
 * 
 * It is responsible for fetching the template from either the configuration
 * file or from the preference store and then converting it into a useful
 * representation by using the fields contained in the compilation unit.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 2005
 * 
 * @version 2.0, 14th April 2006
 * 
 * @version 3.0, 10th December 2006
 */
public class SourceManipulator {
	/**
	 * The preference store associated with the plugin.
	 */
	private static final IPreferenceStore PREF_STORE = JToolsPlugin.getDefault().getPreferenceStore();

	/**
	 * First of ${attribute} patterns.
	 */
	private static final Pattern ATTR_PAT1 = Pattern.compile(".*\\$\\{attribute\\}.*");

	/**
	 * Second of ${attribute} patterns.
	 */
	private static final Pattern ATTR_PAT2 = Pattern.compile("\\$\\{attribute\\}");

	/**
	 * ${class_name} pattern.
	 */
	private static final Pattern CLSS_PAT = Pattern.compile("\\$\\{class_name\\}");

	/**
	 * ${class_instance} pattern.
	 */
	private static final Pattern CLSS_INST_PAT = Pattern.compile("\\$\\{class_instance\\}");

	/**
	 * Indentation pattern.
	 */
	private static final Pattern INDENT_PAT = Pattern.compile("(?m)^");

	/**
     * 
     */
	private static final Pattern READ_METHOD_PAT1 = Pattern.compile(".*\\$\\{read_method\\}.*");
	private static final Pattern READ_METHOD_PAT2 = Pattern.compile("\\$\\{read_method\\}");

	private static final Pattern WRITE_METHOD_PAT1 = Pattern.compile(".*\\$\\{write_method\\}.*");
	private static final Pattern WRITE_METHOD_PAT2 = Pattern.compile("\\$\\{write_method\\}");

	private static final Pattern CONVERT_CLASS_PAT = Pattern.compile("\\$\\{convert_class\\}");

	/**
	 * 
	 */
	private static final Pattern FIELD_PAT = Pattern.compile(".*private*;.*");

	/**
	 * Writes the toString implementation alongwith it's JavaDoc into the
	 * compilation unit.
	 * 
	 * @param compUnit
	 *            the compilation unit (Java source file).
	 * 
	 * @throws JToolsException
	 *             in case of any errors during the operation.
	 */
	public static void createToStringWithJavaDoc(IType type) throws JToolsException {
		try {
			String elemName = type.getElementName();

			IProgressMonitor monitor = new NullProgressMonitor();

			String javaDoc = SourceManipulator.PREF_STORE.getString(PreferenceConstants.TOSTRING_JAVADOC_STORE_KEY);

			if (Util.isNullString(javaDoc)) {
				javaDoc = Util.getDefaultToStringJavaDoc();
			}

			javaDoc = CLSS_PAT.matcher(javaDoc).replaceAll(elemName);

			String impl = SourceManipulator.PREF_STORE.getString(PreferenceConstants.TOSTRING_BODY_STORE_KEY);

			if (Util.isNullString(impl)) {
				impl = Util.getDefaultToStringImplementation();
			}

			impl = CLSS_PAT.matcher(impl).replaceAll(elemName);

			Matcher matcher = ATTR_PAT1.matcher(impl);

			String attributesLine;

			StringBuffer attributesChunk = new StringBuffer("");

			String fieldName;

			IField[] fields = type.getFields();

			boolean isSort = SourceManipulator.PREF_STORE.getBoolean("tostring.sort");

			if (isSort) {
				Arrays.sort(fields, new Comparator<IField>() {
					public int compare(IField field1, IField field2) {
						return field1.getElementName().compareTo(field2.getElementName());
					}
				});
			}

			// translate all variables into their values.
			if (matcher.find()) {
				attributesLine = matcher.group();

				for (int i = 0; i < fields.length; i++) {
					// don't display static fields.
					if (Flags.isStatic(fields[i].getFlags())) {
						continue;
					}

					fieldName = fields[i].getElementName();

					attributesChunk.append(ATTR_PAT2.matcher(attributesLine).replaceAll(fieldName)).append('\n');
				}

				attributesChunk = fields.length == 0 ? new StringBuffer("") : new StringBuffer(attributesChunk
						.substring(0, attributesChunk.lastIndexOf("\n")));
			}

			impl = matcher.replaceAll(attributesChunk.toString());

			/*
			 * Bug Fix: The very fist time after installation, if toString was
			 * attempted to be generated on a class which already had toString,
			 * auto overwrite feature was failing. This was because unless the
			 * preference page is opened for the first time, the value of the
			 * overwrite flag as fetched from the preference store will be false
			 * as the preference store wouldn't have been initialized.
			 * 
			 * Now we assume a true value for overwrite flag if isDefault
			 * fetches true.
			 */

			boolean canOverwrite = SourceManipulator.PREF_STORE.isDefault("tostring.overwrite")
					|| SourceManipulator.PREF_STORE.getBoolean("tostring.overwrite");

			IMethod[] existingToString = type.findMethods(type.getMethod("toString", new String[] {}));

			String method = new StringBuffer(javaDoc).append(impl).toString();

			String indent = getIndentation(type);

			if (existingToString == null) {
				method = INDENT_PAT.matcher(method).replaceAll(indent);

				type.createMethod(method, null, true, monitor);
			} else {
				if (canOverwrite) {
					existingToString[0].delete(false, monitor);

					method = INDENT_PAT.matcher(method).replaceAll(indent);

					type.createMethod(method, null, true, monitor);
				}
			}
		} catch (JavaModelException e) {
			throw new JToolsException("Type " + type.getElementName() + " generated exception", e);
		}
	}
	
	/**
	 * Writes the enumeration implementation alongwith it's JavaDoc into the
	 * compilation unit.
	 * 
	 * @param type
	 * @throws JToolsException
	 */
	public static void createEnumerationWithJavaDoc(IType type) throws JToolsException {
		try {
			String elemName = type.getElementName();

			IProgressMonitor monitor = new NullProgressMonitor();

			String javaDoc = SourceManipulator.PREF_STORE
					.getString(PreferenceConstants.ENUMERATION_JAVADOC_STORE_KEY);

			if (Util.isNullString(javaDoc)) {
				javaDoc = Util.getDefaultEnumerationJavaDoc();
			}

			javaDoc = CLSS_PAT.matcher(javaDoc).replaceAll(elemName);

			String instanceName = Character.toLowerCase(elemName.charAt(0)) + elemName.substring(1);

			javaDoc = CLSS_INST_PAT.matcher(javaDoc).replaceAll(instanceName);

			String impl = SourceManipulator.PREF_STORE.getString(PreferenceConstants.ENUMERATION_BODY_STORE_KEY);

			if (Util.isNullString(impl)) {
				impl = Util.getDefaultEnumerationImplementation();
			}

			impl = CLSS_PAT.matcher(impl).replaceAll(elemName);
			
			Matcher matcher = FIELD_PAT.matcher(impl);
			
			String fieldImpl = "";
			
			if (matcher.find()) {
				impl = matcher.replaceAll("");
				fieldImpl = matcher.group();
			}

			String field = INDENT_PAT.matcher(new StringBuffer(javaDoc).append(fieldImpl).toString()).replaceAll(getIndentation(type));;
			String methods = INDENT_PAT.matcher(impl).replaceAll(getIndentation(type));

			IJavaElement position = determinePosition(type);

			type.createField(field, position, true, monitor);
			type.createMethod(methods, position, true, monitor);

		} catch (JavaModelException e) {
			throw new JToolsException("Type " + type.getElementName() + " generated exception", e);
		}
	}

	/**
	 * Writes the externalizable implementation alongwith it's JavaDoc into the
	 * compilation unit.
	 * 
	 * @param compUnit
	 *            the compilation unit (Java source file).
	 * 
	 * @throws JToolsException
	 *             in case of any errors during the operation.
	 */
	public static void createExternalizableWithJavaDoc(IType type) throws JToolsException {
		try {
			String elemName = type.getElementName();

			IProgressMonitor monitor = new NullProgressMonitor();

			String javaDoc = SourceManipulator.PREF_STORE
					.getString(PreferenceConstants.EXTERNALIZABLE_JAVADOC_STORE_KEY);

			if (Util.isNullString(javaDoc)) {
				javaDoc = Util.getDefaultExternalizableJavaDoc();
			}

			javaDoc = CLSS_PAT.matcher(javaDoc).replaceAll(elemName);

			String instanceName = Character.toLowerCase(elemName.charAt(0)) + elemName.substring(1);

			javaDoc = CLSS_INST_PAT.matcher(javaDoc).replaceAll(instanceName);

			String impl = SourceManipulator.PREF_STORE.getString(PreferenceConstants.EXTERNALIZABLE_BODY_STORE_KEY);

			if (Util.isNullString(impl)) {
				impl = Util.getDefaultExternalizableImplementation();
			}

			impl = CLSS_PAT.matcher(impl).replaceAll(elemName);

			IField[] fields = type.getFields();

			boolean isSort = SourceManipulator.PREF_STORE.getBoolean("externalizable.sort");

			if (isSort) {
				Arrays.sort(fields, new Comparator<IField>() {
					public int compare(IField field1, IField field2) {
						return field1.getElementName().compareTo(field2.getElementName());
					}
				});
			}

			// create attributes
			Matcher matcher;
			String attributesChunk;

			// write
			matcher = WRITE_METHOD_PAT1.matcher(impl);
			if (matcher.find()) {
				attributesChunk = createAttributesChunk(matcher.group(), fields);
				impl = matcher.replaceAll(attributesChunk);
			}

			// read
			matcher = READ_METHOD_PAT1.matcher(impl);
			if (matcher.find()) {
				attributesChunk = createAttributesChunk(matcher.group(), fields);
				impl = matcher.replaceAll(attributesChunk);
			}

			impl = CLSS_INST_PAT.matcher(impl).replaceAll(instanceName);

			/*
			 * Bug Fix: The very fist time after installation, if externalizable
			 * was attempted to be generated on a class which already had one,
			 * auto overwrite feature was failing. This was because unless the
			 * preference page is opened for the first time, the value of the
			 * overwrite flag as fetched from the preference store will be false
			 * as the preference store wouldn't have been initialized.
			 * 
			 * Now we assume a true value for overwrite flag if isDefault
			 * fetches true.
			 */

			boolean canOverwrite = SourceManipulator.PREF_STORE.isDefault("externalizable.overwrite")
					|| SourceManipulator.PREF_STORE.getBoolean("externalizable.overwrite");

			IMethod[] existing = null;

			existing = type.findMethods(type.getMethod("writeExternal", new String[] { "QObjectOutput;" }));

			if (existing != null && canOverwrite) {
				existing[0].delete(false, monitor);
			}

			existing = type.findMethods(type.getMethod("readExternal", new String[] { "QObjectInput;" }));

			if (existing != null && canOverwrite) {
				existing[0].delete(false, monitor);
			}

			String result = new StringBuffer(javaDoc).append(impl).toString();

			String indent = getIndentation(type);

			result = INDENT_PAT.matcher(result).replaceAll(indent);

			IJavaElement position = determinePosition(type);

			type.createMethod(result, position, true, monitor);

		} catch (JavaModelException e) {
			throw new JToolsException("Type " + type.getElementName() + " generated exception", e);
		}
	}

	/**
	 * 
	 * @param impl
	 * @param fields
	 * @return
	 * @throws JavaModelException
	 */
	private static String createAttributesChunk(String impl, IField[] fields) throws JavaModelException {
		Matcher matcher = ATTR_PAT1.matcher(impl);

		String attributesLine;

		StringBuffer attributesChunk = new StringBuffer("");

		String fieldName;

		String type;

		String writeMethod;
		String readMethod;
		String className;

		String r;

		// translate all variables into their values.
		if (matcher.find()) {
			attributesLine = matcher.group();

			for (int i = 0; i < fields.length; i++) {
				// don't touch static or final fields.
				if (Flags.isStatic(fields[i].getFlags()) || Flags.isFinal(fields[i].getFlags())) {
					continue;
				}

				fieldName = fields[i].getElementName();

				type = Signature.toString(fields[i].getTypeSignature());

				className = "";

				if (type.equals("int")) {
					writeMethod = "writeInt";
					readMethod = "readInt";
				} else if (type.equals("byte")) {
					writeMethod = "writeByte";
					readMethod = "readByte";
				} else if (type.equals("boolean")) {
					writeMethod = "writeBoolean";
					readMethod = "readBoolean";
				} else if (type.equals("char")) {
					writeMethod = "writeChar";
					readMethod = "readChar";
				} else if (type.equals("double")) {
					writeMethod = "writeDouble";
					readMethod = "readDouble";
				} else if (type.equals("float")) {
					writeMethod = "writeFloat";
					readMethod = "readFloat";
				} else if (type.equals("long")) {
					writeMethod = "writeLong";
					readMethod = "readLong";
				} else if (type.equals("short")) {
					writeMethod = "writeShort";
					readMethod = "readShort";
				} else {
					writeMethod = "writeObject";
					readMethod = "readObject";
					className = "(" + type + ") ";
				}

				r = ATTR_PAT2.matcher(attributesLine).replaceAll(fieldName);
				r = WRITE_METHOD_PAT2.matcher(r).replaceAll(writeMethod);
				r = READ_METHOD_PAT2.matcher(r).replaceAll(readMethod);
				r = CONVERT_CLASS_PAT.matcher(r).replaceAll(className);

				attributesChunk.append(r).append('\n');
			}

			attributesChunk = fields.length == 0 ? new StringBuffer("") : new StringBuffer(attributesChunk.substring(0,
					attributesChunk.lastIndexOf("\n")));
		}

		return attributesChunk.toString();
	}

	/**
	 * Determines the position for the new externalizable.
	 * 
	 * @param type
	 *            the type in which the externalizable will be inserted.
	 * 
	 * @return an <tt>IJavaElement</tt> which is the first method (if any) in
	 *         the type or <tt>null</tt> otherwise.
	 * 
	 * @throws JavaModelException
	 *             in case of errors during the operation.
	 */
	private static IJavaElement determinePosition(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();

		IJavaElement position = null;

		if (methods != null && methods.length >= 1) {
			position = methods[0];
		}

		return position;
	}

	/**
	 * Returns the indentation used for a given type.
	 * 
	 * @param type
	 *            whose indentation is desired.
	 * 
	 * @return a string holding the indent value.
	 * 
	 * @throws JavaModelException
	 *             in case of errors during the operation.
	 */
	private static String getIndentation(IType type) throws JavaModelException {
		int indent = StubUtility.getIndentUsed(type);

		StringBuffer tab = new StringBuffer("");

		for (int x = 0; x <= indent; x++) {
			tab.append("    ");
		}

		return tab.toString();
	}
}
