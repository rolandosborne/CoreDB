package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.model.Amigo;
import org.coredb.model.UserEntry;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountUser;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountUserRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

@Service
public class UserService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountUserRepository accountUserRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public AccountUser getUser(Account account, String id) throws NotFoundException {
    AccountUser user = accountUserRepository.findOneByAccountAndEmigoId(account, id);
    if(user == null) {
      throw new NotFoundException(404, "account user not found");
    }
    return user;
  }  

  @Transactional
  public void deleteEmigo(Account account, String emigoId) throws NotFoundException {

    // increment revision
    account.setUserRevision(account.getUserRevision() + 1);
    accountRepository.save(account);

    // delete referenced emigo
    AccountUser user = getUser(account, emigoId);
    accountUserRepository.delete(user);
  }

  public Amigo getIdentity(String id) throws NotFoundException {
    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(id);
    if(entity == null) {
      throw new NotFoundException(404, "identity not found");
    }
    return entity;
  }

  public List<UserEntry> getEmigos(Account account) {

    List<AccountUser> users = accountUserRepository.findAll();

    // return list as base class
    @SuppressWarnings("unchecked")
    List<UserEntry> entries = (List<UserEntry>)(List<?>)users;
    return entries;
  }

}
