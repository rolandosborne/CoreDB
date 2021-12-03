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

import org.coredb.model.Token;
import org.coredb.model.Pass;
import org.coredb.model.Amigo;
import org.coredb.model.AmigoToken;
import org.coredb.model.AmigoMessage;
import org.coredb.model.ServiceAccess;
import org.coredb.model.LinkMessage;
import org.coredb.model.CreateLink;
import org.coredb.model.AttachLink;
import org.coredb.model.UserEntry;
import org.coredb.model.AccountStatus;
import org.coredb.model.AccountEntry;

import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;
import java.lang.IllegalStateException;
import java.io.IOException;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AppEntity;
import org.coredb.jpa.entity.AccountUser;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.entity.AccountPass;
import org.coredb.jpa.repository.AccountUserRepository;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountAppRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.AppEntityRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountPassRepository;
import org.coredb.service.KeyStoreService;
import org.coredb.service.util.SecurityUtil;
import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.LinkUtil;
import static org.coredb.NameRegistry.*;

@Service
public class AccessService {

  @Autowired
  private KeyStoreService keyStoreService;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountAppRepository accountAppRepository;

  @Autowired
  private AccountMessageRepository accountMessageRepository;

  @Autowired
  private AccountPassRepository accountPassRepository;

  @Autowired
  private AppEntityRepository appEntityRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountUserRepository accountUserRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AccountService accountService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountMessage getAmigoMessage(Account act) throws NotFoundException {

    AccountMessage message = accountMessageRepository.findOneByAccount(act);
    if(message == null) {
      throw new NotFoundException(404, "emigo message not found for account");
    }
    return message;
  }

  private EmigoEntity setEmigo(AmigoMessage msg) throws IllegalArgumentException, IOException, Exception {

    if(msg == null) {
      throw new IllegalArgumentException("message not given");
    }

    // search for matching emigos
    EmigoEntity entry;
    Amigo service = EmigoUtil.getObject(msg);
    List<EmigoEntity> emigos = emigoEntityRepository.findByEmigoId(service.getAmigoId());
    if(emigos.isEmpty()) {

      // check if service limit reached
      Long count = appEntityRepository.count();
      if(count >= configService.getServerNumValue(SC_EMIGO_REGISTRY_COUNT, (long)1048576)) {
        throw new IOException("emigo registry limit reached");
      }

      entry = new EmigoEntity();
    }
    else {
      if(emigos.size() > 1) {
        log.error("multiple emigo entries with same id");
      }
      if(emigos.get(0).getRevision() < service.getRevision()) {
        entry = emigos.get(0);
      }
      else {
        return emigos.get(0);
      }
    }

    entry.setHandle(service.getHandle());
    entry.setName(service.getName());
    entry.setDescription(service.getDescription());
    entry.setLogo(service.getLogo());
    entry.setLocation(service.getLocation());
    entry.setEmigoId(service.getAmigoId());
    entry.setRevision(service.getRevision());
    entry.setVersion(service.getVersion());
    entry.setNode(service.getNode());
    entry.setRegistry(service.getRegistry());
    return emigoEntityRepository.save(entry);
  }

  private AppEntity setService(EmigoEntity entry) throws IOException {

    //add service entry if not present
    AppEntity entity = appEntityRepository.findOneByEmigoId(entry.getEmigoId());
    if(entity == null) {

      // check if service limit reached
      Long count = appEntityRepository.count();
      if(count >= configService.getServerNumValue(SC_APP_REGISTRY_COUNT, (long)4096)) {
        throw new IOException("account limit reached");
      }

      entity = new AppEntity();
      entity.setEmigoId(entry.getEmigoId());
      return appEntityRepository.save(entity);
    }
    return entity;
  }

  public ServiceAccess getRequestedAccess(AccountApp app) {
    return app.getServiceAccess();
  }

  public ServiceAccess getAuthorizedAccess(AccountApp app) {
    return app.getAccountAccess();
  }

  @Transactional
  public ServiceAccess setRequestedAccess(AccountApp app, ServiceAccess access) {
   
    // update account access
    app.setServiceAccess(access);
    return accountAppRepository.save(app).getServiceAccess();
  }

