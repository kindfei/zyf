package test.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Test extends java.lang.ClassLoader {

	SecureRandom sr = new SecureRandom();
	String algorithm = "DES";
	String keyFileName = "E:/key";
	String encryptedFileName = "E:/final";
	String password = "Einstein";
	
	public void generateKey() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			kg.init(sr);
			SecretKey key = kg.generateKey();
			byte rawKeyData[] = key.getEncoded();
			writeByteArray(rawKeyData, keyFileName);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void encrypt() {
		try {
			byte rawKeyData[] = {0x77,0x21,0x3F,0x6E,0x3E,0x3F,0x4F,0x52};
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			byte[] data = password.getBytes();
			byte[] encryptedClassData = cipher.doFinal(data);
			writeByteArray(encryptedClassData, encryptedFileName);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String decrypt() {
		String str = null;
		try {
			byte rawKeyData[] = {0x77,0x21,0x3F,0x6E,0x3E,0x3F,0x4F,0x52};
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			byte encryptedData[] = readByteArray(encryptedFileName);
			byte decryptedData[] = cipher.doFinal(encryptedData);
			str = new String(decryptedData);
			System.out.println("[" + str + "]");
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	void writeByteArray(byte b[], String fileName) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			out.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	byte[] readByteArray(String fileName) {
		byte b[] = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			b = new byte[in.available()];
			in.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public static void main(String args[]) {
		Test test = new Test();
//		test.generateKey();
		test.encrypt();
		test.decrypt();
	}
}
