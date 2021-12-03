package org.coredb.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.repository.EmigoEntityRepository;

import org.coredb.model.Amigo;
import org.coredb.model.AlertEntry;
import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
import org.coredb.model.AlertEntry;
import org.coredb.model.AccountStatus;

import org.coredb.jpa.entity.AccountConfig;
import org.coredb.jpa.entity.AccountAlert;

import org.coredb.jpa.repository.AccountConfigRepository;
import org.coredb.jpa.repository.AccountAlertRepository;

import static org.coredb.NameRegistry.*;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountConfigRepository accountConfigRepository;

  @Autowired
  private AccountAlertRepository accountAlertRepository;

  @Autowired
  private ConfigService configService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private Set<String> configs;

  public AccountService() {
    configs = new HashSet<String>();
    configs.add(AC_AUTO_ADD_EMIGO);
    configs.add(AC_PROMPT_UNKNOWN);
    configs.add(AC_AUTO_ACCEPT_REQUEST);
  }

  private AccountConfig getAccountConfig(Account account, String id) {
    
    // retrive specified config
    AccountConfig config = accountConfigRepository.findOneByAccountAndConfigId(account, id);
    if(config == null) {
      return new AccountConfig(account, id);
    }
    return config;
  }

  private AccountAlert getAccountAlert(Account account, String id) throws NotFoundException {

    // retrive specified alert
    AccountAlert alert = accountAlertRepository.findOneByAccountAndAlertId(account, id);
    if(alert == null) {
      throw new NotFoundException(404, "account alert not found");
    }
    return alert;
  }

  public AccountStatus getStatus() {

    // construct status object
    AccountStatus status = new AccountStatus();
    status.setTotal(configService.getServerNumValue(SC_ACCOUNT_MAX_COUNT, (long)128));
    status.setCurrent(accountRepository.count());
    return status;
  }

  public List<ConfigEntry> getConfigs(Account account) {

    List<AccountConfig> config = accountConfigRepository.findByAccountAndConfigIdIn(account, configs);

    // retrun list as base class
    @SuppressWarnings("unchecked")
    List<ConfigEntry> entries = (List<ConfigEntry>)(List<?>)config;
    return entries;
  }

  public ConfigEntry getConfig(Account account, String id) throws NotFoundException {

    // return specified entry
    return getAccountConfig(account, id);
  }

  @Transactional
  public ConfigEntry updateConfig(Account account, String id, Config data) throws AccessDeniedException {

    // check if config is valid writable
    if(configs.contains(id)) {

      // retrieve specified entry
      AccountConfig config = getAccountConfig(account, id);

      // save and return updated entry
      config.setConfig(data);
      return accountConfigRepository.save(config);
    }

    // otherwise config is not writable
    throw new AccessDeniedException("cannot update specified config");
  }

  @Transactional
  public void deleteConfig(Account account, String id) throws NotFoundException {
  
    // retrieve and delete entry
    AccountConfig config = getAccountConfig(account, id);
    accountConfigRepository.delete(config);
  }

  public List<AlertEntry> getAlerts(Account account) {
    
    List<AccountAlert> alerts = accountAlertRepository.findAll();

    // retrun list as base class
    @SuppressWarnings("unchecked")
    List<AlertEntry> entries = (List<AlertEntry>)(List<?>)alerts;
    return entries;
  }

  @Transactional
  public void deleteAlert(Account account, String id) throws NotFoundException {
  
    // retrieve and delete alert
    AccountAlert alert = getAccountAlert(account, id);
    accountAlertRepository.delete(alert);
  }
  
}



