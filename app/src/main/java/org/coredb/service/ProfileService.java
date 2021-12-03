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

import java.lang.IllegalArgumentException;
import org.coredb.api.NotFoundException;

import org.coredb.model.LabelEntry;
import org.coredb.model.Attribute;
import org.coredb.model.AttributeEntry;
import org.coredb.model.AttributeView;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountAttribute;
import org.coredb.jpa.entity.AccountAttributeLabelId;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountLabelRepository;
import org.coredb.jpa.repository.AccountAttributeRepository;
import org.coredb.jpa.repository.AccountAttributeLabelIdRepository;

@Service
public class ProfileService {

  @Autowired
  private AccountAttributeRepository accountAttributeRepository;

  @Autowired
  private AccountLabelRepository accountLabelRepository;

  @Autowired
  private AccountAttributeLabelIdRepository accountAttributeLabelIdRepository;

  @Autowired
  private AccountRepository accountRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private List<AttributeView> setAttributeView(List<AccountAttribute> attributes) {
  
    List<AttributeView> views = new ArrayList<AttributeView>();
    for(AccountAttribute attribute: attributes) {
      AttributeView view = new AttributeView();
      view.setAttributeId(attribute.getAttributeId());
      view.setRevision(attribute.getRevision());
      views.add(view);
    }
    return views;
  }

  private AttributeEntry setAttributeEntry(AccountAttribute attribute) {
    
    for(AccountLabel l: attribute.getAccountLabels()) {
      attribute.addLabelsItem(l.getLabelId());
    }
    return attribute;
  }

  private AccountAttribute getAttribute(Account account, String attributeId) throws NotFoundException {

    // find the referenced account attribute
    AccountAttribute attribute = accountAttributeRepository.findOneByAccountAndAttributeId(account, attributeId);
    if(attribute == null) {
      throw new NotFoundException(404, "account attribute not found");
    }
    return attribute;
  }

  public AttributeEntry getAttributeEntry(Account account, String attributeId) throws NotFoundException {
    AccountAttribute attribute = getAttribute(account, attributeId);
    return setAttributeEntry(attribute);
  }

  private AccountLabel getLabel(Account account, String id) throws NotFoundException {

    // find the referecnced account laebel
    AccountLabel label = accountLabelRepository.findOneByAccountAndLabelId(account, id);
    if(label == null) {
      throw new NotFoundException(404, "account label not found");
    }
    return label;
  }

  public Attribute getAttributeLabelEntry(Account account, List<AccountLabel> labels, String attributeId) throws NotFoundException {
  
    // check if attribute exists
    AccountAttribute attribute = accountAttributeRepository.findOneByAccountAndAccountLabelsInAndAttributeId(account, labels, attributeId);
    if(attribute == null) {
      throw new NotFoundException(404, "attribute id not available");
    }
    return attribute.getAttribute();
  }

  public List<Attribute> getAttributeLabelSet(Account account, List<String> schemas, List<AccountLabel> labels) {

    // filter results
    List<AccountAttribute> attributes = accountAttributeRepository.findDistinctByAccountAndSchemaIdInAndAccountLabelsIn(account, schemas, labels);

    // extract attribute data
    List<Attribute> attr = new ArrayList<Attribute>();
    for(AccountAttribute a: attributes) {
      attr.add(a.getAttribute());
    }
    return attr;
  }

  public List<AttributeView> getAttributeViewLabelSet(Account account, List<String> schemas, List<AccountLabel> labels) {

    // filter results
    List<AccountAttribute> attributes = accountAttributeRepository.findDistinctByAccountAndSchemaIdInAndAccountLabelsIn(account, schemas, labels);
   
    return setAttributeView(attributes);
  }

  public List<AttributeEntry> getAttributeSet(Account account, List<String> keys) {

    // filter results
    List<AccountAttribute> attributes;
    if(keys == null) {
      attributes = accountAttributeRepository.findByAccount(account);
    }
    else {
      attributes = accountAttributeRepository.findDistinctByAccountAndSchemaIdIn(account, keys);
    }

    // convert to base class
    List<AttributeEntry> entries = new ArrayList<AttributeEntry>();
    for(AccountAttribute attribute: attributes) {
      entries.add(setAttributeEntry(attribute));
    }
    return entries;
  }

