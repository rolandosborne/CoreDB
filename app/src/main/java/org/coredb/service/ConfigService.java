package org.coredb.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.coredb.jpa.repository.AccountConfigRepository;
import org.coredb.jpa.repository.ServerConfigRepository;
import org.coredb.jpa.repository.AccountRepository;
import static org.coredb.NameRegistry.*;
import org.coredb.jpa.entity.AccountConfig;
import org.coredb.jpa.entity.ServerConfig;
import org.coredb.jpa.entity.Account;

@Service
public class ConfigService {

  public class ConfigServiceValue {
    public String strValue;
    public Long numValue;
    public Boolean boolValue;
    ConfigServiceValue() { }
    ConfigServiceValue(String val) { strValue = val; }
    ConfigServiceValue(Long val) { numValue = val; }
    ConfigServiceValue(Boolean val) { boolValue = val; }
    ConfigServiceValue(String s, Long n, Boolean b) {
      this.strValue = s;
      this.numValue = n;
      this.boolValue = b;
    }
  }    

  private Map<String, Map<String, ConfigServiceValue>> accountConfigs = 
      new HashMap<String, Map<String, ConfigServiceValue>>();
  private Map<String, ConfigServiceValue> serverConfigs =
      new HashMap<String, ConfigServiceValue>();

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountConfigRepository accountConfigRepository;

  @Autowired
  private ServerConfigRepository serverConfigRepository;

  // to be invoked on configured interval
  public long refresh() {

    // refresh all account configs
    List<Account> accounts = accountRepository.findAll();
    accountConfigs.clear();
    for(Account account : accounts) {
      String emigoId = account.getEmigoId();
      List<AccountConfig> configs = accountConfigRepository.findByAccount(account);
      accountConfigs.put(emigoId, new HashMap<String, ConfigServiceValue>());
      for(AccountConfig config: configs) {
        String configId = config.getConfigId();
        accountConfigs.get(emigoId).put(configId, 
            new ConfigServiceValue(config.getStrValue(), config.getNumValue(), config.getBoolValue()));
      }
    }

    // refresh all server configs
    List<ServerConfig> servers = serverConfigRepository.findAll();
    serverConfigs.clear();
    for(ServerConfig server: servers) {
      String configId = server.getConfigId();
      serverConfigs.put(configId,
          new ConfigServiceValue(server.getStrValue(), server.getNumValue(), server.getBoolValue()));
    }

    return getServerNumValue(SC_CONFIG_REFRESH_INTERVAL, (long)60).longValue();
  }

  public String getAccountStrValue(String emigoId, String configId, String unset) {
    ConfigServiceValue val = getAccountConfig(emigoId, configId);
    if(val == null || val.strValue == null) {
      return unset;
    }
    else {
      return val.strValue;
    }
  }

  public Long getAccountNumValue(String emigoId, String configId, Long unset) {
    ConfigServiceValue val = getAccountConfig(emigoId, configId);
    if(val == null || val.numValue == null) {
      return unset;
    }
    else {
      return val.numValue;
    }
  }

  public Boolean getAccountBoolValue(String emigoId, String configId, Boolean unset) {
    ConfigServiceValue val = getAccountConfig(emigoId, configId);
    if(val == null || val.boolValue == null) {
      return unset;
    }
    else {
      return val.boolValue;
    }
  }

  public String getServerStrValue(String configId, String unset) {
    ConfigServiceValue val = getServerConfig(configId);
    if(val == null || val.strValue == null) {
      return unset;
    }
    else {
      return val.strValue;
    }
  }

  public Long getServerNumValue(String configId, Long unset) {
    ConfigServiceValue val = getServerConfig(configId);
    if(val == null || val.numValue == null) {
      return unset;
    }
    else {
      return val.numValue;
    }
  }

  public Boolean getServerBoolValue(String configId, Boolean unset) {
    ConfigServiceValue val = getServerConfig(configId);
    if(val == null || val.boolValue == null) {
      return unset;
    }
    else {
      return val.boolValue;
    }
  }

  private ConfigServiceValue getAccountConfig(String emigoId, String configId) {
    ConfigServiceValue val = null;     

    // load account confiug
    if(accountConfigs.get(emigoId) != null) {
      val = accountConfigs.get(emigoId).get(configId);
    }

    // return value if present
    return val;
  }

  public ConfigServiceValue getServerConfig(String configId) {

    // return value if present
    return serverConfigs.get(configId);
  }
}
    
