package zyf.sm.util;

import java.io.IOException;

import org.apache.commons.logging.Log;

import zyf.sm.bean.ConnectionInfo;

import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;


public class Executor {
	private static Log log = Helper.getLog(Executor.class);
	
	private static final String BIN_PUTTY = "bin/PUTTY.EXE";
	private static final String BIN_WINSCP = "bin/winscp382.exe";
	private static final String BIN_PUTTYJP = "bin/puttyjp.exe";
	
	//private static final String MD5_PUTTY = "f6918ab9914b9e90d957c2bc58a80371"; //0.58
	private static final String MD5_PUTTY = "337892c9f4ba1c6d0ff7ae87afae1350"; //0.59
	private static final String MD5_WINSCP = "1f1d0ab568c53aea766a773752b885f7";
	private static final String MD5_PUTTYJP = "48e896e1ec634538cf864c78acd562e1";
	
	private static final String SESSION_PATH_PUTTY = "Software\\SimonTatham\\PuTTY\\Sessions\\";
	private static final String SESSION_PATH_WINSCP = "Software\\Martin Prikryl\\WinSCP 2\\Sessions\\";
	
	private static final String PUTTY_DEFAULT_SETTING = "conf/PuttyDefaultSetting.properties";
	
	private static String dirSecureCRT;
	private static String dirTeraTerm;
	
	static {
		dirSecureCRT = GjtRegHelper.getSysValue("SOFTWARE\\VanDyke\\SecureCRT\\Install", "Main Directory");
		dirTeraTerm = GjtRegHelper.getSysValue("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\UTF-8 TeraTerm Pro with TTSSH2_is1", "InstallLocation");
		if (dirTeraTerm == null) {
			dirTeraTerm = GjtRegHelper.getSysValue("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\UTF-8 TeraTerm Pro with TTSSH2", "InstallLocation");
		}
	}
	
	public static boolean isSecureCRTInstalled() {
		if (dirSecureCRT == null) return false;
		return true;
	}
	
	public static boolean isTeraTermInstalled() {
		if (dirTeraTerm == null) return false;
		return true;
	}
	
	public static String runPuTTYen(ConnectionInfo info) {
		if (!Crypto.validateMD5(BIN_PUTTY, MD5_PUTTY)) return BIN_PUTTY + " MD5 not match";
		
		return runPuTTY(info, "Courier New", "UTF-8", BIN_PUTTY);
	}
	
	public static String runPuTTYjp(ConnectionInfo info) {
		if (!Crypto.validateMD5(BIN_PUTTYJP, MD5_PUTTYJP)) return BIN_PUTTYJP + " MD5 not match";
		
		return runPuTTY(info, "MS Gothic", "EUC-JP", BIN_PUTTYJP);
	}
	
	private static String runPuTTY(ConnectionInfo info, String font, String code, String bin) {
		
		String type = info.getType();
		if (type.equalsIgnoreCase("SSH1")) {
			type = "1";
		} else if (type.equalsIgnoreCase("SSH2")) {
			type = "2";
		} else if (type.equalsIgnoreCase("Telnet")) {
			type = "telnet";
		} else {
			return "Run PuTTY error: Unsupported type " + type;
		}
		String port = info.getPort();
		String user = info.getUser();
		String host = info.getHost();
		String password = Crypto.decrypt(info.getPassword());
		if (password == null) return "Run PuTTY error: Password can't be decrypted";
		
		String session = user + "@" + host;
		String path = SESSION_PATH_PUTTY + session;

		RegistryKey regKey = GjtRegHelper.impUserProp(PUTTY_DEFAULT_SETTING, path);
		try {
			regKey.setValue(GjtRegHelper.createValue("HostName", session));
			regKey.setValue(GjtRegHelper.createValue("WinTitle", session));
			regKey.setValue(GjtRegHelper.createValue("Font", font));
			regKey.setValue(GjtRegHelper.createValue("LineCodePage", code));
			
			String command = bin + " " + "-" + type + " -P " + port + " -pw " + password + " -load \"" + session + "\"";
			
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command, null, null);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "Run PuTTY error: " + e.getMessage();
		} finally {
			try {
				if (regKey != null) regKey.closeKey();
			} catch (RegistryException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return "";
	}
	
	public static String runWinSCP(ConnectionInfo info) {
		if (!Crypto.validateMD5(BIN_WINSCP, MD5_WINSCP)) return BIN_WINSCP + " MD5 not match";
		
		String user = info.getUser();
		String host = info.getHost();
		String port = info.getPort();
		String password = Crypto.decrypt(info.getPassword());
		if (password == null) return "Run WinSCP error: Password can't be decrypted";
		
		password = new Crypto().encrypt4WinSCP(password, user + host);
		
		String session = user + "@" + host;
		String path = SESSION_PATH_WINSCP + session;

		RegistryKey regKey = GjtRegHelper.createUserKey(path);
		try {
			regKey.setValue(GjtRegHelper.createValue("HostName", host));
			regKey.setValue(GjtRegHelper.createValue("UserName", user));
			regKey.setValue(GjtRegHelper.createValue("Password", password));
			regKey.setValue(GjtRegHelper.createValue("PingType", "0"));
			if (port.equals("22")) {
				try {
					regKey.deleteValue("PortNumber");
				} catch (NoSuchValueException e) {
					log.debug(e.getMessage());
				}
			} else {
				regKey.setValue(GjtRegHelper.createValue("PortNumber", port));
			}
			
			String command = BIN_WINSCP + " " + session;
//			String command = BIN_WINSCP + " scp://" + user + ":" + password + "@" + host + ":" + port;
			
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command, null, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "Run WinSCP error: " + e.getMessage();
		} finally {
			try {
				if (regKey != null) regKey.closeKey();
			} catch (RegistryException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return "";
	}
	
	public static String runTeraTerm(ConnectionInfo info) {
		if (dirTeraTerm == null) return "TeraTerm is not installed";
		
		String port = info.getPort();
		String user = info.getUser();
		String host = info.getHost();
		String password = Crypto.decrypt(info.getPassword());
		if (password == null) return "Run TeraTerm error: Password can't be decrypted";
		
		String arg = host + ":" + port + " /LA=J /KR=EUC /KT=EUC /auth=password" + " /user=" + user + " /passwd=" + password;
		
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(dirTeraTerm + "/ttermpro.exe " + arg, null, null);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return "Run TeraTerm error: " + e.getMessage();
		}
		return "";
	}
	
	public static String runSecureCRT(ConnectionInfo info) {
		if (dirSecureCRT == null) return "SecureCRT is not installed";
		
		String type = info.getType();
		if (type.equalsIgnoreCase("SSH1")) {
			
		} else if (type.equalsIgnoreCase("SSH2")) {
			
		} else {
			return "Run SecureCRT error: Unsupported type " + type;
		}
		String port = info.getPort();
		String user = info.getUser();
		String host = info.getHost();
		String password = Crypto.decrypt(info.getPassword());
		if (password == null) return "Run SecureCRT error: Password can't be decrypted";
		
		String arg = "/" + type + " /P " + port + " /password " + password + " /L " + user + " " + host;
		
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(dirSecureCRT + "/SecureCRT.exe " + arg, null, null);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return "Run SecureCRT error: " + e.getMessage();
		}
		return "";
	}
}
