package zyf.sm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;

public class JavaRegistryEditor {
	private static Log log = Helper.getLog(JavaRegistryEditor.class);

	private static final int KEY_QUERY_VALUE = 1;
	private static final int KEY_SET_VALUE = 2;
	private static final int KEY_READ = 0x20019;
	private static final int DELETE = 0x10000;

	private static final int KEY_ENUMERATE_SUB_KEYS = 8;

	private static final int NULL_NATIVE_HANDLE = 0;

	private static final int ERROR_CODE = 1;
	private static final int VALUES_NUMBER = 2;
	private static final int MAX_VALUE_NAME_LENGTH = 4;
	private static final int MAX_KEY_LENGTH = 3;

	private static final int SUBKEYS_NUMBER = 0;

	private static final int ERROR_SUCCESS = 0;

	private static final int ERROR_FILE_NOT_FOUND = 2;

	private Preferences root;

	private Class clz;

	private String subKey;

	public JavaRegistryEditor(Preferences root, String path) {
		this.root = root;
		clz = root.getClass();
		this.subKey = path;
	}

	public static JavaRegistryEditor userRoot(String path) {
		Preferences userRoot = Preferences.userRoot();
		return new JavaRegistryEditor(userRoot, path);
	}

	public static JavaRegistryEditor systemRoot(String path) {
		Preferences systemRoot = Preferences.systemRoot();
		return new JavaRegistryEditor(systemRoot, path);
	}