  @Transactional
  public ServiceAccess setAuthorizedAccess(AccountApp app, ServiceAccess access) {
 
    // update service access
    app.setAccountAccess(access);
    return accountAppRepository.save(app).getAccountAccess();
  }

  @Transactional
  public AccountEntry addAccount() throws IllegalArgumentException, Exception {

    Long count = accountRepository.count();
    if(count >= configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128)) {
      throw new IOException("account limit reached");
    }

    // generate new token
    String token = SecurityUtil.token();

    // generate key pair
    KeyPair key = SecurityUtil.gen();
    String id = SecurityUtil.id(key.getPublic());
    keyStoreService.set(id, key);

    // insert new emigo entry
    EmigoEntity emigoEntry = new EmigoEntity();
    emigoEntry.setEmigoId(id);
    emigoEntry.setRevision(1);
    emigoEntry.setRegistry(configService.getServerStrValue(SC_DEFAULT_REGISTRY, null));
    emigoEntry.setVersion(configService.getServerStrValue(SC_VERSION, "0.0.0"));
    emigoEntry.setNode(configService.getServerStrValue(SC_SERVER_HOST_PORT, null));
    emigoEntry = emigoEntityRepository.save(emigoEntry);

    // insert new service entry
    AppEntity appEntity = new AppEntity(id);
    appEntity = appEntityRepository.save(appEntity);

    // insert new account
    Account account = new Account();
    account.setEmigoId(id);
    account.setEnabled(true);
    account.setDirty(true);
    account = accountRepository.save(account);

    // update account message
    AmigoMessage msg = EmigoUtil.getMessage(key, emigoEntry);
    AccountMessage accountMessage = new AccountMessage(account, msg);
    accountMessage = accountMessageRepository.save(accountMessage);

    // insert new service
    AccountApp accountApp = new AccountApp(account, appEntity, token);
    accountApp = accountAppRepository.save(accountApp);

    // add self as user
    AccountUser user = new AccountUser(account, id, token, token);
    accountUserRepository.save(user);
      
