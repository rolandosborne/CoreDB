package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.coredb.NameRegistry.*;

import java.lang.UnsupportedOperationException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountSubjectAsset;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.jpa.entity.AccountLabel;

import org.coredb.jpa.repository.AccountSubjectRepository;
import org.coredb.jpa.repository.AccountSubjectLabelIdRepository;
import org.coredb.jpa.repository.AccountSubjectAssetRepository;
import org.coredb.jpa.repository.AccountShareRepository;

import org.coredb.model.SubjectAsset;
import org.coredb.model.Subject;
import org.coredb.model.SubjectTag;
import org.coredb.model.SubjectEntry;
import org.coredb.model.SubjectView;
import org.coredb.model.AuthMessage;
import org.coredb.model.AssetData;
import org.coredb.model.ShareEntry;
import org.coredb.model.ShareEntry.StatusEnum;

@Service
public class ViewService {

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private AgentService agentService;

  @Autowired
  private ShowService showService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountShare getAccountShare(String token) throws AccessDeniedException {
        
    // load share connection
    AccountShare share = shareRepository.findOneByInToken(token);
    if(share == null) {
      throw new AccessDeniedException("share connection not found");
    }
    
    // connection must be connected
    if(share.getStatus() != StatusEnum.CONNECTED) {
      throw new AccessDeniedException("share entry not connected");
    }

    // retrun valid share
    return share;
  }

  public String addAgent(String token, AuthMessage message) 
      throws IllegalStateException, IllegalArgumentException, Exception {
    
    // load share connection
    AccountShare share = getAccountShare(token);

    // return extracted token
    return agentService.setAppAccess(share.getEmigo(), message);
  }

  public Integer getRevision(String token, String agent) 
      throws AccessDeniedException, UnsupportedOperationException {
 
    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return share.getAccount().getViewRevision();
  }  

  public List<Subject> getSubjectSet(String token, List<String> schemas, String agent)
      throws AccessDeniedException, UnsupportedOperationException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return subjects
    return showService.getSubjectLabelSet(share.getAccount(), schemas, share.getEmigo().getAccountLabels());
  }

  public List<SubjectView> getSubjectViewSet(String token, List<String> schemas, String agent)
      throws AccessDeniedException, UnsupportedOperationException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return subjects
    return showService.getSubjectViewLabelSet(share.getAccount(), schemas, share.getEmigo().getAccountLabels());
  }

  public Subject getSubject(String token, String agent, String subjectId) 
        throws UnsupportedOperationException, NotFoundException, AccessDeniedException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return subject           
    return showService.getSubjectLabelEntry(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId);
  }

  public AssetData getSubjectAsset(String token, String agent, String subjectId, String assetId, Long min, Long max)
        throws NotFoundException, AccessDeniedException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return asset data
    return showService.getSubjectLabelAsset(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId, assetId, min, max);
  }

  public AssetData getSubjectAsset(String token, String agent, String subjectId, String assetId)
        throws NotFoundException, AccessDeniedException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return asset data
    return showService.getSubjectLabelAsset(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId, assetId);
  }

  public SubjectTag getSubjectTag(String token, String agent, String subjectId, String schema, Boolean descending)
        throws NotFoundException, AccessDeniedException {
      
    // find share entry
    AccountShare share = getAccountShare(token);
   
    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return subject tag
    return showService.getSubjectLabelTag(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId, schema, descending);
  }

  public SubjectTag addSubjectTag(String token, String agent, String subjectId, String schema, Boolean descending, String data)
      throws NotFoundException, AccessDeniedException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return updated tag
    return showService.addSubjectLabelTag(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId, schema, descending, data, share.getEmigo().getEmigoId());
  }

  public SubjectTag removeSubjectTag(String token, String agent, String subjectId, String tagId, String schema, Boolean descending)
      throws NotFoundException, AccessDeniedException {

    // find share entry
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    // return updated tag
    return showService.removeSubjectLabelTag(share.getAccount(), share.getEmigo().getAccountLabels(), subjectId, tagId, schema, descending, share.getEmigo().getEmigoId());
  }

}