	private int[] WindowsRegCreateKeyEx(int hKey, byte[] subKey) {
		int[] i = null;
		try {
			final Method mWindowsRegCreateKeyEx1 = clz.getDeclaredMethod(
					"WindowsRegCreateKeyEx1", new Class[] { int.class,
							byte[].class });
			mWindowsRegCreateKeyEx1.setAccessible(true);

			i = (int[]) mWindowsRegCreateKeyEx1.invoke(root, new Object[] {
					new Integer(hKey), subKey });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i;
	}

	private int WindowsRegDeleteKey(int hKey, byte[] subKey) {
		Integer i = null;
		try {
			final Method mWindowsRegDeleteKey = clz.getDeclaredMethod(
					"WindowsRegDeleteKey", new Class[] { int.class,
							byte[].class });
			mWindowsRegDeleteKey.setAccessible(true);

			i = (Integer) mWindowsRegDeleteKey.invoke(root, new Object[] {
					new Integer(hKey), subKey });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i.intValue();
	}

	private int WindowsRegFlushKey(int hKey) {
		Integer i = null;
		try {
			final Method mWindowsRegFlushKey = clz.getDeclaredMethod(
					"WindowsRegFlushKey", new Class[] { int.class });
			mWindowsRegFlushKey.setAccessible(true);

			i = (Integer) mWindowsRegFlushKey.invoke(root,
					new Object[] { new Integer(hKey) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i.intValue();
	}

	private byte[] WindowsRegQueryValueEx(int hKey, byte[] valueName) {
		byte[] b = null;
		try {
			final Method mWinRegQueryValue = clz.getDeclaredMethod(
					"WindowsRegQueryValueEx", new Class[] { int.class,
							byte[].class });
			mWinRegQueryValue.setAccessible(true);

			b = (byte[]) mWinRegQueryValue.invoke(root, new Object[] {
					new Integer(hKey), valueName });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return b;
	}

	private int WindowsRegSetValueEx(int hKey, byte[] valueName, byte[] value) {
		Integer i = null;
		try {
			final Method mWindowsRegSetValueEx1 = clz.getDeclaredMethod(
					"WindowsRegSetValueEx1", new Class[] { int.class,
							byte[].class, byte[].class });
			mWindowsRegSetValueEx1.setAccessible(true);

			i = (Integer) mWindowsRegSetValueEx1.invoke(root, new Object[] {
					new Integer(hKey), valueName, value });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i.intValue();
	}

	private int WindowsRegDeleteValue(int hKey, byte[] valueName) {
		Integer i = null;
		try {
			final Method mWindowsRegDeleteValue = clz.getDeclaredMethod(
					"WindowsRegDeleteValue", new Class[] { int.class,
							byte[].class });
			mWindowsRegDeleteValue.setAccessible(true);

			i = (Integer) mWindowsRegDeleteValue.invoke(root, new Object[] {
					new Integer(hKey), valueName });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i.intValue();
	}

	private int[] WindowsRegQueryInfoKey(int hKey) {
		int[] i = null;
		try {
			final Method mWindowsRegQueryInfoKey = clz.getDeclaredMethod(
					"WindowsRegQueryInfoKey1", new Class[] { int.class });
			mWindowsRegQueryInfoKey.setAccessible(true);

			i = (int[]) mWindowsRegQueryInfoKey.invoke(root,
					new Object[] { new Integer(hKey) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return i;
	}

	private byte[] WindowsRegEnumKeyEx(int hKey, int subKeyIndex,
			int maxKeyLength) {
		byte[] b = null;
		try {
			final Method mWindowsRegEnumKeyEx = clz.getDeclaredMethod(
					"WindowsRegEnumKeyEx1", new Class[] { int.class, int.class,
							int.class });
			mWindowsRegEnumKeyEx.setAccessible(true);

			b = (byte[]) mWindowsRegEnumKeyEx.invoke(root, new Object[] {
					new Integer(hKey), new Integer(subKeyIndex),
					new Integer(maxKeyLength) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return b;
	}

	private byte[] WindowsRegEnumValue(int hKey, int valueIndex,
			int maxValueNameLength) {
		byte[] b = null;
		try {
			final Method mWindowsRegEnumValue = clz.getDeclaredMethod(
					"WindowsRegEnumValue1", new Class[] { int.class, int.class,
							int.class });
			mWindowsRegEnumValue.setAccessible(true);

			b = (byte[]) mWindowsRegEnumValue.invoke(root, new Object[] {
					new Integer(hKey), new Integer(valueIndex),
					new Integer(maxValueNameLength) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return b;
	}

	private int openKey(byte[] windowsAbsolutePath, int mask1, int mask2) {
		Integer hSettings = null;
		try {
			final Method mOpenKey = clz.getDeclaredMethod("openKey",
					new Class[] { byte[].class, int.class, int.class });
			mOpenKey.setAccessible(true);

			hSettings = (Integer) mOpenKey.invoke(root,
					new Object[] { windowsAbsolutePath, new Integer(mask1),
							new Integer(mask2) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return hSettings.intValue();
	}

	private void closeKey(int nativeHandle) {
		try {
			final Method mCloseKey = clz.getDeclaredMethod("closeKey",
					new Class[] { int.class });
			mCloseKey.setAccessible(true);

			mCloseKey.invoke(Preferences.userRoot(),
					new Object[] { new Integer(nativeHandle) });
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getSpi(String javaName) {
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_READ, KEY_READ);
		if (nativeHandle == NULL_NATIVE_HANDLE) {
			return null;
		}
		Object resultObject = WindowsRegQueryValueEx(nativeHandle, stringToByteArray(javaName));
		if (resultObject == null) {
			closeKey(nativeHandle);
			return null;
		}
		closeKey(nativeHandle);
		return byteArrayToString((byte[]) resultObject);
	}

	public void putSpi(String javaName, String value) {
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_SET_VALUE, KEY_SET_VALUE);
		if (nativeHandle == NULL_NATIVE_HANDLE) {
			// isBackingStoreAvailable = false;
			return;
		}
		int result = WindowsRegSetValueEx(nativeHandle, stringToByteArray(javaName), stringToByteArray(value));
		if (result != ERROR_SUCCESS) {
		}
		closeKey(nativeHandle);
	}

	public void removeSpi(String key) {
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_SET_VALUE, KEY_SET_VALUE);
		if (nativeHandle == NULL_NATIVE_HANDLE) {
			return;
		}
		int result = WindowsRegDeleteValue(nativeHandle, stringToByteArray(key));
		if (result != ERROR_SUCCESS && result != ERROR_FILE_NOT_FOUND) {
			//            isBackingStoreAvailable = false;
		}
		closeKey(nativeHandle);
	}
	
	public void flush() {
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_READ, KEY_READ);
        if (nativeHandle == NULL_NATIVE_HANDLE) {
        	return;
        }
		int result = WindowsRegFlushKey(nativeHandle);
        if (result != ERROR_SUCCESS) {
        }      
		closeKey(nativeHandle);
	}

	public String[] keysSpi() {
		// Find out the number of values
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_QUERY_VALUE, KEY_QUERY_VALUE);
		if (nativeHandle == NULL_NATIVE_HANDLE) {
			return null;
		}
		int[] result = WindowsRegQueryInfoKey(nativeHandle);
		if (result[ERROR_CODE] != ERROR_SUCCESS) {
		}
		int maxValueNameLength = result[MAX_VALUE_NAME_LENGTH];
		int valuesNumber = result[VALUES_NUMBER];
		if (valuesNumber == 0) {
			closeKey(nativeHandle);
			return new String[0];
		}
		// Get the values
		String[] valueNames = new String[valuesNumber];
		for (int i = 0; i < valuesNumber; i++) {
			byte[] windowsName = WindowsRegEnumValue(nativeHandle, i,
					maxValueNameLength + 1);
			if (windowsName == null) {
			}
			valueNames[i] = byteArrayToString(windowsName);
		}
		closeKey(nativeHandle);
		return valueNames;
	}

	public String[] childrenNamesSpi() {
		// Open key
		int nativeHandle = openKey(stringToByteArray(subKey),
				KEY_ENUMERATE_SUB_KEYS | KEY_QUERY_VALUE,
				KEY_ENUMERATE_SUB_KEYS | KEY_QUERY_VALUE);
		if (nativeHandle == NULL_NATIVE_HANDLE) {
			return null;
		}
		// Get number of children
		int[] result = WindowsRegQueryInfoKey(nativeHandle);
		if (result[ERROR_CODE] != ERROR_SUCCESS) {
		}
		int maxKeyLength = result[MAX_KEY_LENGTH];
		int subKeysNumber = result[SUBKEYS_NUMBER];
		if (subKeysNumber == 0) {
			closeKey(nativeHandle);
			return new String[0];
		}
		String[] subkeys = new String[subKeysNumber];
		String[] children = new String[subKeysNumber];
		// Get children
		for (int i = 0; i < subKeysNumber; i++) {
			byte[] windowsName = WindowsRegEnumKeyEx(nativeHandle, i,
					maxKeyLength + 1);
			if (windowsName == null) {
			}
			String javaName = byteArrayToString(windowsName);
			children[i] = javaName;
		}
		closeKey(nativeHandle);
		return children;
	}
	
	private byte[] stringToByteArray(String str) {
		byte[] result = new byte[str.length() + 1];
		for (int i = 0; i < str.length(); i++) {
			result[i] = (byte) str.charAt(i);
		}
		result[str.length()] = 0;
		return result;
	}
	
	private String byteArrayToString(byte[] array) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < array.length - 1; i++) {
			result.append((char) array[i]);
		}
		return result.toString();
	}
}
