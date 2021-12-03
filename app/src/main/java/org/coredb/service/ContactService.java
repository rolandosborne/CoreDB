package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.util.*;
import java.io.File;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.coredb.NameRegistry.*;

import java.security.InvalidParameterException;
import java.lang.UnsupportedOperationException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.jpa.entity.EmigoEntity;

import org.coredb.jpa.repository.AccountLabelRepository;
import org.coredb.jpa.repository.AccountAppRepository;
import org.coredb.jpa.repository.AccountShareRepository;
import org.coredb.jpa.repository.EmigoEntityRepository;

import org.coredb.model.Amigo;
import org.coredb.model.ShareEntry;
import org.coredb.model.ShareEntry.StatusEnum;
import org.coredb.model.LabelEntry;
import org.coredb.model.Attribute;
import org.coredb.model.AttributeView;
import org.coredb.model.AuthMessage;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountApp;


@Service
public class ContactService {

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AccountLabelRepository accountLabelRepository;

  @Autowired
  private AccountAppRepository accountAppRepository;

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AgentService agentService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountShare getAccountShare(String token) throws InvalidParameterException {
        
    // load share connection
    AccountShare share = shareRepository.findOneByInToken(token);
    if(share == null) {
      throw new InvalidParameterException("share connection not found");
    }
    if(share.getStatus() != StatusEnum.CONNECTED) {
      throw new InvalidParameterException("share connection not established");
    }
    return share;  
  }

  public String addAgent(String token, AuthMessage message)
      throws IllegalStateException, IllegalArgumentException, InvalidParameterException, Exception {

    // load share connection
    AccountShare share = getAccountShare(token);

    // return extracted token
    return agentService.setAppAccess(share.getEmigo(), message);
  }

  public Integer getRevision(String token, String agent) throws UnsupportedOperationException, AccessDeniedException, InvalidParameterException {
  
    // find specified account share
    AccountShare share = getAccountShare(token);

    // validate agent token
    agentService.checkAppToken(share.getEmigo(), agent);

    // return current contact revision
    return share.getAccount().getContactRevision();
  } 

  public Amigo getIdentity(String token, String agent) throws UnsupportedOperationException, InvalidParameterException, NotFoundException {
  
    // find specified account share
    AccountShare share = getAccountShare(token);

    // validate agent token
    agentService.checkAppToken(share.getEmigo(), agent);

    // return identity object
    EmigoEntity emigo = emigoEntityRepository.findOneByEmigoId(share.getAccount().getEmigoId());
    if(emigo == null) {
      throw new NotFoundException(404, "emigo entity not found");
    }
    return emigo;
  } 

  public List<Attribute> filterAttributes(String token, List<String> schemas, String agent) throws UnsupportedOperationException, InvalidParameterException {
    
    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return attribute set
    return profileService.getAttributeLabelSet(share.getAccount(), schemas, share.getEmigo().getAccountLabels());
  }

  public List<AttributeView> filterAttributeViews(String token, List<String> schemas, String agent) throws UnsupportedOperationException, InvalidParameterException {
    
    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return attribute set
    return profileService.getAttributeViewLabelSet(share.getAccount(), schemas, share.getEmigo().getAccountLabels());
  }

  public Attribute getAttribute(String token, String attributeId, String agent) throws UnsupportedOperationException, InvalidParameterException, NotFoundException {
  
    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return attribute
    return profileService.getAttributeLabelEntry(share.getAccount(), share.getEmigo().getAccountLabels(), attributeId);
  }
}









