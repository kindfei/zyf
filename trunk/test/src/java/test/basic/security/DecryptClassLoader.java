package test.basic.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DecryptClassLoader extends ClassLoader {

	private Cipher cipher;

	public DecryptClassLoader(byte[] rawKey) throws GeneralSecurityException, IOException {
		DESKeySpec dks = new DESKeySpec(rawKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		String algorithm = "DES";
		SecureRandom sr = new SecureRandom();
		cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
	}
	
	public static void main(String args[]) throws Exception {
		
		byte[] rawKey = {};
		DecryptClassLoader dr = new DecryptClassLoader(rawKey);

		Class clasz = dr.loadClass("");
		Method main = clasz.getMethod("main", new Class[]{String[].class});

		main.invoke(null, new String[]{});
	}

	public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			Class clasz = findLoadedClass(name);
			if (clasz != null)
				return clasz;

			byte classData[] = {};
			if (classData != null) {
				byte decryptedClassData[] = cipher.doFinal(classData);
				clasz = defineClass(name, decryptedClassData,0, decryptedClassData.length);
			}
			
			if (clasz == null)
				clasz = findSystemClass(name);

			if (resolve && clasz != null)
				resolveClass(clasz);

			return clasz;
		} catch (GeneralSecurityException gse) {
			throw new ClassNotFoundException(gse.toString());
		}
	}
	
	public native Class loadClass();
}
