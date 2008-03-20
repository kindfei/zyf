package helper;

import java.security.SecureRandom;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.logging.Log;

import zyf.sm.Startup;
import zyf.sm.util.Helper;


public class Prevenient {
	private static Log log = Helper.getLog(Prevenient.class);
	
	private static byte[] toArray (String str) {
		StringTokenizer token = new StringTokenizer(str, "/");
		byte[] bs = new byte[token.countTokens()];
		for (int i=0; token.hasMoreTokens(); i++) {
			String val = token.nextToken();
			bs[i] = (byte)Integer.parseInt(val);
		}
		return bs;
	}
	
	public static String decrypt(String str) {
		String password = null;
		try {
			byte[] rawKey = Startup.rawKey;
			DESKeySpec dks = new DESKeySpec(rawKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			SecureRandom sr = new SecureRandom();
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			
			byte[] code = toArray(str);
			byte[] src = cipher.doFinal(code);
			password = new String(src, "ISO-8859-1");
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return password;
	}
}
