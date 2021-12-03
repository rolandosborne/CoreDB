package org.coredb.service.util;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;

import javax.ws.rs.NotAcceptableException;

import org.coredb.model.Amigo;
import org.coredb.model.AmigoMessage;
import org.coredb.model.AmigoMessage.*;
import org.coredb.service.util.SecurityUtil;
import org.coredb.service.KeyStoreService;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountEmigo;

public class EmigoUtil {

  public static Amigo getObject(AmigoMessage msg) throws IllegalArgumentException, Exception {

    if(msg.getKey() == null || msg.getKeyType() == null || msg.getData() == null || msg.getSignature() == null) {
      throw new IllegalArgumentException("incomplete emigo message");
    }
    
    // load the public key object
    PublicKey key = SecurityUtil.readPubkey(msg.getKey(), msg.getKeyType());

    //validate signature
    if(!SecurityUtil.verify(msg.getData(), msg.getSignature(), key)) {
      throw new IllegalArgumentException("emigo signature error");
    }

    //populate data entry
    Amigo emigo = decode(msg.getData());

    //validate key and id
    if(!emigo.getAmigoId().equals(SecurityUtil.id(key))) {
      throw new IllegalArgumentException("emigo key id mismatch");
    }

    return emigo;
  }

  public static AmigoMessage getMessage(KeyPair key, Amigo emigo) throws IllegalArgumentException, Exception {
  
    // sign data
    String enc = encode(emigo);
    String sig = SecurityUtil.sign(enc, key.getPrivate());
    
    // construct message
    AmigoMessage msg = new AmigoMessage();
    msg.setKey(SecurityUtil.writePubkey(key.getPublic()));
    msg.setKeyType(key.getPublic().getAlgorithm());
    msg.setSignature(sig);
    msg.setData(enc);
    return msg;
  }

  private static String encode(Amigo emigo) throws IllegalArgumentException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(Include.NON_NULL);
      String serial = mapper.writeValueAsString(emigo);
      byte[] bytes = serial.getBytes(StandardCharsets.UTF_8);
      return Base64.getEncoder().encodeToString(bytes);
    }
    catch(Exception e) {
      throw new IllegalArgumentException("invalid emigo data'");
    }
  }

  private static Amigo decode(String base) throws IllegalArgumentException {
    try {
      byte[] bytes = Base64.getDecoder().decode(base);
      String serial = new String(bytes);
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(serial, Amigo.class);
    }
    catch(Exception e) {
      throw new IllegalArgumentException("invalid emigo message");
    }
  }

}

