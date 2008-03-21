package zyf.cr;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtils {
	private static Log log = LogFactory.getLog(PropertyUtils.class);
	private static Properties prop;
	static {
		prop = new Properties();
		try {
			prop.load(ClassLoader.getSystemResourceAsStream("CallRegister.properties"));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static boolean isConsoleInput() {
		return Boolean.getBoolean(prop.getProperty("CONSOLE_INPUT"));
	}
	
	public static String getNumber() {
		return prop.getProperty("PHONE_NUMBER");
	}
	
	public static String getPassword() {
		return prop.getProperty("PASSWORD");
	}
	
	public static int getRecentlyMonths() {
		int i = 0;
		try {
			i = Integer.parseInt(prop.getProperty("RECENT_MONTHS"));
		} catch (NumberFormatException e) {
		}
		return i;
	}
	
	public static String getBeginDate() {
		return prop.getProperty("BEGIN_DATE");
	}
	
	public static String getEndDate() {
		return prop.getProperty("END_DATE");
	}
	
	public static String getSavePath() {
		return prop.getProperty("SAVE_PATH");
	}
	
	public static String getImageSavePath() {
		return prop.getProperty("IMAGE_SAVE_PATH");
	}
}
