package org.coredb.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.nio.file.AccessDeniedException;
import javax.ws.rs.NotAcceptableException;

import static org.coredb.NameRegistry.*;
import org.coredb.service.ConfigService.ConfigServiceValue;
import org.coredb.service.util.SecurityUtil;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountAppRepository;
import org.coredb.jpa.repository.AccountShareRepository;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.model.ShareEntry.StatusEnum;

@Service
public class AuthService {

  @Autowired
  private EmigoEntityRepository emigoEntryRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AccountAppRepository accountAppRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public AccountShare getAccountShare(String token) throws InvalidParameterException {

    // load share connection
    AccountShare share = shareRepository.findOneByInToken(token);
    if(share == null) {
      throw new InvalidParameterException("share connection not found");
    }
    if(share.getStatus() != StatusEnum.CONNECTED) {
      throw new InvalidParameterException("share connection not established");
    }
    return share;
  }

  public AccountApp getAccountApp(String token) throws InvalidParameterException, NotAcceptableException {
 
    // find account service by token
    AccountApp service = accountAppRepository.findOneByToken(token);
    if(service == null) {
      throw new InvalidParameterException("invalid service token");
    }
    if(!service.getAccount().getEnabled()) {
      throw new NotAcceptableException("account disabled");
    }
    return service;
  }

  public void accessToken(String token) throws AccessDeniedException {
    compareToken(SC_ACCESS_TOKEN, token);
  }

  public void configToken(String token) throws AccessDeniedException {
    compareToken(SC_CONFIG_TOKEN, token);
  }

  public void statToken(String token) throws AccessDeniedException {
    compareToken(SC_STAT_TOKEN, token);
  }

  public void accountToken(String token) throws AccessDeniedException {
    compareToken(SC_ACCOUNT_TOKEN, token);
  }

  public void registryToken(String token) throws AccessDeniedException {
    compareToken(SC_REGISTRY_TOKEN, token);
  }
  
  private void compareToken(String id, String token) throws InvalidParameterException {

    // compare with stored  token
    ConfigServiceValue config = configService.getServerConfig(id);

    if(config == null || config.strValue == null) {
      throw new InvalidParameterException("admin token not set");
    }
    if(!config.strValue.equals(token)) {
      throw new InvalidParameterException("incorrect admin token");
    }
  }

  public AccountApp indexService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);    
    if(!accountApp.getAuthorizedIndex()) {
      throw new AccessDeniedException("index permission denied for [" + token + "]");
    }
    return accountApp;
  }
  
  public AccountApp groupService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedGroup()) {
      throw new AccessDeniedException("group permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp profileService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedProfile()) {
      throw new AccessDeniedException("profile permission denied for [" + token + "]");
    }
    return accountApp;
  }
  
  public AccountApp identityService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedIdentity()) {
      throw new AccessDeniedException("identity permission denied for [" + token + "]");
    }
    return accountApp;
  }
  
  public AccountApp userService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedUser()) {
      throw new AccessDeniedException("user permission denied for [" + token + "]");
    }
    return accountApp;
  }
 
  public AccountApp appService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedApp()) {
      throw new AccessDeniedException("service permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp accessService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedAccess()) {
      throw new AccessDeniedException("access permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp accountService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedAccount()) {
      throw new AccessDeniedException("group permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp promptService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedPrompt()) {
      throw new AccessDeniedException("pormpt permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp shareService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedShare()) {
      throw new AccessDeniedException("share permission denied for [" + token + "]");
    }
    return accountApp;
  }

  public AccountApp showService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedShow()) {
      throw new AccessDeniedException("show permission denied for [" + token + "]");
    }
    return accountApp;
  }
 
  public AccountApp conversationService(String token) throws AccessDeniedException, NotAcceptableException, InvalidParameterException {
 
    // validate permission
    AccountApp accountApp = getAccountApp(token);
    if(!accountApp.getAuthorizedConversation()) {
      throw new AccessDeniedException("conversation permission denied for [" + token + "]");
    }
    return accountApp;
  }
      
}
    

