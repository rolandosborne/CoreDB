package org.coredb.service;

import java.time.Instant;
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

import java.lang.IllegalArgumentException;
import org.coredb.api.NotFoundException;

import static org.coredb.NameRegistry.*;

import org.coredb.model.Amigo;
import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
import org.coredb.model.SystemStat;
import org.coredb.model.AlertEntry;
import org.coredb.model.AccountEntry;
import org.coredb.model.AmigoMessage;
import org.coredb.model.AccountStatus;
import org.coredb.model.ServerInfo;
import org.coredb.model.LinkMessage;
import org.coredb.model.AmigoToken;
import org.coredb.model.AttachLink;
import org.coredb.model.Token;

import org.coredb.jpa.entity.Stat;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountAlert;
import org.coredb.jpa.entity.AccountConfig;
import org.coredb.jpa.entity.ServerConfig;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountPrompt;
import org.coredb.jpa.entity.AccountUser;
import org.coredb.jpa.entity.AppEntity;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.AccountAttributeLabelId;
import org.coredb.jpa.entity.AccountSubjectLabelId;
import org.coredb.jpa.entity.AccountEmigoLabelId;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.StatRepository;
import org.coredb.jpa.repository.ServerConfigRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountShareRepository;
import org.coredb.jpa.repository.AccountSharePendingRepository;
import org.coredb.jpa.repository.AccountSharePromptRepository;
import org.coredb.jpa.repository.AccountLabelRepository;
import org.coredb.jpa.repository.AccountEmigoRepository;
import org.coredb.jpa.repository.AccountAttributeRepository;
import org.coredb.jpa.repository.AccountSubjectRepository;
import org.coredb.jpa.repository.AccountAlertRepository;
import org.coredb.jpa.repository.AccountPromptRepository;
import org.coredb.jpa.repository.AccountAnswerRepository;
import org.coredb.jpa.repository.AccountUserRepository;
import org.coredb.jpa.repository.AccountUserAgentRepository;
import org.coredb.jpa.repository.AccountPassRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.AccountConfigRepository;
import org.coredb.jpa.repository.OriginalSubjectAssetRepository;
import org.coredb.jpa.repository.AccountSubjectAssetRepository;
import org.coredb.jpa.repository.AccountAppAgentRepository;
import org.coredb.jpa.repository.AppEntityRepository;
import org.coredb.jpa.repository.AccountAppRepository;
import org.coredb.jpa.repository.AccountLoginRepository;
import org.coredb.jpa.repository.AccountTokenRepository;

import org.coredb.jpa.repository.AccountAttributeLabelIdRepository;
import org.coredb.jpa.repository.AccountSubjectLabelIdRepository;
import org.coredb.jpa.repository.AccountEmigoLabelIdRepository;

import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.LinkUtil;
import org.coredb.service.util.SecurityUtil;

