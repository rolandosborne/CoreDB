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

import org.coredb.model.AmigoMessage;
import org.coredb.model.ShareMessage;
import org.coredb.model.Amigo;
import org.coredb.model.Share;
import org.coredb.model.Share.*;
import org.coredb.service.util.SecurityUtil;

public class ShareUtil {

  public static ShareData getObject(ShareMessage msg) 
        throws IllegalArgumentException, Exception {

    // validate key attribute
    if(msg.getAmigo() == null || msg.getAmigo().getKey() == null || msg.getAmigo().getKeyType() == null) {
      throw new IllegalArgumentException("invalid key attribute");
    }
    PublicKey key = SecurityUtil.readPubkey(msg.getAmigo().getKey(), msg.getAmigo().getKeyType());

    Share share;
    if(msg.getOpen() != null) {

      // validate signature
      if(SecurityUtil.verify(msg.getOpen(), msg.getSignature(), key) != true) {
        throw new IllegalArgumentException("invalid share message signature");
      }

      // validate request action
      share = decode(msg.getOpen());
      if(share.getAction() != ActionEnum.OPEN) {
        throw new IllegalArgumentException("invalid message action");
      }

      // request must contain token
      if(share.getToken() == null) {
        throw new IllegalArgumentException("missing token from request");
      }
    }
    else if(msg.getClose() != null) {

      // validate signature
      if(SecurityUtil.verify(msg.getClose(), msg.getSignature(), key) != true) {
        throw new IllegalArgumentException("invalid share message signature");
      }

      // validate deny action
      share = decode(msg.getClose());
      if(share.getAction() != ActionEnum.CLOSE) {
        throw new IllegalArgumentException("invalid message action");
      }
    }
    else {
      throw new IllegalArgumentException("no action specified");
    }

    // validate share messgae share   
    if(share.getAmigoId() == null || share.getExpires() == null || share.getIssued() == null) {
      throw new IllegalArgumentException("missing field in share message");
    }

    // validate emigo
    Amigo emigo = EmigoUtil.getObject(msg.getAmigo());

    return new ShareData(share, emigo, msg.getAmigo());
  }

  public static ShareMessage getMessage(KeyPair key, Share share, AmigoMessage emigo) 
        throws IllegalArgumentException, Exception {
    
    String enc = encode(share);
    String sig = SecurityUtil.sign(enc, key.getPrivate());
    ShareMessage msg = new ShareMessage();
    if(share.getAction() == ActionEnum.OPEN) {
      msg.setOpen(enc);
    }
    else if(share.getAction() == ActionEnum.CLOSE) {
      msg.setClose(enc);
    }
    else {
      throw new IllegalArgumentException("unknown message type");
    }
    msg.setSignature(sig);
    msg.setAmigo(emigo);
    return msg;
  }

  private static String encode(Share share) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    String serial = mapper.writeValueAsString(share);
    byte[] bytes = serial.getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(bytes);
  }

  private static Share decode(String base) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(base);
    String serial = new String(bytes);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serial, Share.class);
  }
}
    

