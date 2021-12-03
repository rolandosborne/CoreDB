package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.time.Instant;
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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import java.nio.charset.StandardCharsets;

import java.nio.file.AccessDeniedException;
import org.coredb.model.Amigo;
import org.coredb.model.AmigoMessage;
import org.coredb.model.ServiceAccess;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLogin;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountToken;
import org.coredb.jpa.entity.AccountLogin;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.entity.AccountPass;
import org.coredb.jpa.entity.ServerConfig;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountLoginRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountTokenRepository;
import org.coredb.jpa.repository.AccountLoginRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.AccountPassRepository;
import org.coredb.jpa.repository.ServerConfigRepository;

import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.SecurityUtil;
import static org.coredb.NameRegistry.*;

@Service
public class PortalService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountLoginRepository loginRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountTokenRepository accountTokenRepository;

  @Autowired
  private AccountLoginRepository accountLoginRepository;

  @Autowired
  private AccountMessageRepository accountMessageRepository;

  @Autowired
  private AccountPassRepository accountPassRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AdminService adminService;

  @Autowired
  private ServerConfigRepository serverConfigRepository;

  @Autowired
  private KeyStoreService keyStoreService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private  String computePassword(String password, String salt) throws Exception {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    sha.update(salt.getBytes(StandardCharsets.UTF_8));
    return Hex.encodeHexString(sha.digest(password.getBytes(StandardCharsets.UTF_8)));
  }

  private void checkAuthentication(String alias, String password) throws AccessDeniedException, Exception {

    String serverAlias = configService.getServerStrValue(SC_ADMIN_ALIAS, null);
    String serverSalt = configService.getServerStrValue(SC_ADMIN_SALT, null);
    String serverPassword = configService.getServerStrValue(SC_ADMIN_PASSWORD, null);
    if(serverAlias == null || serverSalt == null || serverPassword == null) {
      throw new AccessDeniedException("device credentials not set");
    }

    // validate alias
    if(!serverAlias.equals(alias)) {
      throw new AccessDeniedException("device authentication error");
    }

    // validate password
    String pass = SecurityUtil.hash(serverSalt + password);
    if(!serverPassword.equals(pass)) {
      throw new AccessDeniedException("device authentication error");
    }
  }

  private AccountLogin checkLogin(String username, String password) throws AccessDeniedException, Exception {

    AccountLogin login = accountLoginRepository.findOneByUsername(username);
    if(login == null) {
      throw new AccessDeniedException("invalid username");
    }

    // validate password
    String pass = SecurityUtil.hash(login.getSalt() + password);
    if(!pass.equals(login.getPassword())) {
      throw new AccessDeniedException("invalid password");
    }
    return login;
  }

  @Transactional
  public String generateAccountToken(String alias, String password) throws AccessDeniedException, Exception {
  
    // validate server credentials
    checkAuthentication(alias, password);

    // generate new account token
    Long cur = Instant.now().getEpochSecond();
    AccountToken accountToken = new AccountToken();
    accountToken.setToken(SecurityUtil.token());
    accountToken.setIssued(cur);
    accountToken.setExpires(configService.getServerNumValue(SC_ADMIN_TOKEN_EXPIRE, (long)86400) + cur);
    accountToken.setExpired(false);
    return accountTokenRepository.save(accountToken).getToken();
  }

  @Transactional
  public String generateResetToken(String alias, String password, String amigoId) throws NotFoundException, AccessDeniedException, Exception {
  
    // validate server credentials
    checkAuthentication(alias, password);

    // get referenced account
    Account account = accountRepository.findOneByEmigoId(amigoId);
    if(account == null) {
      throw new NotFoundException(404, "account not found");
    }

    // generate new account token
    Long cur = Instant.now().getEpochSecond();
    AccountToken accountToken = new AccountToken();
    accountToken.setAccount(account);
    accountToken.setToken(SecurityUtil.token());
    accountToken.setIssued(cur);
    accountToken.setExpires(configService.getServerNumValue(SC_ADMIN_TOKEN_EXPIRE, (long)86400) + cur);
    accountToken.setExpired(false);
    return accountTokenRepository.save(accountToken).getToken();
  }

  public List<String> getAmigos(String alias, String password) throws AccessDeniedException, Exception {

    // validate server credentials
    checkAuthentication(alias, password);

    // load ids
    List<String> ids = new ArrayList<String>();
    List<Account> accounts = accountRepository.findAll();
    for(Account account: accounts) {
      ids.add(account.getEmigoId());
    }
    return ids;
  }

  public Amigo getAmigoProfile(String alias, String password, String amigoId) throws AccessDeniedException, NotFoundException, Exception {

    // validate server credentials
    checkAuthentication(alias, password);

    // retrieve amigo/emigo
    EmigoEntity amigo = emigoEntityRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "amigo not found");
    }
    return amigo;
  }

  public void removeAmigoProfile(String alias, String password, String amigoId) throws AccessDeniedException, NotFoundException, Exception {

    // validate server credentials
    checkAuthentication(alias, password);

    // remove specified account
    adminService.removeAccount(amigoId);
  }

  public Amigo getProfile(String username, String password) throws AccessDeniedException, NotFoundException, Exception {
    
    // validate user login
    AccountLogin login = checkLogin(username, password);

    // retrieve amigo
    EmigoEntity amigo = emigoEntityRepository.findOneByEmigoId(login.getAccount().getEmigoId());
    if(amigo == null) {
      throw new NotFoundException(404, "profile not found");
    }
    return amigo;
  }

  private AccountToken getAccountToken(String token) throws AccessDeniedException {
    
    // find referenced token
    AccountToken accountToken = accountTokenRepository.findOneByToken(token);
    if(accountToken == null) {
      throw new AccessDeniedException("token not found");
    }
    
    // validdate token
    Long cur = Instant.now().getEpochSecond();
    if(accountToken.getExpired() || cur < accountToken.getIssued() || cur > accountToken.getExpires()) {
      throw new AccessDeniedException("token not valid");
    }

    return accountToken;
  }

  @Transactional
  public Amigo setProfile(String username, String password, String token) throws AccessDeniedException, IOException, Exception {

    // validate token
    AccountToken accountToken = getAccountToken(token);
    if(accountToken.getAccount() != null) {
      throw new AccessDeniedException("invalid token");
    }

    // expire token
    accountToken.setExpired(true);
    accountTokenRepository.save(accountToken); 

    Long count = accountRepository.count();
    if(count >= configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128)) {
      throw new IOException("account limit reached");
    }

    // generate key pair
    KeyPair key = SecurityUtil.gen();
    String id = SecurityUtil.id(key.getPublic());
    keyStoreService.set(id, key);

    // insert new emigo entry
    EmigoEntity emigoEntry = new EmigoEntity();
    emigoEntry.setEmigoId(id);
    emigoEntry.setHandle(username);
    emigoEntry.setRevision(1);
    emigoEntry.setRegistry(configService.getServerStrValue(SC_DEFAULT_REGISTRY, null));
    emigoEntry.setVersion(configService.getServerStrValue(SC_VERSION, "0.0.0"));
    emigoEntry.setNode(configService.getServerStrValue(SC_SERVER_HOST_PORT, null));
    emigoEntry = emigoEntityRepository.save(emigoEntry);

    // insert new account
    Account account = new Account();
    account.setEmigoId(id);
    account.setEnabled(true);
    account.setDirty(false);
    account = accountRepository.save(account);

    // compute password
    String salt = SecurityUtil.salt();
    String pass = SecurityUtil.hash(salt + password);

    // create new account login
    AccountLogin login = new AccountLogin();
    login.setUsername(username);
    login.setPassword(pass);
    login.setSalt(salt);
    login.setAccount(account);
    accountLoginRepository.save(login);

    // update account message
    AmigoMessage msg = EmigoUtil.getMessage(key, emigoEntry);
    AccountMessage accountMessage = new AccountMessage(account, msg);
    accountMessage = accountMessageRepository.save(accountMessage);

    return emigoEntry;
  }

  public Boolean checkToken(String token) {

    // find referenced account token
    AccountToken accountToken = accountTokenRepository.findOneByToken(token);
    if(accountToken == null) {
      return false;
    }

    // check if still valid
    Long cur = Instant.now().getEpochSecond();
    if(accountToken.getExpired() || cur < accountToken.getIssued() || cur > accountToken.getExpires()) {
      return false;
    }
    return true;
  }

  public Boolean checkUsername(String username, String amigoId) {
    
    // find an account with usename
    AccountLogin accountLogin = accountLoginRepository.findOneByUsername(username);
    if(accountLogin == null) {
      return true;
    }
  
    // check if assigned to amigoId  
    return accountLogin.getAccount().getEmigoId().equals(amigoId);
  }

  @Transactional
  public Amigo resetUsername(String username, String password, String updated) throws AccessDeniedException, NotFoundException, Exception {

    // retrieve account login
    AccountLogin login = checkLogin(username, password);
    Account account = login.getAccount();
 
    // update username
    login.setUsername(updated);
    accountLoginRepository.save(login);

    // retrieve amigo
    EmigoEntity amigo = emigoEntityRepository.findOneByEmigoId(login.getAccount().getEmigoId());
    if(amigo == null) {
      throw new NotFoundException(404, "profile not found");
    }
    amigo.setHandle(updated);
    amigo.setRevision(amigo.getRevision() + 1);

    // update identity revision
    account.setIdentityRevision(amigo.getRevision());
    accountRepository.save(account);
 
    // save public profile
    return emigoEntityRepository.save(amigo);
  }

  @Transactional
  public Amigo resetPassword(String token, String password) throws AccessDeniedException, NotFoundException, Exception {

    // validate token
    AccountToken accountToken = getAccountToken(token);
    if(accountToken.getAccount() == null) {
      throw new AccessDeniedException("invalid token");
    }

    // retrieve amigo
    EmigoEntity amigo = emigoEntityRepository.findOneByEmigoId(accountToken.getAccount().getEmigoId());
    if(amigo == null) {
      throw new NotFoundException(404, "profile not found");
    }
 
    // compute password
    String salt = SecurityUtil.salt();
    String pass = SecurityUtil.hash(salt + password);

    // find associated login
    AccountLogin login = accountLoginRepository.findOneByAccount(accountToken.getAccount());
    if(login == null) {
      throw new NotFoundException(404, "login not found");
    }
    login.setPassword(pass);
    login.setSalt(salt);
    accountLoginRepository.save(login);

    // expire token
    accountToken.setExpired(true);
    accountTokenRepository.save(accountToken); 

    // return profile 
    return amigo;
  }

  @Transactional
  public String setPassCode(String username, String password) throws AccessDeniedException, NotFoundException, Exception {

    // validate login
    AccountLogin login = checkLogin(username, password);
    
    // reference account
    Account account = login.getAccount();
    if(account == null) {
      throw new NotFoundException(404, "account not found");
    }

    // setup valid window
    Long issued = Instant.now().getEpochSecond();
    Long expire = configService.getServerNumValue(SC_ACCOUNT_PASS_EXPIRE, (long)300);
    Long expires = issued + expire;

    // generate pass token
    String token = SecurityUtil.pass();

    // invalidate previous pass
    accountPassRepository.deleteByAccount(login.getAccount());

    // set access levels
    ServiceAccess access = new ServiceAccess();
    access.setEnableShow(true);
    access.setEnableIdentity(true);
    access.setEnableProfile(true);
    access.setEnableGroup(true);
    access.setEnableShare(true);
    access.setEnablePrompt(true);
    access.setEnableService(true);
    access.setEnableIndex(true);
    access.setEnableUser(true);
    access.setEnableAccount(true);
    access.setEnableConversation(true);

    // create new pass
    AccountPass pass = new AccountPass(account, access);
    pass.setPass(token);
    pass.setIssued(issued);
    pass.setExpires(expires);
    return accountPassRepository.save(pass).getPass();
  } 

  public Boolean checkAdmin() {
    ServerConfig config = serverConfigRepository.findOneByConfigId(SC_ADMIN_SET);
    if(config == null || !config.getBoolValue()) {
      return false;
    }
    return true;
  }

  @Transactional
  public void setAdmin(String username, String password, String domain) throws AccessDeniedException, Exception {

    // check if admin already set
    if(checkAdmin()) {
      throw new AccessDeniedException("admin params already set");
    }
  
    // compute password
    String salt = SecurityUtil.salt();
    String pass = SecurityUtil.hash(salt + password);

    List<ServerConfig> configs = new ArrayList<ServerConfig>();

    ServerConfig aliasConfig = new ServerConfig();
    aliasConfig.setConfigId(SC_ADMIN_ALIAS);
    aliasConfig.setStrValue(username);
    configs.add(aliasConfig);

    ServerConfig saltConfig = new ServerConfig();
    saltConfig.setConfigId(SC_ADMIN_SALT);
    saltConfig.setStrValue(salt);
    configs.add(saltConfig);

    ServerConfig passConfig = new ServerConfig();
    passConfig.setConfigId(SC_ADMIN_PASSWORD);
    passConfig.setStrValue(pass);
    configs.add(passConfig);

    ServerConfig nodeConfig = new ServerConfig();
    nodeConfig.setConfigId(SC_SERVER_HOST_PORT);
    nodeConfig.setStrValue("https://" + domain);
    configs.add(nodeConfig);

    ServerConfig registryConfig = new ServerConfig();
    registryConfig.setConfigId(SC_DEFAULT_REGISTRY);
    registryConfig.setStrValue("https://" + domain + "/registry");
    configs.add(registryConfig);

    ServerConfig setConfig = new ServerConfig();
    setConfig.setConfigId(SC_ADMIN_SET);
    setConfig.setBoolValue(true);
    configs.add(setConfig);

    serverConfigRepository.save(configs);
    configService.refresh();
  }
}