@Service
public class AdminService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountConfigRepository accountConfigRepository;

  @Autowired
  private ServerConfigRepository serverConfigRepository;

  @Autowired
  private AccountMessageRepository messageRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private StatRepository statRepository;

  @Autowired
  private AccountSubjectAssetRepository assetRepository;

  @Autowired
  private OriginalSubjectAssetRepository originalRepository;

  @Autowired
  private AccountPassRepository passRepository;

  @Autowired
  private AccountAppRepository appRepository;

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AccountSharePendingRepository sharePendingRepository;

  @Autowired
  private AccountSharePromptRepository sharePromptRepository;

  @Autowired
  private AccountLabelRepository labelRepository;

  @Autowired
  private AccountEmigoRepository emigoRepository;

  @Autowired
  private AccountAttributeRepository attributeRepository;

  @Autowired
  private AccountSubjectRepository subjectRepository;

  @Autowired
  private AccountAlertRepository alertRepository;

  @Autowired
  private AccountPromptRepository promptRepository;

  @Autowired
  private AccountAnswerRepository answerRepository;

  @Autowired
  private AccountUserRepository userRepository;

  @Autowired
  private AccountUserAgentRepository userAgentRepository;

  @Autowired
  private AccountAppAgentRepository appAgentRepository;

  @Autowired
  private AppEntityRepository appEntityRepository;

  @Autowired
  private AccountAttributeLabelIdRepository attributeLabelRepository;

  @Autowired
  private AccountSubjectLabelIdRepository subjectLabelRepository;

  @Autowired
  private AccountEmigoLabelIdRepository emigoLabelRepository;

  @Autowired
  private AccountLoginRepository accountLoginRepository;

  @Autowired
  private AccountTokenRepository accountTokenRepository;

  @Autowired
  private IdentityService identityService;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AccessService accessService;

  @Autowired
  private KeyStoreService keyStoreService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private Long requests = (long)0;

  public Long incrementRequestCount() {
    return ++requests;
  }

  private Account getEmigoAccount(String emigoId) throws NotFoundException {
  
    Account account = accountRepository.findOneByEmigoId(emigoId);
    if(account == null) {
      throw new NotFoundException(404, "account not found");
    }
    return account;
  }

  private AppEntity getAppEntity(String emigoId) throws NotFoundException {

    AppEntity app = appEntityRepository.findOneByEmigoId(emigoId);
    if(app == null) {
      throw new NotFoundException(404, "app entity not found");
    }
    return app;
  }

  public ServerInfo getInfo() {

    String version = configService.getServerStrValue(SC_VERSION, "0.0.0");
    ServerInfo info = new ServerInfo();
    info.setVersion(version);
    return info;
  }

  public List<SystemStat> getStats() {
    
    List<Stat> stats = statRepository.findAllByOrderByTimestampDesc();

    // return base class
    @SuppressWarnings("unchecked")
    List<SystemStat> sys = (List<SystemStat>)(List<?>)stats;
    return sys;
  }

  @Transactional
  public void addStat(Integer processor, Long memory, Long storage) {
    
    // create new stat entry
    Long cur = Instant.now().getEpochSecond();
    Stat stat = new Stat();
    stat.setTimestamp(cur.intValue());
    stat.setProcessor(processor);
    stat.setMemory(memory);
    stat.setStorage(storage);
    stat.setRequests(requests);
    stat.setAccounts(accountRepository.count());
    statRepository.save(stat);

    // reset requests
    requests = (long)0;

    // truncate list
    Long max = configService.getServerNumValue(SC_STAT_COUNT, (long)4096);
    List<Stat> stats = statRepository.findAllByOrderByTimestampDesc();
    if(stats.size() > max) {
      statRepository.deleteByTimestampLessThanEqual(stats.get(max.intValue()).getTimestamp());
    }
  }

  @Transactional
  public AlertEntry addAlert(String emigoId, String message) throws NotFoundException {

    Account account = getEmigoAccount(emigoId);
    AccountAlert alert = new AccountAlert(account, message);
    return alertRepository.save(alert);
  }

  public void clearAlert(String emigoId, String alertId) throws NotFoundException {

    Account account = getEmigoAccount(emigoId);
    alertRepository.deleteByAccountAndAlertId(account, alertId);
  }

  public List<AlertEntry> getAlerts(String emigoId) throws NotFoundException {
  
    Account account = getEmigoAccount(emigoId);
    List<AccountAlert> alerts = alertRepository.findByAccount(account);
    
    // return base class
    @SuppressWarnings("unchecked")
    List<AlertEntry> entries = (List<AlertEntry>)(List<?>)alerts;
    return entries;
  } 

  public List<ConfigEntry> getServerConfigs() throws NotFoundException {

    List<ServerConfig> configs = serverConfigRepository.findAll();
    
    // return base class
    @SuppressWarnings("unchecked")
    List<ConfigEntry> entries = (List<ConfigEntry>)(List<?>)configs;
    return entries;
  }

  public ConfigEntry getServerConfig(String configId) throws NotFoundException {
    
    ServerConfig cnf = serverConfigRepository.findOneByConfigId(configId);
    if(cnf == null) {
      return new ServerConfig();
    }
    return cnf;
  }

  @Transactional
  public ConfigEntry setServerConfig(String configId, Config config) throws NotFoundException {
  
    ServerConfig cnf = serverConfigRepository.findOneByConfigId(configId);
    if(cnf == null) {
      cnf = new ServerConfig();
    }
    cnf.setConfig(config);
    return serverConfigRepository.save(cnf);  
  }

  @Transactional
  public void clearServerConfig(String configId) throws NotFoundException {
    serverConfigRepository.deleteByConfigId(configId);
  }

  public List<ConfigEntry> getAccountConfigs(String emigoId) throws NotFoundException {
  
    Account account = getEmigoAccount(emigoId);
    List<AccountConfig> configs = accountConfigRepository.findByAccount(account);
    
    // return base class
    @SuppressWarnings("unchecked")
    List<ConfigEntry> entries = (List<ConfigEntry>)(List<?>)configs;
    return entries;
  }

  public ConfigEntry getAccountConfig(String emigoId, String configId) throws NotFoundException {

    Account account = getEmigoAccount(emigoId);
    AccountConfig cnf = accountConfigRepository.findOneByAccountAndConfigId(account, configId);
    if(cnf == null) {
      return new AccountConfig();
    }
    return cnf;
  }

  @Transactional
  public ConfigEntry setAccountConfig(String emigoId, String configId, Config config) throws NotFoundException {
    
    Account account = getEmigoAccount(emigoId);
    AccountConfig cnf = accountConfigRepository.findOneByAccountAndConfigId(account, configId);
    if(cnf == null) {
      cnf = new AccountConfig();
    }
    cnf.setConfig(config);
    return accountConfigRepository.save(cnf);
  }

  @Transactional
  public void clearAccountConfig(String emigoId, String configId) throws NotFoundException {

    Account account = getEmigoAccount(emigoId);
    accountConfigRepository.deleteByAccountAndConfigId(account, configId);
  }

  public List<AccountEntry> getAccounts() {

    List<Account> accounts = accountRepository.findAll();
    
    // return base class
    @SuppressWarnings("unchecked")
    List<AccountEntry> entries = (List<AccountEntry>)(List<?>)accounts;
    return entries;
  }

  public List<AmigoMessage> getMessages() {

    List<AccountMessage> msgs = messageRepository.findAll();

    // return base class
    @SuppressWarnings("unchecked")
    List<AmigoMessage> emigos = (List<AmigoMessage>)(List<?>)msgs;
    return emigos;
  }

  public AccountStatus getStatus() {

    AccountStatus status = new AccountStatus();
    status.setTotal(configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128));
    status.setCurrent(accountRepository.count());
    return status;
  }

  public Amigo getAccount(String emigoId) throws NotFoundException {
  
    AccountEntry entry = getEmigoAccount(emigoId);
    EmigoEntity emigo = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    return emigo; 
  }

  @Transactional
  public AccountEntry setAccountState(String emigoId, Boolean enabled) throws NotFoundException {

    Account account = getEmigoAccount(emigoId);
    account.setEnabled(enabled);
    return accountRepository.save(account);
  }

  @Transactional
  public AmigoMessage setAccountRevision(String emigoId) throws NotFoundException, Exception {

    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(entity == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    Account account = getEmigoAccount(emigoId);
    return identityService.setEmigo(account, entity);
  }

  @Transactional
  public AmigoMessage setAccountRegistry(String emigoId, String registry) throws NotFoundException, Exception {
    
    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(entity == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    entity.setRegistry(registry);
    Account account = getEmigoAccount(emigoId);
    return identityService.setEmigo(account, entity);
  }

  @Transactional
  public AmigoToken setAccountService(String emigoId, LinkMessage msg) 
      throws NotFoundException, IllegalArgumentException, Exception {
  
    return accessService.setAccountService(emigoId, msg);
  }

  @Transactional
  public AccountEntry addAccount() throws IllegalArgumentException, Exception {

    return accessService.addAccount();
  }

  @Transactional
  public void deleteAccount(String emigoId) throws NotFoundException {

    removeLabels(emigoId);
    removeAccount(emigoId);
  }

  @Transactional
  public void removeLabels(String emigoId) throws NotFoundException {
    
    Account act = getEmigoAccount(emigoId);
    List<AccountLabel> labels = labelRepository.findByAccount(act);
    for(AccountLabel label: labels) {
      emigoLabelRepository.deleteByLabelId(label.getId());
      attributeLabelRepository.deleteByLabelId(label.getId());
      subjectLabelRepository.deleteByLabelId(label.getId());
    }
  }

  @Transactional
  public void removeAccount(String emigoId) throws NotFoundException {

    Account act = getEmigoAccount(emigoId);

    // delete portal entites
    accountLoginRepository.deleteByAccount(act);
    accountTokenRepository.deleteByAccount(act);   
 
    // delete share
    shareRepository.deleteByAccount(act);

    // delete sharepending
    sharePendingRepository.deleteByAccount(act);

    // delete shareprompt
    sharePromptRepository.deleteByAccount(act);

    // delete emigo and linked
    emigoRepository.deleteByAccount(act);

    // delete subject and linked
    List<AccountSubject> subjects = subjectRepository.findByAccount(act);
    assetRepository.deleteBySubjectIn(subjects);
    originalRepository.deleteBySubjectIn(subjects);
    subjectRepository.deleteByAccount(act);

    // delete attribute and linked
    attributeRepository.deleteByAccount(act);

    // delete label and linked
    labelRepository.deleteByAccount(act);

    // delete alert
    alertRepository.deleteByAccount(act);

    // delete prompt and linked
    List<AccountPrompt> prompts = promptRepository.findByAccount(act);
    answerRepository.deleteByPromptIn(prompts);
    promptRepository.deleteByAccount(act);

    // delete user and linked
    List<AccountUser> users = userRepository.findByAccount(act);
    userAgentRepository.deleteByUserIn(users);
    userRepository.deleteByAccount(act);

    // delete app
    appRepository.deleteByAccount(act);

    // delete pass
    passRepository.deleteByAccount(act);

    // delete message
    messageRepository.deleteByAccount(act);

    // delete config
    accountConfigRepository.deleteByAccount(act);

    // delete all account references
    accountRepository.delete(act);
  }

  public List<org.coredb.model.Service> getServices() {

    List<AppEntity> apps = appEntityRepository.findAll();

    // return base class
    @SuppressWarnings("unchecked")
    List<org.coredb.model.Service> services = (List<org.coredb.model.Service>)(List<?>)apps;
    return services;
  }

  public org.coredb.model.Service getService(String emigoId) throws NotFoundException {

    return getAppEntity(emigoId);
  }

  @Transactional
  public org.coredb.model.Service setService(String emigoId, Boolean enable) throws NotFoundException {

    AppEntity app = getAppEntity(emigoId);
    app.setEnabled(enable);
    return appEntityRepository.save(app);
  }

  @Transactional
  public org.coredb.model.Service addService(AmigoMessage message, Boolean enable) throws NotFoundException, Exception {

    Amigo emigo = EmigoUtil.getObject(message);
    String emigoId = emigo.getAmigoId();

    // add new emigo if new
    EmigoEntity emigoEntity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(emigoEntity == null) {
      emigoEntity = new EmigoEntity(emigo);
      emigoEntity = emigoEntityRepository.save(emigoEntity);
    }

    // add or update
    AppEntity appEntity = appEntityRepository.findOneByEmigoId(emigoId);
    if(appEntity == null) {
      appEntity = new AppEntity(emigoId);
    }
    appEntity.setEnabled(enable);
    return appEntityRepository.save(appEntity);
  }

  @Transactional
  public void deleteService(String emigoId) throws NotFoundException {

    AppEntity app = getAppEntity(emigoId);

    appRepository.deleteByAppEntity(app);
    appAgentRepository.deleteByApp(app);
    appEntityRepository.deleteByEmigoId(emigoId);
  }

}
