package zyf.sm.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegDWordValue;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;
import com.ice.jni.registry.RegistryValue;

public class GjtRegHelper {
	private static final Log log = Helper.getLog(GjtRegHelper.class);
	
	private static RegistryKey systemRoot = Registry.HKEY_LOCAL_MACHINE;
	private static RegistryKey userRoot = Registry.HKEY_CURRENT_USER;
	
	public static String getSysValue(String path, String name) {
		RegistryKey regKey = null;
		try {
			regKey = systemRoot.openSubKey(path, RegistryKey.ACCESS_READ);
			return regKey.getStringValue(name);
		} catch (NoSuchKeyException e) {
			log.debug(e.getMessage());
		} catch (NoSuchValueException e) {
			log.debug(e.getMessage());
		} catch (RegistryException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (regKey != null) regKey.closeKey();
			} catch (RegistryException e) {
				log.debug(e.getMessage());
			}
		}
		return null;
	}
	
	public static RegistryKey openUserKey(String path) {
		try {
			return userRoot.openSubKey(path, RegistryKey.ACCESS_ALL);
		} catch (NoSuchKeyException e) {
			log.debug(e.getMessage());
		} catch (RegistryException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static RegistryKey createUserKey(String path) {
		RegistryKey regKey = null;
		regKey = openUserKey(path);
		if (regKey != null) {
			return regKey;
		}
		try {
			regKey = userRoot.createSubKey(path, null);
			log.info("Create: " + path);
		} catch (RegistryException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (regKey != null) regKey.closeKey();
			} catch (RegistryException e) {
				log.debug(e.getMessage());
			}
		}
		return openUserKey(path);
	}
	
	public static RegistryKey impUserProp(String file, String path) {
		RegistryKey regKey = null;
		regKey = openUserKey(path);
		if (regKey != null) {
			return regKey;
		}
		try {
			regKey = userRoot.createSubKey(path, null);
			log.info("Create: " + path);
			
			Properties prop = new Properties();
			prop.load(new FileInputStream(file));
			for (Iterator iter = prop.entrySet().iterator(); iter.hasNext();) {
				Map.Entry element = (Map.Entry) iter.next();
				String key = (String) element.getKey();
				String value = (String) element.getValue();
				
				if (value.startsWith("dword:")) {
					value = value.substring(6);
					RegDWordValue dwValue = new RegDWordValue(regKey, key);
					dwValue.setData(parse(value));
					regKey.setValue(dwValue);
				} else {
					RegStringValue strValue = new RegStringValue(regKey, key);
					strValue.setData(value);
					regKey.setValue(strValue);
				}
				log.debug("Insert: " + key + "=" + value);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (RegistryException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (regKey != null) regKey.closeKey();
			} catch (RegistryException e) {
				log.debug(e.getMessage());
			}
		}
		return openUserKey(path);
	}
	
	private static int parse(String source) {
		long l = Long.parseLong(source, 16);
		if (l > Integer.MAX_VALUE) {
			return (int) (Integer.MIN_VALUE + l - Integer.MAX_VALUE - 1);
		} else {
			return (int) l;
		}
	}
	
	public static RegistryValue createValue(String name, String value) {
		RegStringValue regValue = new RegStringValue(null, name);
		regValue.setData(value);
		return regValue;
	}
	
	public static RegistryValue createValue(String name, int value) {
		RegDWordValue regValue = new RegDWordValue(null, name);
		regValue.setData(value);
		return regValue;
	}
}