  public List<AttributeView> getProfileViews(Account account, List<String> keys) {

    // filter attributes
    List<AccountAttribute> attributes = accountAttributeRepository.findDistinctByAccountAndSchemaIdIn(account, keys);

    return setAttributeView(attributes);
  }

  @Transactional
  public AttributeEntry addAttribute(Account account, String key, String value) {

    // increment profile revision
    Integer revision = account.getProfileRevision() + 1;
    account.setProfileRevision(revision);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);

    // construct new attribute
    AccountAttribute attribute = new AccountAttribute(account, key, value);
    attribute.setRevision(revision);
    attribute = accountAttributeRepository.save(attribute);
    return setAttributeEntry(attribute);
  }

  @Transactional
  public AttributeEntry updateAttribute(Account account, String attributeId, String key, String value)
        throws NotFoundException {

    // increment profile revision
    Integer revision = account.getProfileRevision() + 1;
    account.setProfileRevision(revision);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);
    
    // retrieve current entry
    AccountAttribute attribute = getAttribute(account, attributeId);
    
    // update entry
    attribute.setSchemaId(key);
    attribute.setValue(value);
    attribute.setRevision(revision);
    attribute = accountAttributeRepository.save(attribute);
    return setAttributeEntry(attribute);
  }

  @Transactional
  public void deleteAttribute(Account account, String attributeId) throws NotFoundException {
    
    // delete attribute
    AccountAttribute attribute = getAttribute(account, attributeId);
    accountAttributeRepository.delete(attribute);

    // increment profile revision
    account.setProfileRevision(account.getProfileRevision() + 1);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);
  }

  public AttributeEntry addAttributeLabel(Account account, String labelId, String attributeId) throws NotFoundException {
  
    AccountAttribute attribute = setAttributeLabel(account, labelId, attributeId);
    return setAttributeEntry(attribute);
  }

  @Transactional
  public AttributeEntry setAttributeLabels(Account account, String attributeId, List<String> labelIds) throws NotFoundException {

    // retrieve entities
    AccountAttribute attribute = getAttribute(account, attributeId);
    List<AccountLabel> labels = accountLabelRepository.findByAccountAndLabelIdIn(account, labelIds);

    // increment profile revision
    Integer revision = account.getProfileRevision() + 1;
    account.setProfileRevision(revision);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);
    
    // retrieve attribute to associate
    attribute.setAccountLabels(labels);
    attribute.setRevision(revision);
    attribute = accountAttributeRepository.save(attribute);

    return setAttributeEntry(attribute);
  }

  @Transactional
  private AccountAttribute setAttributeLabel(Account account, String labelId, String attributeId) throws NotFoundException {

    // retrieve entities
    AccountAttribute attribute = getAttribute(account, attributeId);
    AccountLabel label = getLabel(account, labelId);
    
    // increment profile revision
    Integer revision = account.getProfileRevision() + 1;
    account.setProfileRevision(revision);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);
    
    // retrieve attribute to associate
    attribute.getAccountLabels().add(label);
    attribute.setRevision(revision);

    return accountAttributeRepository.save(attribute);
  }

  public AttributeEntry deleteAttributeLabel(Account account, String labelId, String attributeId) throws NotFoundException {

    AccountAttribute attribute = removeAttributeLabel(account, labelId, attributeId);
    return setAttributeEntry(attribute);
  }
 
  @Transactional
  private AccountAttribute removeAttributeLabel(Account account, String labelId, String attributeId) throws NotFoundException {

    // retrieve entites 
    AccountAttribute attribute = getAttribute(account, attributeId);
    AccountLabel label = getLabel(account, labelId);

    // increment profile revision
    Integer revision = account.getProfileRevision() + 1;
    account.setProfileRevision(revision);
    account.setContactRevision(account.getContactRevision() + 1);
    accountRepository.save(account);

    // make sure attribute and labels exits
    attribute.setRevision(revision);
    Iterator<AccountLabel> labels = attribute.getAccountLabels().iterator();
    while(labels.hasNext()) {
      AccountLabel l = labels.next();
      if(l.getLabelId().equals(labelId)) {
        labels.remove();
      }
    }   
    
    // return updated attribute
    return accountAttributeRepository.save(attribute);
  }

}
