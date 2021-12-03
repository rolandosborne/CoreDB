package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.time.Instant;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import java.nio.charset.StandardCharsets;

import java.lang.UnsupportedOperationException;
import java.lang.IllegalArgumentException;
import org.coredb.api.NotFoundException;
import java.lang.IllegalStateException;

import static org.coredb.NameRegistry.*;

import org.coredb.jpa.repository.AccountUserAgentRepository;
import org.coredb.jpa.repository.AccountAppAgentRepository;
import org.coredb.jpa.repository.AccountUserRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.AppEntityRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountUserAgentRepository;
import org.coredb.jpa.repository.AccountShareRepository;

import org.coredb.jpa.entity.AccountShare;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.AccountUser;
import org.coredb.jpa.entity.AccountUserAgent;
import org.coredb.jpa.entity.AccountAppAgent;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.entity.AppEntity;

import org.coredb.model.Auth;
import org.coredb.model.AuthMessage;
import org.coredb.model.AmigoMessage;
import org.coredb.model.ShareEntry.StatusEnum;

import org.coredb.service.util.AuthUtil;
import org.coredb.service.util.SecurityUtil;
import org.coredb.service.util.AuthData;

@Service
public class AgentService {

  @Autowired
  private KeyStoreService keyStoreService;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AccountMessageRepository accountMessageRepository;

  @Autowired
  private AppEntityRepository appEntityRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AccountUserRepository accountUserRepository;

  @Autowired
  private AccountAppAgentRepository accountAppAgentRepository;

  @Autowired
  private AccountUserAgentRepository accountUserAgentRepository;

  private AccountUser getAccountUser(String token) 
      throws NotFoundException, IllegalStateException {
  
    // retrieve entries with token
    AccountUser user = accountUserRepository.findOneByServiceToken(token);
    if(user == null) {
      throw new NotFoundException(404, "service token not found");
    }
    return user;
  }

  private AccountMessage getAccountMessage(Account account) throws NotFoundException {
    
    // retrieve entries for account
    AccountMessage message = accountMessageRepository.findOneByAccount(account);
    if(message == null) {
      throw new NotFoundException(404, "account message note found");
    }
    return message;
  }

  private AuthMessage updateAuthMessage(AccountUserAgent agent) throws NotFoundException, Exception {

    // get emigo id
    Account account = agent.getUser().getAccount();
    String eid = account.getEmigoId(); 

    // retrieve emigo message
    AmigoMessage emigo = getAccountMessage(account);

    // retrieve key pair
    KeyPair keyPair = keyStoreService.get(eid);

    // compute auth window
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(eid, AC_AUTH_MESSAGE_EXPIRE, (long)2592000);

    // construct auth object
    Auth auth = new Auth();
    auth.setAmigoId(agent.getUser().getAmigoId());
    auth.setIssued(issued);
    auth.setExpires(expires);
    auth.setToken(SecurityUtil.token());    

    // generate message
    AuthMessage message = AuthUtil.getMessage(keyPair, auth, emigo);

    // save entry
    agent.setMessage(message.getData());
    agent.setSignature(message.getSignature());
    agent.setIssued(auth.getIssued());
    agent.setExpires(auth.getExpires());
    agent.setToken(auth.getToken());
    accountUserAgentRepository.save(agent);

    // return message
    return message;
  }

  private AuthMessage addAuthMessage(AccountUser user) throws NotFoundException, Exception {

    // get emigo id
    Account account = user.getAccount();
    String eid = account.getEmigoId(); 

    // retrieve emigo message
    AmigoMessage emigo = getAccountMessage(account);

    // retrieve key pair
    KeyPair keyPair = keyStoreService.get(eid);

    // compute auth window
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(eid, AC_AUTH_MESSAGE_EXPIRE, (long)2592000);

    // construct auth object
    Auth auth = new Auth();
    auth.setAmigoId(user.getEmigoId());
    auth.setIssued(issued);
    auth.setExpires(expires);
    auth.setToken(SecurityUtil.token());    

    // generate message
    AuthMessage message = AuthUtil.getMessage(keyPair, auth, emigo);

    // save entry
    AccountUserAgent agent = new AccountUserAgent();
    agent.setUser(user);
    agent.setMessage(message.getData());
    agent.setSignature(message.getSignature());
    agent.setIssued(auth.getIssued());
    agent.setExpires(auth.getExpires());
    agent.setToken(auth.getToken());
    accountUserAgentRepository.save(agent);

    // return message
    return message;
  }

  private AuthMessage getAuthMessage(AccountUserAgent agent) throws NotFoundException, Exception {

    // retrieve emig message
    AmigoMessage emigo = getAccountMessage(agent.getUser().getAccount());

    // construct auth message
    AuthMessage message = new AuthMessage();
    message.setAmigo(emigo);
    message.setData(agent.getMessage());
    message.setSignature(agent.getSignature());
    return message;
  }
    
  @Transactional
  public AuthMessage setUserAccess(String token) 
      throws NotFoundException, IllegalStateException, Exception {
  
    // retrieve account user entry
    AccountUser user = getAccountUser(token);

    // validate existing user agent auth
    String eid = user.getAccount().getEmigoId();

    // check if agent entry already exists
    List<AccountUserAgent> agents = accountUserAgentRepository.findByUser(user);
    for(AccountUserAgent userAgent: agents) {

      // check expiration
      Long cur = Instant.now().getEpochSecond();
      Long reissue = cur + configService.getAccountNumValue(eid, AC_AUTH_MESSAGE_REISSUE, (long)1296000);
      if(cur >= userAgent.getIssued() && reissue <= userAgent.getExpires()) {
        return getAuthMessage(userAgent);
      }
      return updateAuthMessage(userAgent);
    }

    // generate new auth message
    return addAuthMessage(user);
  }

