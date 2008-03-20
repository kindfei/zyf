package zyf.sm.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.logging.Log;

import zyf.sm.Startup;


public class Crypto {
	private static Log log = Helper.getLog(Crypto.class);
	
	private static final byte PWALG_SIMPLE_MAGIC = (byte)0xA3;
	private static final String PWALG_SIMPLE_STRING = "0123456789ABCDEF";
	private static final int PWALG_SIMPLE_MAXLEN = 30;
	private static final int PWALG_SIMPLE_MAXLEN_WINSCP = 50;
	private static final byte PWALG_SIMPLE_FLAG = (byte)0xFF;
	
	private int index = 0;

	private Random rdm = new Random();
	
	public static boolean validateMD5(String path, String strMD5) {
		boolean result = false;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[16 * 1024];
			FileInputStream fin = new FileInputStream(path);
			int length = -1;
			while ((length = fin.read(buffer)) != -1) {
				md5.update(buffer, 0, length);
			}
			fin.close();
			byte[] rst = md5.digest();
			String strRst = "";
			for (int i = 0; i < rst.length; i++) {
				int b = rst[i];
				if (b < 0)
					b = b + 256;
				if (b < 16)
					strRst += "0";
				strRst += Integer.toHexString(b);
			}
			
			if (strMD5.equals(strRst)) {
				result = true;
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
	
	public static String encrypt(String password) {
		String str = null;
		try {
			byte[] rawKey = Startup.rawKey;
			DESKeySpec dks = new DESKeySpec(rawKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			SecureRandom sr = new SecureRandom();
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			
			byte[] src = password.getBytes("ISO-8859-1");
			byte[] code = cipher.doFinal(src);
			str = toStringValue(code);
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return str;
	}
	
	private static String toStringValue(byte[] code) {
		String result = "";
		char[] chars = PWALG_SIMPLE_STRING.toCharArray();
		Random rdm = new Random();
		int len = code.length;
		int shift = (len < PWALG_SIMPLE_MAXLEN) ? 
				rdm.nextInt(PWALG_SIMPLE_MAXLEN - len) : 0;
		
		result += hexConvert((byte)shift, chars);
		result += hexConvert((byte)len, chars);
			
		for (int i=0; i < shift; i++)
			result += hexConvert((byte)rdm.nextInt(256), chars);
		
		for (int i=0; i < len; i++)
			result += hexConvert(code[i], chars);
		
		while (result.length() < PWALG_SIMPLE_MAXLEN * 2)
			result += hexConvert((byte)rdm.nextInt(256), chars);
		
		return result;
	}
	
	private static String hexConvert(byte b, char[] chars) {
		char hight = chars[(b & 0xF0) >> 4];
		char low = chars[b & 0x0F];
		return new String(new char[]{hight,low});
	}
	
	public static String decrypt(String str) {
		String password = "";
		try {
			byte[] rawKey = Startup.rawKey;
			DESKeySpec dks = new DESKeySpec(rawKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			SecureRandom sr = new SecureRandom();
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			
			byte[] code = toByteArray(str);
			byte[] src = cipher.doFinal(code);
			password = new String(src, "ISO-8859-1");
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return password;
	}
	
	private static byte[] toByteArray(String str) {
		char[] chars = str.toCharArray();
		int idx = binConvert(0, chars) * 2;
		int len = binConvert(2, chars);
		byte[] result = new byte[len];
		
		for (int j=0,i=idx+4; j < len; i=i+2)
			result[j++] = binConvert(i, chars);

		return result;
	}
	
	private static byte binConvert(int i, char[] chars) {
		byte hight = (byte)(PWALG_SIMPLE_STRING.indexOf(chars[i]) << 4);
		byte low = (byte)PWALG_SIMPLE_STRING.indexOf(chars[++i]);
		return (byte)(hight + low);
	}
	
	public String encrypt4WinSCP(String password, String key) {
		String result = "";
		int shift;
		
		password = key + password;
		
		shift = (password.length() < PWALG_SIMPLE_MAXLEN_WINSCP) ? 
				rdm.nextInt(PWALG_SIMPLE_MAXLEN_WINSCP - password.length()) : 0;
		result += simpleEncryptChar(PWALG_SIMPLE_FLAG);
		result += simpleEncryptChar((byte)0);
		result += simpleEncryptChar((byte)password.length());
		result += simpleEncryptChar((byte)shift);

		for (int i=0; i < shift; i++) {
			result += simpleEncryptChar((byte)rdm.nextInt(256));
		}

		for (int i=0; i < password.length(); i++)
			try {
				result += simpleEncryptChar(password.getBytes("US-ASCII")[i]);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		while (result.length() < PWALG_SIMPLE_MAXLEN_WINSCP * 2) {
			result += simpleEncryptChar((byte)rdm.nextInt(256));
		}
		
		return result;
	}
	
	private String simpleEncryptChar(byte ch) {
		ch = (byte)((~ch) ^ PWALG_SIMPLE_MAGIC);
		char[] chars = PWALG_SIMPLE_STRING.toCharArray();
		char hight = chars[(ch & 0xF0) >> 4];
		char low = chars[ch & 0x0F];

		return new String(new char[]{hight,low});
	}
	
	public String decrypt4WinSCP(String pwdCode, String key) {
		String result = "";
		int lenght;
		
		byte flag = simpleDecryptNextChar(pwdCode);
		if (flag == PWALG_SIMPLE_FLAG) {
			simpleDecryptNextChar(pwdCode);
			lenght = simpleDecryptNextChar(pwdCode);
		} else {
			lenght = flag;
		}
		
		int len = simpleDecryptNextChar(pwdCode) * 2;
		index = index + len;
		
		for (int i=0; i<lenght; i++)
			try {
				result += new String(new byte[]{simpleDecryptNextChar(pwdCode)}, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
		if (flag == PWALG_SIMPLE_FLAG) {
			if (!result.substring(0, key.length()).equals(key)) {
				System.out.println("wrong key: " + result.substring(0, key.length()));
				result = "";
			}
			else result = result.substring(key.length());
		}
		
		index = 0;
		return result;
	}
	
	private byte simpleDecryptNextChar(String str) {
		if (str.length() > 0) {
			char[] chars = str.toCharArray();
			byte hight = (byte)(PWALG_SIMPLE_STRING.indexOf(chars[index++]) << 4);
			byte low = (byte)PWALG_SIMPLE_STRING.indexOf(chars[index++]);
			return (byte)~((hight + low) ^ PWALG_SIMPLE_MAGIC);
		} else {
			return 0x00;
		}
	}
}
