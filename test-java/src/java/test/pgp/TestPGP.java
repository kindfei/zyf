package test.pgp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

public class TestPGP {
	public static void main(String[] args) throws Exception {
		String PGPPATH = "C:/Documents and Settings/yz69579/Desktop/New Folder/";
		String pubring = PGPPATH + "pubring.pkr";
		String secring = PGPPATH + "secring.skr";
		String pass = "Sherry_RSA";
		String userId = "Advent_DX";
		
		String fileName = PGPPATH + "textfile";
		
		PGPHandler inst = new PGPHandler();
		inst.setPubring(pubring);
		inst.setSecring(secring);
		
		inst.init();
		
		/*
		 * 
		 */
//		dumpPublicKey(new FileInputStream(pubring));
//		dumpSecretKey(new FileInputStream(secring));
		
		/*
		 * 
		 */
//		inst.signFile(new FileOutputStream(PGPPATH + "textfile.s"), fileName, userId, pass, true);
//		inst.decryptAndVerifyFile(PGPPATH + "textfile.s.out", new FileInputStream(PGPPATH + "textfile.s"), pass);
//		
		inst.encryptFile(new FileOutputStream(PGPPATH + "textfile.e"), fileName, userId, true, false, false, userId, pass);
//		inst.decryptAndVerifyFile(PGPPATH + "textfile.e.out", new FileInputStream(PGPPATH + "textfile.e"), pass);
//		
//		inst.encryptFile(new FileOutputStream(PGPPATH + "textfile.s.e"), fileName, userId, true, false, true, userId, pass);
//		inst.decryptAndVerifyFile(PGPPATH + "textfile.s.e.out", new FileInputStream(PGPPATH + "textfile.s.e"), pass);
		
		/*
		 * 
		 */
//		inst.decryptAndVerifyFile(PGPPATH + "tmp.s.out", new FileInputStream(PGPPATH + "tmp.s.asc"));
//		inst.decryptAndVerifyFile(PGPPATH + "tmp.e.out", new FileInputStream(PGPPATH + "tmp.e.asc"));
//		inst.decryptAndVerifyFile(PGPPATH + "tmp.s.e.out", new FileInputStream(PGPPATH + "tmp.s.e.asc"));
		
//		inst.decryptAndVerifyFile("C:/Documents and Settings/yz69579/Desktop/pgp_test.txt.pgp.out", new FileInputStream("C:/Documents and Settings/yz69579/Desktop/pgp_test.txt.pgp"), pass);
		
	}

	static void dumpPublicKey(InputStream in) throws IOException, PGPException {
		in = PGPUtil.getDecoderStream(in);

		PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in);

		Iterator<?> rIt = pgpPub.getKeyRings();

		while (rIt.hasNext()) {
			PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
			Iterator<?> kIt = kRing.getPublicKeys();

			boolean first = true;
			while (kIt.hasNext()) {
				PGPPublicKey k = (PGPPublicKey) kIt.next();
				
				String userIds = "";
				for (Iterator<?> it = k.getUserIDs(); it.hasNext();) {
					userIds += it.next() + ", ";
				}
				
				if (first) {
					first = false;
				} else {
					System.out.print("\tsubkey: ");
				}
				System.out.println(
						"algorithm=" + getAlgorithm(k.getAlgorithm()) + "\t" + 
						"bitStrength=" + k.getBitStrength() + "\t" + 
						"keyID=" + Long.toHexString(k.getKeyID()) + "\t" + 
						"userIds=" + userIds + "\t" + 
						"encryptionKey=" + k.isEncryptionKey() + "\t" +
						"masterKey=" + k.isMasterKey() + "\t" +
						"revoked=" + k.isRevoked()
						);
			}
		}
	}

	static void dumpSecretKey(InputStream in) throws IOException, PGPException {
		in = PGPUtil.getDecoderStream(in);

		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(in);

		Iterator<?> rIt = pgpSec.getKeyRings();

		while (rIt.hasNext()) {
			PGPSecretKeyRing kRing = (PGPSecretKeyRing) rIt.next();
			Iterator<?> kIt = kRing.getSecretKeys();

			while (kIt.hasNext()) {
				PGPSecretKey k = (PGPSecretKey) kIt.next();
				
				String userIds = "";
				for (Iterator<?> it = k.getUserIDs(); it.hasNext();) {
					userIds += it.next() + ", ";
				}
				
				System.out.println(
						"algorithm=" + k.getKeyEncryptionAlgorithm() + "\t" + 
						"keyID=" + Long.toHexString(k.getKeyID()) + "\t" + 
						"userIds=" + userIds + "\t" + 
						"signingKey=" + k.isSigningKey()
						);
			}
		}
	}

	public static String getAlgorithm(int algId) {
		switch (algId) {
		case PublicKeyAlgorithmTags.RSA_GENERAL:
			return "RSA_GENERAL";
		case PublicKeyAlgorithmTags.RSA_ENCRYPT:
			return "RSA_ENCRYPT";
		case PublicKeyAlgorithmTags.RSA_SIGN:
			return "RSA_SIGN";
		case PublicKeyAlgorithmTags.ELGAMAL_ENCRYPT:
			return "ELGAMAL_ENCRYPT";
		case PublicKeyAlgorithmTags.DSA:
			return "DSA";
		case PublicKeyAlgorithmTags.EC:
			return "EC";
		case PublicKeyAlgorithmTags.ECDSA:
			return "ECDSA";
		case PublicKeyAlgorithmTags.ELGAMAL_GENERAL:
			return "ELGAMAL_GENERAL";
		case PublicKeyAlgorithmTags.DIFFIE_HELLMAN:
			return "DIFFIE_HELLMAN";
		}

		return "unknown";
	}
}