  @Transactional
  public Auth setAppAuth(String token, AuthMessage message) throws IllegalStateException, IllegalArgumentException, InvalidParameterException, Exception {

    // load referenced connection
    AccountShare share = shareRepository.findOneByInToken(token);
    if(share == null) {
      throw new InvalidParameterException("share connection not found");
    }
    if(share.getStatus() != StatusEnum.CONNECTED) {
      throw new InvalidParameterException("share connection not established");
    }

    // apply auth message
    return setAuth(share.getEmigo(), message);
  }

  @Transactional
  public String setAppAccess(AccountEmigo accountEmigo, AuthMessage msg) 
        throws IllegalStateException, IllegalArgumentException, Exception {
    return setAuth(accountEmigo, msg).getToken();
  }
  
  @Transactional
  public Auth setAuth(AccountEmigo accountEmigo, AuthMessage msg) 
        throws IllegalStateException, IllegalArgumentException, Exception {

    // extract data from message
    AuthData auth = AuthUtil.getObject(msg);
    String eid = accountEmigo.getAccount().getEmigoId();
    String token = auth.getAuth().getToken();

    // validate target of auth
    if(!accountEmigo.getEmigoId().equals(auth.getAuth().getAmigoId())) {
      throw new IllegalArgumentException("auth target does not match");
    }

    // validate time window
    Long cur = Instant.now().getEpochSecond();
    Long start = cur + configService.getServerNumValue(SC_AUTH_MESSAGE_SKEW, (long)15);
    Long end = cur - configService.getServerNumValue(SC_AUTH_MESSAGE_SKEW, (long)15);
    if(auth.getAuth().getIssued() > start || auth.getAuth().getExpires() < end) {
      throw new IllegalArgumentException("auth message not valid");
    }

    EmigoEntity emigoEntity = emigoEntityRepository.findOneByEmigoId(accountEmigo.getEmigoId());
    List<AccountAppAgent> agents = accountAppAgentRepository.findByEmigoAndToken(emigoEntity, token);
    if(agents.isEmpty()) {

      // make sure emigo isn't spamming
      Long agentCount = accountAppAgentRepository.countByEmigo(emigoEntity);
      if(agentCount >= configService.getAccountNumValue(eid, AC_EMIGO_AUTH_COUNT, (long)1024)) {
        throw new IllegalStateException("maximum auth tokens stored");
      }

      // check if emigo has been registered
      EmigoEntity emigo = emigoEntityRepository.findOneByEmigoId(auth.getEmigo().getAmigoId());
      if(emigo == null) {
        Long emigoCount = emigoEntityRepository.count();
        if(emigoCount >= configService.getServerNumValue(SC_EMIGO_REGISTRY_COUNT, (long)4194304)) {
          throw new IllegalStateException("maximum emgios registered");
        }
        EmigoEntity entity = new EmigoEntity(auth.getEmigo());
        emigo = emigoEntityRepository.save(entity);
      }

      // check if service has been registered
      AppEntity app = appEntityRepository.findOneByEmigoId(emigo.getEmigoId());
      if(app == null) {
        Long appCount = appEntityRepository.count();
        if(appCount >= configService.getServerNumValue(SC_APP_REGISTRY_COUNT, (long)1048576)) {
          throw new IllegalStateException("maxium apps registered");
        }

        app = new AppEntity();
        app.setEmigoId(emigo.getEmigoId());
        app.setEnabled(configService.getServerBoolValue(SC_DEFAULT_APP_ENABLE, true));
        app = appEntityRepository.save(app);
      }
      if(!app.getEnabled()) {
        throw new IllegalArgumentException("app is not allowed");
      }

      // create new agent entry
      AccountAppAgent agent = new AccountAppAgent();
      agent.setEmigo(emigoEntity);
      agent.setApp(app);
      agent.setIssued(auth.getAuth().getIssued());
      agent.setExpires(auth.getAuth().getExpires());
      agent.setToken(token);
      agent = accountAppAgentRepository.save(agent);

      // return established token
      return auth.getAuth();
    }

    // validate stored window
    if(agents.get(0).getIssued() > start || agents.get(0).getExpires() < end) {
      throw new IllegalArgumentException("auth message not valid");
    }
    return auth.getAuth();
  }

  public void checkAppToken(AccountEmigo emigo, String token)
        throws UnsupportedOperationException {

    // find entry
    List<AccountAppAgent> agents = accountAppAgentRepository.findByToken(token);
    AccountAppAgent appAgent = null;
    for(AccountAppAgent agent: agents) {
      if(agent.getEmigo().getEmigoId().equals(emigo.getEmigoId())) {
        appAgent = agent;
      }
    }
    if(appAgent == null) {
      throw new UnsupportedOperationException("agent token is not found");
    }

    // is agent blocked
    if(!appAgent.getApp().getEnabled()) {
      throw new UnsupportedOperationException("app not allowed");
    }

    // validate time window
    Long cur = Instant.now().getEpochSecond();
    Long start = cur + configService.getServerNumValue(SC_AUTH_MESSAGE_SKEW, (long)15);
    Long end = cur - configService.getServerNumValue(SC_AUTH_MESSAGE_SKEW, (long)15);
    if(appAgent.getIssued() > start || appAgent.getExpires() < end) {
      throw new UnsupportedOperationException("agent token is not valid");
    }
  }

  @Transactional
  public long clearExpired() {
   
    // delete any expired entries 
    Long cur = Instant.now().getEpochSecond();
    accountUserAgentRepository.deleteByExpiresLessThan(cur);
    accountAppAgentRepository.deleteByExpiresLessThan(cur);
    return configService.getServerNumValue(SC_AGENT_CLEAR_INTERVAL, (long)1440).longValue();
  }
}

