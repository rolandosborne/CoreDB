package org.coredb.service.util;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import java.nio.charset.StandardCharsets;

public class SecurityUtil {

  public static KeyPair gen() throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(4096, new SecureRandom());
    KeyPair pair = generator.generateKeyPair();
    return pair;
  }

  public static String writePubkey(PublicKey key) throws DecoderException {
    return Hex.encodeHexString(key.getEncoded());
  }

  public static PublicKey readPubkey(String key, String type) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
    byte[] bytes = Hex.decodeHex(key);
    KeyFactory publicKeyFactory = KeyFactory.getInstance(type);
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
    return publicKeyFactory.generatePublic(publicKeySpec);
  }

  public static String id(PublicKey publicKey) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    return Hex.encodeHexString(sha.digest(publicKey.getEncoded()));
  }

  public static String sign(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(data.getBytes(StandardCharsets.UTF_8));
    byte[] signature = privateSignature.sign();
    return Hex.encodeHexString(signature); 
  }

  public static Boolean verify(String data, String signature, PublicKey publicKey) 
        throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, DecoderException {
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(publicKey);
    publicSignature.update(data.getBytes(StandardCharsets.UTF_8));
    byte[] signatureBytes = Hex.decodeHex(signature);
    return publicSignature.verify(signatureBytes);
  }

  public static String hash(File file) throws IOException, FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    FileInputStream stream = new FileInputStream(file);
    byte[] bytes = new byte[4096];
    int count = 0;
    while((count = stream.read(bytes)) != -1) {
      digest.update(bytes, 0, count);
    }
    stream.close();
    byte[] sha = digest.digest();
    return Hex.encodeHexString(sha);
  }

  public static String hash(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] sha = digest.digest(data.getBytes(StandardCharsets.UTF_8));
    return Hex.encodeHexString(sha);
  }

  public static String token() throws Exception {
    byte[] bytes = new byte[32];
    SecureRandom rand = new SecureRandom();
    rand.nextBytes(bytes);
    return Hex.encodeHexString(bytes);
  }

  public static String pass() throws Exception {
    byte[] bytes = new byte[4];
    SecureRandom rand = new SecureRandom();
    rand.nextBytes(bytes);
    return Hex.encodeHexString(bytes);
  }

  public static String salt() throws Exception {
    return token();
  }

  public static String prepare(String password, String salt) throws DecoderException, NoSuchAlgorithmException {
    byte[] pass = password.getBytes(StandardCharsets.UTF_8);
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    sha.update(Hex.decodeHex(salt));
    return Hex.encodeHexString(sha.digest(pass));
  }

}

