package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KeyStoreService {

  @Value("${keystore.path}")
  private String path;

  public KeyPair get(String eid) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    FileInputStream fis;

    // read in public key
		File filePublicKey = new File(path + "/" + eid + ".pubkey");
		fis = new FileInputStream(path + "/" + eid + ".pubkey");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();
 
    // read in public key algorithm
		File filePublicAlg = new File(path + "/" + eid + ".pubalg");
		fis = new FileInputStream(path + "/" + eid + ".pubalg");
		byte[] publicAlg = new byte[(int) filePublicAlg.length()];
		fis.read(publicAlg);
		fis.close();
 
		// read in private key
		File filePrivateKey = new File(path + "/" + eid + ".privkey");
		fis = new FileInputStream(path + "/" + eid + ".privkey");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();
 
		// read in private key algorithm
		File filePrivateAlg = new File(path + "/" + eid + ".privalg");
		fis = new FileInputStream(path + "/" + eid + ".privalg");
		byte[] privateAlg = new byte[(int) filePrivateAlg.length()];
		fis.read(privateAlg);
		fis.close();
 
		// extract public key object
		KeyFactory publicKeyFactory = KeyFactory.getInstance(new String(publicAlg));
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);
 
    // extract private key object
    KeyFactory privateKeyFactory = KeyFactory.getInstance(new String(privateAlg));
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);
 
		return new KeyPair(publicKey, privateKey);
  }

  public void set(String eid, KeyPair key) throws IOException {
    FileOutputStream fos;
		PrivateKey privateKey = key.getPrivate();
		PublicKey publicKey = key.getPublic();
 
		// write public key to path
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		fos = new FileOutputStream(path + "/" + eid + ".pubkey");
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
    fos = new FileOutputStream(path + "/" + eid + ".pubalg");
    fos.write(publicKey.getAlgorithm().getBytes());
    fos.close();
 
		// write private key to path
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		fos = new FileOutputStream(path + "/" + eid + ".privkey");
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
    fos = new FileOutputStream(path + "/" + eid + ".privalg");
    fos.write(privateKey.getAlgorithm().getBytes());
    fos.close();
  }
}
