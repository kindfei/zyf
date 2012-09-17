package test.pgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;

/**
 * 
 * @author yz69579
 *
 */
public class PGPHandler {
	private final static Log log = LogFactory.getLog(PGPHandler.class);
	
	private final static String providerName = "BC";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private String pubring;
	private String secring;
	
	private boolean cacheKeyRing;
	
	public String getPubring() {
		return pubring;
	}

	public void setPubring(String pubring) {
		this.pubring = pubring;
	}

	public String getSecring() {
		return secring;
	}

	public void setSecring(String secring) {
		this.secring = secring;
	}

	public boolean isCacheKeyRing() {
		return cacheKeyRing;
	}

	public void setCacheKeyRing(boolean cacheKeyRing) {
		this.cacheKeyRing = cacheKeyRing;
	}

	private byte[] pubringCache;
	private byte[] secringCache;
	
	public void init() throws IOException {
		if (cacheKeyRing) {
			pubringCache = readKey(pubring);
			secringCache = readKey(secring);
		}
	}
	
	private byte[] readKey(String fileName) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		FileInputStream in = new FileInputStream(fileName);
		byte[] buf = new byte[1024];
		
		for (int len; (len = in.read(buf)) > 0;) {
			out.write(buf, 0, len);
		}
		
		in.close();
		
