package org.coredb.service;

import java.security.*;
import java.security.spec.*;
import java.io.*;

import java.util.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.springframework.core.io.InputStreamResource;

import org.coredb.model.Amigo;
import org.coredb.model.AmigoMessage;

import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;

import org.coredb.service.KeyStoreService;
import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.SecurityUtil;

@Service
public class IdentityService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountMessageRepository accountMessageRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private KeyStoreService keyStoreService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  // Transactional method must call this
  public AccountMessage setEmigo(Account account, EmigoEntity entity) throws NotFoundException, Exception {

    // load signing key
    KeyPair key = keyStoreService.get(entity.getEmigoId());
    
    // save emigo
    entity.setRevision(entity.getRevision() + 1);
    EmigoEntity emigo = emigoEntityRepository.save(entity);

    account.setIdentityRevision(entity.getRevision());
    accountRepository.save(account);
    
    // generate new message
    AmigoMessage emigoMessage = EmigoUtil.getMessage(key, emigo);

    // update message
    AccountMessage message = accountMessageRepository.findOneByAccount(account);
    if(message == null) {
      throw new NotFoundException(404, "message not found");
    }
    message.setMessage(emigoMessage.getData());
    message.setSignature(emigoMessage.getSignature());
    return accountMessageRepository.save(message);
  }

  private EmigoEntity getEmigo(Account account) throws NotFoundException {
    
    EmigoEntity emigo = emigoEntityRepository.findOneByEmigoId(account.getEmigoId());
    if(emigo == null) {
      throw new NotFoundException(404, "emigo entity not found");
    }
    return emigo;
  }

  public Amigo getIdentity(Account account) throws NotFoundException {
    return getEmigo(account);
  }

  public Integer getRevision(Account account) throws NotFoundException {
    EmigoEntity emigo = getEmigo(account);
    return emigo.getRevision();
  }

  public AmigoMessage getMessage(Account account) throws NotFoundException {
    AccountMessage message = accountMessageRepository.findOneByAccount(account);
    if(message == null) {
      throw new NotFoundException(404, "message not found");
    }
    return message;
  }

  private Boolean checkDirty(EmigoEntity entity) {
    String registry = entity.getRegistry();
    if(registry == null) {
      return false;
    }
    if(registry.equals(entity.getNode() + "/registry")) {
      return false;
    }
    return true;
  }

  @Transactional
  public AmigoMessage updateName(Account account, String value) 
        throws NotFoundException, Exception {

    // update specified name
    EmigoEntity entity = getEmigo(account);
    entity.setName(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  @Transactional
  public InputStream getLogo(Account account) throws NotFoundException {
    
    EmigoEntity entity = getEmigo(account);
    if(entity.getLogo() == null) {
      throw new NotFoundException(404, "logo not set");
    }
    try {
      byte[] bytes = Base64.getDecoder().decode(entity.getLogo());
      return new ByteArrayInputStream(bytes);
    }
    catch(Exception e) {
      throw new NotFoundException(404, "invalid logo image");
    }
  }

  @Transactional
  public AmigoMessage updateLogo(Account account, String value) 
        throws NotFoundException, Exception {

    // update specified logo
    EmigoEntity entity = getEmigo(account);
    entity.setLogo(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  @Transactional
  public AmigoMessage updateDescription(Account account, String value) 
        throws NotFoundException, Exception {

    // update specified description
    EmigoEntity entity = getEmigo(account);
    entity.setDescription(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  @Transactional
  public AmigoMessage updateLocation(Account account, String value) 
        throws NotFoundException, Exception {
  
    // update specified location
    EmigoEntity entity = getEmigo(account);
    entity.setLocation(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  @Transactional
  public AmigoMessage updateRegistry(Account account, String value) 
        throws NotFoundException, Exception {
  
    // update specified location
    EmigoEntity entity = getEmigo(account);
    entity.setRegistry(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  @Transactional
  public AmigoMessage updateHandle(Account account, String value) 
        throws NotFoundException, Exception {
  
    // update specified location
    EmigoEntity entity = getEmigo(account);
    entity.setHandle(value);
    if(checkDirty(entity)) {
      account.setDirty(true);
    } 
    accountRepository.save(account);
    return setEmigo(account, entity);
  }

  public Boolean getDirty(Account account) {
    return account.getDirty();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void setDirty(Account account, Boolean flag, Integer revision)
        throws NotFoundException, Exception {
    if(revision != null) {
      EmigoEntity entity = getEmigo(account);
      if(entity.getRevision() != revision) {
        return;
      }
    }
    account.setDirty(flag);
    accountRepository.save(account);
  }

}
