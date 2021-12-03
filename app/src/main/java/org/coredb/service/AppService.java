package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.model.ServiceEntry;
import org.coredb.model.ServiceAccess;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.AppEntity;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountAppRepository;
import org.coredb.jpa.repository.AppEntityRepository;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

@Service
public class AppService {

  @Autowired
  private AppEntityRepository appEntityRepository;

  @Autowired
  private AccountAppRepository accountAppRepository;

  @Autowired
  private AccountRepository accountRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountApp getAccountApp(Account account, String id) throws NotFoundException {

    // retrieve app entity
    AppEntity app = appEntityRepository.findOneByEmigoId(id);
    if(app == null) {    
      throw new NotFoundException(404, "app entity not found");
    }

    // retrieve account app
    AccountApp entry = accountAppRepository.findOneByAccountAndAppEntity(account, app);
    if(entry == null) {
      throw new NotFoundException(404, "account app not found");
    }

    return entry;
  }  

  public ServiceAccess getAccountAccess(AccountApp app) throws NotFoundException {
    
    // retrieve app entry and return access
    return app.getAccountAccess();
  }

  public ServiceAccess getServiceAccess(AccountApp app) throws NotFoundException {
    
    // retrieve app entry and return access
    return app.getServiceAccess();
  }


  @Transactional
  public ServiceEntry setAppAuthorizedAccess(Account account, String serviceId, ServiceAccess access) 
        throws NotFoundException {
   
    // increment revision
    account.setServiceRevision(account.getServiceRevision() + 1);
    accountRepository.save(account);
    
    // retrieve referenced app
    AccountApp app = getAccountApp(account, serviceId);
    app.setAccountAccess(access);
    
    return accountAppRepository.save(app);
  }

  @Transactional
  public void deleteApp(Account account, String serviceId)
        throws NotFoundException {

    // retrieve app entry to delete
    AccountApp app = getAccountApp(account, serviceId);

    // delete entry
    accountAppRepository.delete(app);

    // increment revision
    account.setServiceRevision(account.getServiceRevision() + 1);
    accountRepository.save(account);
  }

  public ServiceEntry getApp(Account account, String emigoId) throws NotFoundException {
    return getAccountApp(account, emigoId);
  }

  public List<ServiceEntry> getApps(Account account) {

    List<AccountApp> apps = accountAppRepository.findByAccount(account);

    // convert response
    @SuppressWarnings("unchecked")
    List<ServiceEntry> entries = (List<ServiceEntry>)(List<?>)apps;
    return entries;
  }

}

