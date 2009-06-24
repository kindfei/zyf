package zyf.sm.util;

import java.io.IOException;

import org.apache.commons.logging.Log;

public class CommandRegistryEditor {
	private static final Log log = Helper.getLog(CommandRegistryEditor.class);
	
	public static final String TYPE_REG_SZ = "REG_SZ";
	public static final String TYPE_REG_MULTI_SZ = "REG_MULTI_SZ";
	public static final String TYPE_REG_DWORD_BIG_ENDIAN = "REG_DWORD_BIG_ENDIAN";
	public static final String TYPE_REG_DWORD = "REG_DWORD";
	public static final String TYPE_REG_BINARY = "REG_BINARY";
	public static final String TYPE_REG_DWORD_LITTLE_ENDIAN = "REG_DWORD_LITTLE_ENDIAN";
	public static final String TYPE_REG_LINK = "REG_LINK";
	public static final String TYPE_REG_FULL_RESOURCE_DESCRIPTOR = "REG_FULL_RESOURCE_DESCRIPTOR";
	public static final String TYPE_REG_EXPAND_SZ = "REG_EXPAND_SZ";
	
	public static void add(String path, String type, String key, String value) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("reg add \"" + path + "\" /v " + key + " /t " + type + " /d " + value + " /f");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void delete(String path, String key) {
		Runtime runtime = Runtime.getRuntime();
		String command = "";
		if (key != null) command = "reg delete \"" + path + "\" /v " + key + " /f";
		else command = "reg delete " + path + " /f";
		try {
			runtime.exec(command);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void copy(String src, String dest) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("reg copy \"" + src + "\" \"" + dest + "\" /s /f");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void importFile(String file) {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("reg import \"" + file + "\"");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
