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

import org.coredb.model.Token;
import org.coredb.model.AmigoToken;
import org.coredb.model.AmigoMessage;
import org.coredb.model.LinkMessage;
import org.coredb.model.Amigo;
import org.coredb.model.CreateLink;
import org.coredb.model.AttachLink;
import org.coredb.service.util.SecurityUtil;

public class LinkUtil {

  private static PublicKey getKey(AmigoMessage emigo) throws Exception {
    
    // validate emigo key
    if(emigo == null || emigo.getKey() == null || emigo.getKeyType() == null) {
      throw new IllegalArgumentException("invalid emigo key");
    }
    return SecurityUtil.readPubkey(emigo.getKey(), emigo.getKeyType());
  }

  public static Token getTokenObject(AmigoToken tok) throws IllegalArgumentException, Exception {
   
    // extract public key
    PublicKey key = LinkUtil.getKey(tok.getAmigo());

    // validate data signature
    if(tok.getToken() == null) {
      throw new IllegalArgumentException("token parameters not set");
    }
    if(SecurityUtil.verify(tok.getToken(), tok.getSignature(), key) != true) {
      throw new IllegalArgumentException("invalid token signature");
    } 

    // decode token object
    byte[] bytes = Base64.getDecoder().decode(tok.getToken());
    String serial = new String(bytes);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serial, Token.class);
  } 

  public static CreateLink getCreateObject(LinkMessage msg) throws IllegalArgumentException, Exception {

    // extract public key
    PublicKey key = LinkUtil.getKey(msg.getAmigo());

    // validate data signature
    if(msg.getCreate() == null) {
      throw new IllegalArgumentException("create paratmers not set");
    }
    if(SecurityUtil.verify(msg.getCreate(), msg.getSignature(), key) != true) {
      throw new IllegalArgumentException("invalid link message signature");
    }

    // decode link object
    byte[] bytes = Base64.getDecoder().decode(msg.getCreate());
    String serial = new String(bytes);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serial, CreateLink.class);
  }

  public static AttachLink getAttachObject(LinkMessage msg) throws IllegalArgumentException, Exception {
   
    // extract public key
    PublicKey key = LinkUtil.getKey(msg.getAmigo());
    
    // validate data signature
    if(msg.getAttach() == null) {
      throw new IllegalArgumentException("attach paramters not set");
    }
    if(SecurityUtil.verify(msg.getAttach(), msg.getSignature(), key) != true) {
      throw new IllegalArgumentException("invalid link message signature");
    }

    // decode link object
    byte[] bytes = Base64.getDecoder().decode(msg.getAttach());
    String serial = new String(bytes);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serial, AttachLink.class);
  }

  public static AmigoToken getMessage(String emigoId, KeyPair key, Token token, AmigoMessage emigo) throws Exception {
    
    // serialize and sign data
    String enc = LinkUtil.<Token>encode(token);
    String sig = SecurityUtil.sign(enc, key.getPrivate());
    
    // construct message
    AmigoToken msg = new AmigoToken();
    msg.setAmigoId(emigoId);
    msg.setAmigo(emigo);
    msg.setSignature(sig);
    msg.setToken(enc);
    return msg;
  }

  public static LinkMessage getMessage(KeyPair key, CreateLink link, AmigoMessage emigo) throws Exception {
   
    // serialize and sign data 
    String enc = LinkUtil.<CreateLink>encode(link);
    String sig = SecurityUtil.sign(enc, key.getPrivate());

    // construct message
    LinkMessage msg = new LinkMessage();
    msg.setCreate(enc);
    msg.setSignature(sig);
    msg.setAmigo(emigo);
    return msg;
  }

  public static LinkMessage getMessage(KeyPair key, AttachLink link, AmigoMessage emigo) throws Exception {

    // serialize and sign data
    String enc = LinkUtil.<AttachLink>encode(link);
    String sig = SecurityUtil.sign(enc, key.getPrivate());

    // construct message
    LinkMessage msg = new LinkMessage();
    msg.setAttach(enc);
    msg.setSignature(sig);
    msg.setAmigo(emigo);
    return msg;
  }

  private static <T> String encode(T link) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    String serial = mapper.writeValueAsString(link);
    byte[] bytes = serial.getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(bytes);
  }

}
    