		return out.toByteArray();
	}
	
	private InputStream pubKeyInput() throws FileNotFoundException {
		if (pubringCache != null) {
			return new ByteArrayInputStream(pubringCache);
		}
		return new FileInputStream(pubring);
	}
	
	private InputStream secKeyInput() throws FileNotFoundException {
		if (secringCache != null) {
			return new ByteArrayInputStream(secringCache);
		}
		return new FileInputStream(secring);
	}
	
	/**
	 * 
	 * @param out
	 * @param inFileName
	 * @param userId
	 * @param passphrase
	 * @param armor
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws PGPException
	 * @throws SignatureException
	 */
	public void signFile(OutputStream out, String inFileName, String userId, String passphrase, boolean armor)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, PGPException, SignatureException {
		if (armor) {
			out = new ArmoredOutputStream(out);
		}

		/*
		 * Initialize signature generator
		 */
		PGPSecretKey secKey = readSecretKey(userId);
		PGPPrivateKey priKey = secKey.extractPrivateKey(passphrase.toCharArray(), providerName);
		PGPSignatureGenerator sg = new PGPSignatureGenerator(secKey.getPublicKey().getAlgorithm(), PGPUtil.MD5, providerName);

		sg.initSign(PGPSignature.BINARY_DOCUMENT, priKey);

		Iterator<?> it = secKey.getPublicKey().getUserIDs();
		if (it.hasNext()) {
			PGPSignatureSubpacketGenerator ssg = new PGPSignatureSubpacketGenerator();

			ssg.setSignerUserID(false, (String) it.next());
			sg.setHashedSubpackets(ssg.generate());
		}

		/*
		 * Compress data
		 */
		PGPCompressedDataGenerator cdg = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
		BCPGOutputStream bcOut = new BCPGOutputStream(cdg.open(out));

		/*
		 * Encode signature
		 */
		File file = new File(inFileName);
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		for (int ch = 0; (ch = in.read()) >= 0;) {
			bOut.write(ch);
			sg.update((byte) ch);
		}
		in.close();

		sg.generate().encode(bcOut);

		/*
		 * Output literal data
		 */
		PGPLiteralDataGenerator ldg = new PGPLiteralDataGenerator();
		OutputStream lOut = ldg.open(bcOut, PGPLiteralData.BINARY, file);
		lOut.write(bOut.toByteArray());
		ldg.close();

		cdg.close();
		out.close();
	}

	/**
	 * 
	 * @param out
	 * @param inFileName
	 * @param userId
	 * @param armor
	 * @param withIntegrityCheck
	 * @param sign
	 * @param signatureUserId
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws PGPException
	 */
	public void encryptFile(OutputStream out, String inFileName, String userId, boolean armor, boolean withIntegrityCheck,
			boolean sign, String signatureUserId, String passphrase) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, PGPException {
		if (armor) {
			out = new ArmoredOutputStream(out);
		}
		
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		/*
		 * Compress and sign data
		 */
		if (sign) {
			signFile(bOut, inFileName, signatureUserId, passphrase, false);
		} else {
			PGPCompressedDataGenerator cdg = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
			PGPUtil.writeFileToLiteralData(cdg.open(bOut), PGPLiteralData.BINARY, new File(inFileName));
			cdg.close();
		}

		/*
		 * Encrypt data
		 */
		PGPEncryptedDataGenerator edg = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, withIntegrityCheck, new SecureRandom(), providerName);
		List<PGPPublicKey> pubKeys = readPublicKey(userId);
		for (PGPPublicKey pubKey : pubKeys) {
			edg.addMethod(pubKey);
		}

		byte[] bytes = bOut.toByteArray();
		OutputStream cOut = edg.open(out, bytes.length);
		cOut.write(bytes);

		cOut.close();
		out.close();
	}

	/**
	 * 
	 * @param outFileName
	 * @param in
	 * @return
	 * @throws PGPException
	 * @throws IOException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 */
	public boolean decryptAndVerifyFile(String outFileName, InputStream in, String passphrase)
			throws PGPException, IOException, NoSuchProviderException, SignatureException {
		boolean r = true;
		
		in = PGPUtil.getDecoderStream(in);

		PGPObjectFactory factory = new PGPObjectFactory(in);
		Object o = factory.nextObject();

		/*
		 * Decrypt and Uncompress data
		 */
		if (o instanceof PGPCompressedData) {
			factory = new PGPObjectFactory(((PGPCompressedData) o).getDataStream());
			o = factory.nextObject();
		} else {
			PGPEncryptedDataList edl;
			
			// the first object might be a PGP marker packet.
			if (o instanceof PGPEncryptedDataList) {
				edl = (PGPEncryptedDataList) o;
			} else {
				edl = (PGPEncryptedDataList) factory.nextObject();
			}
			
			// Decrypted data with secret key
			PGPPrivateKey priKey = null;
			PGPPublicKeyEncryptedData pked = null;
			
			PGPSecretKeyRingCollection skrc = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(secKeyInput()));

			for (Iterator<?> it = edl.getEncryptedDataObjects(); priKey == null && it.hasNext();) {
				pked = (PGPPublicKeyEncryptedData) it.next();
				PGPSecretKey secKey = skrc.getSecretKey(pked.getKeyID());
				if (secKey != null) {
					priKey = secKey.extractPrivateKey(passphrase.toCharArray(), providerName);
				}
			}

			if (priKey == null) {
				throw new IllegalArgumentException("secret key for message not found.");
			}

			factory = new PGPObjectFactory(pked.getDataStream(priKey, providerName));
			o = factory.nextObject();
			
			if (o instanceof PGPCompressedData) {
				factory = new PGPObjectFactory(((PGPCompressedData) o).getDataStream());
				o = factory.nextObject();
			}

			if (pked.isIntegrityProtected()) {
				if (!pked.verify()) {
					log.info("message failed integrity check");
					r = false;
				} else {
					log.info("message integrity check passed");
				}
			} else {
				log.info("no message integrity check");
			}
		}
		
		/*
		 * Verify and Output data
		 */
		PGPOnePassSignatureList opsl = null;
		PGPSignatureList sl = null;
		PGPLiteralData ld = null;
		
		if (o instanceof PGPLiteralData) {
			ld = (PGPLiteralData) o;
		} else if (o instanceof PGPOnePassSignatureList) {
			opsl = (PGPOnePassSignatureList) o;
			ld = (PGPLiteralData) factory.nextObject();
		} else if (o instanceof PGPSignatureList) {
			sl = (PGPSignatureList) o;
			ld = (PGPLiteralData) factory.nextObject();
		} else {
			throw new PGPException("message is not a simple encrypted file - type unknown.");
		}
		
		if (outFileName == null) {
			outFileName = ld.getFileName();
		}
		FileOutputStream out = new FileOutputStream(outFileName);
		
		if (opsl != null) {
			r = verifyFile(factory, opsl, ld, out);
		} else if (sl != null) {
			r = verifyFile(sl, ld, out);
		} else {
			InputStream unc = ld.getInputStream();
			for (int ch; (ch = unc.read()) >= 0;) {
				out.write(ch);
			}
			out.close();
		}
		
		return r;
	}
	
	private boolean verifyFile(PGPObjectFactory factory, PGPOnePassSignatureList opsl, PGPLiteralData ld, 
			FileOutputStream out) throws NoSuchProviderException, PGPException, SignatureException, IOException {
		log.info("verfy with PGPOnePassSignatureList");
		PGPPublicKeyRingCollection pkrc = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(pubKeyInput()));
		
		PGPOnePassSignature ops = opsl.get(0);
		ops.initVerify(pkrc.getPublicKey(ops.getKeyID()), providerName);
		InputStream in = ld.getInputStream();
		for (int ch; (ch = in.read()) >= 0;) {
			ops.update((byte) ch);
			out.write(ch);
		}
		out.close();
		
		if (ops.verify(((PGPSignatureList) factory.nextObject()).get(0))) {
			log.info("signature verified.");
			return true;
		} else {
			log.info("signature verification failed.");
			return false;
		}
	}
	
	private boolean verifyFile(PGPSignatureList sl, PGPLiteralData ld, FileOutputStream out) 
			throws NoSuchProviderException, PGPException, SignatureException, IOException {
		log.info("verfy with PGPSignatureList");
		PGPPublicKeyRingCollection pkrc = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(pubKeyInput()));
		
		PGPSignature s = sl.get(0);
		s.initVerify(pkrc.getPublicKey(s.getKeyID()), providerName);
		InputStream in = ld.getInputStream();
		for (int ch; (ch = in.read()) >= 0;) {
			s.update((byte) ch);
			out.write(ch);
		}
		out.close();

		if (s.verify()) {
			log.info("signature verified.");
			return true;
		} else {
			log.info("signature verification failed.");
			return false;
		}
	}

	private List<PGPPublicKey> readPublicKey(String userId) throws IOException, PGPException {
		List<PGPPublicKey> r = new ArrayList<PGPPublicKey>();
		
		PGPPublicKeyRingCollection pkrc = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(pubKeyInput()));

		for (Iterator<?> rIt = pkrc.getKeyRings(); rIt.hasNext();) {
			PGPPublicKeyRing pkr = (PGPPublicKeyRing) rIt.next();

			boolean match = false;
			for (Iterator<?> kIt = pkr.getPublicKeys(); kIt.hasNext();) {
				PGPPublicKey k = (PGPPublicKey) kIt.next();
				if (!match) {
					for (Iterator<?> it = k.getUserIDs(); it.hasNext();) {
						if (((String) it.next()).contains(userId)) {
							match = true;
							break;
						}
					}
					if (!match) {
						break;
					}
				}
				if (match && k.isEncryptionKey()) {
					r.add(k);
				}
			}
		}
		
		if (r.size() == 0) {
			throw new IllegalArgumentException("Can't find encryption key in key ring. userId=" + userId);
		}
		
		return r;
	}

	private PGPSecretKey readSecretKey(String userId) throws IOException, PGPException {
		PGPSecretKeyRingCollection skrc = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(secKeyInput()));

		for (Iterator<?> rIt = skrc.getKeyRings(); rIt.hasNext();) {
			PGPSecretKeyRing skr = (PGPSecretKeyRing) rIt.next();

			for (Iterator<?> kIt = skr.getSecretKeys(); kIt.hasNext();) {
				PGPSecretKey sk = (PGPSecretKey) kIt.next();
				
				for (Iterator<?> it = sk.getUserIDs(); it.hasNext();) {
					if (((String) it.next()).contains(userId) && sk.isSigningKey()) {
						return sk;
					}
				}
			}
		}

		throw new IllegalArgumentException("Can't find signing key in key ring. userId=" + userId);
	}

}