    return account;
  }

  @Transactional
  public AmigoToken addService() throws IllegalArgumentException, Exception {

    Long count = accountRepository.count();
    if(count >= configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128)) {
      throw new IOException("account limit reached");
    }

    // generate new token
    String token = SecurityUtil.token();

    // generate key pair
    KeyPair key = SecurityUtil.gen();
    String id = SecurityUtil.id(key.getPublic());
    keyStoreService.set(id, key);

    // insert new emigo entry
    EmigoEntity emigoEntry = new EmigoEntity();
    emigoEntry.setEmigoId(id);
    emigoEntry.setRevision(1);
    emigoEntry.setRegistry(configService.getServerStrValue(SC_DEFAULT_REGISTRY, null));
    emigoEntry.setVersion(configService.getServerStrValue(SC_VERSION, "0.0.0"));
    emigoEntry.setNode(configService.getServerStrValue(SC_SERVER_HOST_PORT, null));
    emigoEntry = emigoEntityRepository.save(emigoEntry);

    // insert new service entry
    AppEntity appEntity = new AppEntity(id);
    appEntity = appEntityRepository.save(appEntity);

    // insert new account
    Account account = new Account();
    account.setEmigoId(id);
    account.setEnabled(true);
    account.setDirty(true);
    account = accountRepository.save(account);

    // update account message
    AmigoMessage msg = EmigoUtil.getMessage(key, emigoEntry);
    AccountMessage accountMessage = new AccountMessage(account, msg);
    accountMessage = accountMessageRepository.save(accountMessage);

    // insert new service
    AccountApp accountApp = new AccountApp(account, appEntity, token);
    accountApp = accountAppRepository.save(accountApp);

    // add self as user
    AccountUser user = new AccountUser(account, id, token, token);
    accountUserRepository.save(user);

    // construct response
    Token tok = new Token();
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(id, AC_LINK_MESSAGE_EXPIRE, (long)300);
    tok.setToken(token);
    tok.setIssued(issued);
    tok.setExpires(expires);
    tok.setAmigoId(id);

    return LinkUtil.getMessage(id, key, tok, accountMessage);
  }

  public AmigoToken setAccountService(String emigoId, LinkMessage msg)
      throws NotFoundException, IllegalArgumentException, AccessDeniedException, Exception {

    Account act = accountRepository.findOneByEmigoId(emigoId);
    if(act == null) {
      throw new NotFoundException(404, "account not found");
    }

    // get current time
    Long cur = Instant.now().getEpochSecond();

    // extract attach data
    AttachLink link = LinkUtil.getAttachObject(msg);

    // check if message has expired
    Long skew = configService.getServerNumValue(SC_LINK_MESSAGE_SKEW, (long)60);
    if(link.getIssued() > cur + skew || link.getExpires() + skew < cur) {
      throw new IllegalArgumentException("requiest not currently valid");
    }

    // validate emigo target
    if(!link.getAmigoId().equals(emigoId)) {
      throw new IllegalArgumentException("invalid target of link message");
    }

    // load key for signing message
    KeyPair key = keyStoreService.get(emigoId);

    // generate new token
    String token = SecurityUtil.token();

    // add app to entity registry
    EmigoEntity emigo = setEmigo(msg.getAmigo());

    // add app to app registry
    AppEntity app = setService(emigo);

    // create account app entry
    AccountApp accountApp = accountAppRepository.findOneByAccountAndAppEntity(act, app);
    if(accountApp == null) {
      accountApp = new AccountApp(act, app, token, link.getAccess(), link.getAccess());
    }
    else {
      accountApp.setToken(token);
    }
    accountAppRepository.save(accountApp);

    // increment service revision
    act.setServiceRevision(act.getServiceRevision() + 1);

    // retrieve account message
    AccountMessage accountMessage = accountMessageRepository.findOneByAccount(act);
    if(accountMessage == null) {
      throw new IllegalArgumentException("account has no message");
    }

    // construct response
    Token tok = new Token();
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(emigoId, AC_LINK_MESSAGE_EXPIRE, (long)300);
    tok.setToken(token);
    tok.setIssued(issued);
    tok.setExpires(expires);
    tok.setAmigoId(emigoId);
    AmigoToken tokenMessage = LinkUtil.getMessage(emigoId, key, tok, accountMessage);

    return tokenMessage;
  }

  public LinkMessage prepareAccountCreate(Account act, ServiceAccess access) throws IOException, Exception {

    // extract id of emigo
    String eid = act.getEmigoId();

    // check if app already has max users
    Long count = accountUserRepository.countByAccount(act);
    if(count >= configService.getAccountNumValue(eid, AC_USER_COUNT_MAX, (long)1048576)) {
      throw new IOException("max user count reached");
    }

    // load key for signing message
    KeyPair key = keyStoreService.get(eid);

    // establish authorized window
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(eid, AC_LINK_MESSAGE_EXPIRE, (long)300);

    // retrieve emigo message
    AmigoMessage emigoMessage = getAmigoMessage(act);

    // construct create link object
    CreateLink link = new CreateLink();
    link.setAccess(access);
    link.setExpires(expires);
    link.setIssued(issued); 
  
    // generate link message
    return LinkUtil.getMessage(key, link, emigoMessage);
  }

  public LinkMessage serviceAttach(Account act, String emigoId, ServiceAccess access) throws Exception {
  
    // extract id of emigo
    String eid = act.getEmigoId();

    // check if app already has max users
    Long count = accountUserRepository.countByAccount(act);
    if(count >= configService.getAccountNumValue(eid, AC_USER_COUNT_MAX, (long)1048576)) {
      throw new IOException("max user count reached");
    }

    // load key for signing message
    KeyPair key = keyStoreService.get(eid);

    // establish autorized windows
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(eid, AC_LINK_MESSAGE_EXPIRE, (long)300);
  
    // retreive emigo message
    AmigoMessage emigoMessage = getAmigoMessage(act);

    // construct attach link object
    AttachLink link = new AttachLink();
    link.setAmigoId(emigoId);
    link.setAccess(access);
    link.setExpires(expires);
    link.setIssued(issued);

    // generate link message
    return LinkUtil.getMessage(key, link, emigoMessage);
  }

  @Transactional
  public UserEntry serviceToken(Account act, AmigoToken tok) throws IOException, IllegalArgumentException, Exception {

    // extract id of emigo
    String eid = act.getEmigoId();

    // check if app already has max users
    Long count = accountUserRepository.countByAccount(act);
    if(count >= configService.getAccountNumValue(eid, AC_USER_COUNT_MAX, (long)1048576)) {
      throw new IOException("max user count reached");
    }

    // add new entry for emigo
    EmigoEntity emigoEntity = setEmigo(tok.getAmigo());
    if(!emigoEntity.getEmigoId().equals(tok.getAmigoId())) {
      throw new IllegalArgumentException("invalid emigo message");
    }

    // extract token
    Token token = LinkUtil.getTokenObject(tok);

    // validate token emigo
    if(!token.getAmigoId().equals(tok.getAmigoId())) {
      throw new IllegalArgumentException("invalid token emigo");
    }

    // validate token time
    Long cur = Instant.now().getEpochSecond();
    Long skew = configService.getServerNumValue(SC_LINK_MESSAGE_SKEW, (long)60);
    if(token.getIssued() > cur + skew || token.getExpires() + skew < cur) {
      throw new IllegalArgumentException("requiest not currently valid");
    }

    // construct account user entity
    AccountUser user = accountUserRepository.findOneByAccountAndEmigoId(act, tok.getAmigoId());
    if(user == null) {
      user = new AccountUser(act, tok.getAmigoId(), token.getToken(), SecurityUtil.token());
    }
    else {
      user.setAccountToken(token.getToken());
      user.setServiceToken(SecurityUtil.token());
    }

    // save account user entity
    return accountUserRepository.save(user);
  }

  @Transactional
  public Pass generatePass(Account act, Integer expire, ServiceAccess access) throws IOException, Exception {

    // delete any previous pass
    accountPassRepository.deleteByAccount(act);

    // check if service limit is reached
    String eid = act.getEmigoId();
    Long count = accountAppRepository.countByAccount(act);
    if(count > configService.getAccountNumValue(eid, AC_SERVICE_COUNT_MAX, (long)1024)) {
      throw new IOException("app limit reached");
    }

    // setup valid window
    Long issued = Instant.now().getEpochSecond();
    if(expire == null) {
      expire = configService.getServerNumValue(SC_ACCOUNT_PASS_EXPIRE, (long)300).intValue();
    }
    Long expires = issued + expire;

    // generate pass token
    String token = SecurityUtil.pass();

    // invalidate previous pass
    accountPassRepository.deleteByAccount(act);

    // create new pass
    AccountPass pass = new AccountPass(act, access);
    pass.setPass(token);
    pass.setIssued(issued);
    pass.setExpires(expires);
    return accountPassRepository.save(pass);
  }

  @Transactional
  public AmigoToken accountAttach(String emigoId, String pass, LinkMessage msg) throws IllegalArgumentException, AccessDeniedException, Exception {

    // retrieve referenced account
    Account act = accountRepository.findOneByEmigoId(emigoId);
    if(act == null) {
      throw new IllegalArgumentException("account not found");
    }

    // get current time
    Long cur = Instant.now().getEpochSecond();
    
    // check if pass is present
    List<AccountPass> passes = accountPassRepository.findByAccountAndPass(act, pass);
    if(passes.isEmpty() || cur < passes.get(0).getIssued() || cur > passes.get(0).getExpires()) {
      throw new AccessDeniedException("invalid pass token"); 
    }

    // retrieve configured access
    ServiceAccess access = passes.get(0).getAccess();

    // delete any tokens
    accountPassRepository.deleteByAccount(act);

    // extract attach data
    AttachLink link = LinkUtil.getAttachObject(msg);

    // check if message has expired
    Long skew = configService.getServerNumValue(SC_LINK_MESSAGE_SKEW, (long)60);
    if(link.getIssued() > cur + skew || link.getExpires() + skew < cur) {
      throw new IllegalArgumentException("requiest not currently valid");
    }

    // validate emigo target
    if(!link.getAmigoId().equals(emigoId)) {
      throw new IllegalArgumentException("invalid target of link message");
    }
    
    // load key for signing message
    KeyPair key = keyStoreService.get(emigoId);
    
    // generate new token
    String token = SecurityUtil.token();

    // add app to entity registry
    EmigoEntity emigo = setEmigo(msg.getAmigo());

    // add app to app registry
    AppEntity app = setService(emigo);

    // create account app entry
    AccountApp accountApp = accountAppRepository.findOneByAccountAndAppEntity(act, app);
    if(accountApp == null) {
      accountApp = accountAppRepository.save(new AccountApp(act, app, token, access, link.getAccess()));
    }
    else {
      accountApp.setToken(token);
      accountApp = accountAppRepository.save(accountApp);
    }

    // increment service revision
    act.setServiceRevision(act.getServiceRevision() + 1);

    // retrieve account message
    AccountMessage accountMessage = accountMessageRepository.findOneByAccount(act);
    if(accountMessage == null) {
      throw new IllegalArgumentException("account has no message");
    }

    // construct response
    Token tok = new Token();
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(emigoId, AC_LINK_MESSAGE_EXPIRE, (long)300);
    tok.setToken(token);
    tok.setIssued(issued);
    tok.setExpires(expires);
    tok.setAmigoId(emigoId);
    AmigoToken tokenMessage = LinkUtil.getMessage(emigoId, key, tok, accountMessage);  

    // invalidate previous pass
    accountPassRepository.deleteByAccount(act);

    return tokenMessage;
  }

  @Transactional
  public AmigoToken accountCreate(LinkMessage msg) throws IOException, IllegalStateException, IllegalArgumentException, Exception {

    // limit number of accounts
    Long count = accountRepository.count();
    if(count >= configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128)) {
      throw new IOException("account limit reached");
    }

    // extract create data
    CreateLink link = LinkUtil.getCreateObject(msg);

    // check if message has expired
    Long cur = Instant.now().getEpochSecond();
    Long skew = configService.getServerNumValue(SC_LINK_MESSAGE_SKEW, (long)60);
    if(link.getIssued() > cur + skew || link.getExpires() + skew < cur) {
      throw new IllegalArgumentException("requiest not currently valid");
    }

    // load emigo entity of service
    EmigoEntity serviceEntity = setEmigo(msg.getAmigo());

    // generate new token
    String token = SecurityUtil.token();

    // generate key pair
    KeyPair key = SecurityUtil.gen();
    String id = SecurityUtil.id(key.getPublic());
    keyStoreService.set(id, key);

    // insert new emigo entry
    EmigoEntity emigoEntity = new EmigoEntity();
    emigoEntity.setEmigoId(id);
    emigoEntity.setRevision(1);
    emigoEntity.setRegistry(configService.getServerStrValue(SC_DEFAULT_REGISTRY, null));
    emigoEntity.setVersion(configService.getServerStrValue(SC_VERSION, "0.0.0"));
    emigoEntity.setNode(configService.getServerStrValue(SC_SERVER_HOST_PORT, null));
    emigoEntity = emigoEntityRepository.save(emigoEntity);

    // add service entry if not present
    AppEntity appEntity = setService(serviceEntity);

    // insert new account
    Account account = new Account();
    account.setEmigoId(id);
    account.setEnabled(true);
    account.setDirty(true);
    account = accountRepository.save(account);

    // update account message
    AccountMessage accountMessage = new AccountMessage(account, EmigoUtil.getMessage(key, emigoEntity));
    accountMessage = accountMessageRepository.save(accountMessage);

    // insert new service
    AccountApp accountApp = new AccountApp(account, appEntity, token, link.getAccess());
    accountApp = accountAppRepository.save(accountApp);
   
    // construct response
    Token tok = new Token();
    Long issued = Instant.now().getEpochSecond();
    Long expires = issued + configService.getAccountNumValue(id, AC_LINK_MESSAGE_EXPIRE, (long)300);
    tok.setToken(token);
    tok.setIssued(issued);
    tok.setExpires(expires);
    tok.setAmigoId(id);
    return LinkUtil.getMessage(id, key, tok, accountMessage);
  }

}
