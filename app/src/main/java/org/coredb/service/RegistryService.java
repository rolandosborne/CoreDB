package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.ForbiddenException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.codec.DecoderException;
import java.nio.charset.StandardCharsets;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import java.lang.IllegalArgumentException;
import org.coredb.api.NotFoundException;

import org.springframework.core.io.InputStreamResource;

import org.coredb.model.AmigoMessage;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLogin;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountMessage;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountLoginRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountMessageRepository;

@Service
public class RegistryService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountLoginRepository loginRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountMessageRepository accountMessageRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public String getAccountId(String username) throws NotFoundException {

    // retrieve account
    AccountLogin login = loginRepository.findOneByUsername(username);
    if(login == null) {
      throw new NotFoundException(404, "Amigo not found");
    }

    return login.getAccount().getAmigoId();
  }

  private Account getAccount(String amigoId, String username) throws NotFoundException {
 
    // retrieve account
    Account account = null;
    if(amigoId != null) {
      account = accountRepository.findOneByEmigoId(amigoId);
    }
    else if(username != null) {
      AccountLogin login = loginRepository.findOneByUsername(username);
      account = login.getAccount();
    }
    if(account == null) {
      throw new NotFoundException(404, "Amigo not found");
    }

    return account; 
  }

  private EmigoEntity getEmigoEntity(String amigoId, String username) throws NotFoundException {

    // retrieve account
    Account account = getAccount(amigoId, username);

    // retrieve emigo entity from account
    EmigoEntity emigo = emigoEntityRepository.findOneByEmigoId(account.getEmigoId());
    if(emigo == null) {
      throw new NotFoundException(404, "Emigo not found");
    }
    return emigo;
  }

  public InputStream getLogo(String amigoId, String username) throws NotFoundException {

    // get amigo data for account    
    EmigoEntity emigo = getEmigoEntity(amigoId, username);
    if(emigo.getLogo() == null) {
      throw new NotFoundException(404, "Image Not found");
    }    

    // extract image data
    try {
      byte[] bytes = Base64.getDecoder().decode(emigo.getLogo());
      return new ByteArrayInputStream(bytes);
    }
    catch(Exception e) {
      throw new NotFoundException(404, "Valid Image not found");
    }
  }

  public String getName(String amigoId, String username) throws NotFoundException {
    
    // get amigo name from entity
    return getEmigoEntity(amigoId, username).getName();
  }

  public Integer getRevision(String amigoId, String username) throws NotFoundException {

    // get amigo revision from entity
    return getEmigoEntity(amigoId, username).getRevision();
  } 

  public AmigoMessage getMessage(String amigoId, String username) throws NotFoundException {

    // get account entity
    Account account = getAccount(amigoId, username);
    AmigoMessage message = accountMessageRepository.findOneByAccount(account);
    if(message == null) {
      throw new NotFoundException(404, "Account message not found");
    }
    return message;
  }
    
}
