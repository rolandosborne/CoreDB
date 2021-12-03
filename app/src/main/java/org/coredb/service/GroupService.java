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

import org.coredb.model.LabelEntry;
import org.coredb.model.LabelView;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountLabelRepository;
import org.coredb.jpa.repository.AccountEmigoLabelIdRepository;
import org.coredb.jpa.repository.AccountAttributeLabelIdRepository;
import org.coredb.jpa.repository.AccountSubjectLabelIdRepository;

@Service
public class GroupService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountLabelRepository accountLabelRepository;

  @Autowired
  private AccountEmigoLabelIdRepository accountEmigoLabelIdRepository;

  @Autowired
  private AccountAttributeLabelIdRepository accountAttributeLabelIdRepository;

  @Autowired
  private AccountSubjectLabelIdRepository accountSubjectLabelIdRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountLabel getAccountLabel(Account account, String labelId) throws NotFoundException {

    // find the referecnced account laebel 
    AccountLabel label = accountLabelRepository.findOneByAccountAndLabelId(account, labelId);
    if(label == null) {
      throw new NotFoundException(404, "account label not found");
    }
    return label;
  }

  public List<LabelView> getLabelViews(Account account) {
    
    List<AccountLabel> labels = accountLabelRepository.findByAccountOrderByName(account);
    
    List<LabelView> views = new ArrayList<LabelView>();
    for(AccountLabel label: labels) {
      LabelView view = new LabelView();
      view.setLabelId(label.getLabelId());
      view.setRevision(label.getRevision());
      views.add(view);
    }
    return views;
  }

  public List<LabelEntry> getLabels(Account account) {

    List<AccountLabel> labels = accountLabelRepository.findByAccountOrderByName(account);

    // return list as base class
    @SuppressWarnings("unchecked")
    List<LabelEntry> entries = (List<LabelEntry>)(List<?>)labels;
    return entries;
  }

  public LabelEntry getLabel(Account account, String labelId) throws NotFoundException {
    
    AccountLabel label = accountLabelRepository.findOneByAccountAndLabelId(account, labelId);
    if(label == null) {
      throw new NotFoundException(404, "label entry for account not found");
    }
    return label;
  }

  @Transactional
  public LabelEntry addLabel(Account account, String name) {
 
    // increment profile revision
    Integer revision = account.getGroupRevision() + 1;
    account.setGroupRevision(revision);
    accountRepository.save(account);
    
    // construct new label
    AccountLabel label = new AccountLabel(account, name);
    label.setRevision(revision);
    return accountLabelRepository.save(label);
  }

  @Transactional
  public LabelEntry updLabel(Account account, String labelId, String name) throws NotFoundException {

    // increment profile revision
    Integer revision = account.getGroupRevision() + 1;
    account.setGroupRevision(revision);
    accountRepository.save(account);

    // retrieve entity to update
    AccountLabel label = accountLabelRepository.findOneByAccountAndLabelId(account, labelId);
    if(label == null) {
      throw new NotFoundException(404, "label entry for account not found");
    }

    // update record
    label.setName(name);
    label.setRevision(revision);
    return accountLabelRepository.save(label);
  }

  @Transactional
  public void delLabel(Account account, String labelId) throws NotFoundException {

    // delete record
    AccountLabel accountLabel = getAccountLabel(account, labelId);
    accountEmigoLabelIdRepository.deleteByLabelId(accountLabel.getId());
    accountAttributeLabelIdRepository.deleteByLabelId(accountLabel.getId());
    accountSubjectLabelIdRepository.deleteByLabelId(accountLabel.getId());
    accountLabelRepository.delete(accountLabel);

    // NOTE profile and show entity revisions wont change even though label association could have

    // increment revision
    account.setGroupRevision(account.getGroupRevision() + 1);
    account.setProfileRevision(account.getProfileRevision() + 1);
    account.setContactRevision(account.getContactRevision() + 1);
    account.setViewRevision(account.getViewRevision() + 1);
    account.setShowRevision(account.getShowRevision() + 1);
    accountRepository.save(account);
  }

}
