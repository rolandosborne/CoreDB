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
import org.coredb.model.Auth;
import org.coredb.model.AuthMessage;
import org.coredb.model.AmigoMessage;
import org.coredb.service.util.SecurityUtil;

public class AuthUtil {

  public static AuthData getObject(AuthMessage msg) 
        throws IllegalArgumentException, Exception {
  
    // validate key attribute
    if(msg.getAmigo() == null || msg.getAmigo().getKey() == null || msg.getAmigo().getKeyType() == null) {
      throw new IllegalArgumentException("invalid key attribute");
    }
    PublicKey key = SecurityUtil.readPubkey(msg.getAmigo().getKey(), msg.getAmigo().getKeyType());

    // validate signature
    if(SecurityUtil.verify(msg.getData(), msg.getSignature(), key) != true) {
      throw new IllegalArgumentException("invalid share message signature");
    }

    // validate emigo message
    Amigo emigo = EmigoUtil.getObject(msg.getAmigo());

    // validate request
    Auth auth = decode(msg.getData());

    return new AuthData(emigo, auth);
  }

  public static AuthMessage getMessage(KeyPair key, Auth auth, AmigoMessage emigo) 
        throws IllegalArgumentException, Exception {

    String enc = encode(auth);
    String sig = SecurityUtil.sign(enc, key.getPrivate());
    AuthMessage msg = new AuthMessage();
    msg.setAmigo(emigo);
    msg.setData(enc);
    msg.setSignature(sig);
    return msg;
  }

  private static String encode(Auth auth) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    String serial = mapper.writeValueAsString(auth);
    byte[] bytes = serial.getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(bytes);
  }

  private static Auth decode(String base) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(base);
    String serial = new String(bytes);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serial, Auth.class);
  }

}
    

