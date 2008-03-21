package test.basic.security;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class TestSecurity {
	public static void main(String[] args) {
		try {
			
			
			
			KeyStore ks = KeyStore.getInstance("jks");
			ks.load(new FileInputStream("keystores/pro1621keystore.keystore"), new char[] {'s','t','r','e','a','m','p','a','5','5'});
//			ks.load(new FileInputStream("keystores/pro1622keystore.keystore"), new char[] {'t','r','a','d','e','r','p','a','5','5'});
//			ks.load(new FileInputStream("keystores/TestKeyStore"), new char[] {'8','8','8','8','8','8'});
			System.out.println(ks.size());
			Enumeration e = ks.aliases();
			while (e.hasMoreElements()) {
				String aliase = (String) e.nextElement();
				System.out.println(aliase);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static void showInfo(KeyStore ks, String aliase, char[] pass) throws Exception {
		
		Key k = ks.getKey(aliase, pass);
		System.out.println(k.getAlgorithm());
		System.out.println(k.getFormat());
		System.out.println(k.getClass().getName());
		
		Certificate cert = ks.getCertificate(aliase);
		System.out.println(cert.toString());
		System.out.println(cert.getType());
		
		Provider p = ks.getProvider();
		System.out.println(p.getClass().getName());
	}
}
