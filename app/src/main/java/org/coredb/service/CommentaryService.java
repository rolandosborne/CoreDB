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

import org.coredb.model.Insight;
import org.coredb.model.Dialogue;
import org.coredb.model.TopicView;
import org.coredb.model.Topic;
import org.coredb.model.Blurb;
import org.coredb.model.ShareEntry.StatusEnum;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountShare;

import org.coredb.jpa.repository.AccountShareRepository;

import java.lang.UnsupportedOperationException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

@Service
public class CommentaryService {

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AgentService agentService;

  @Autowired
  private ConversationService conversationService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountShare getAccountShare(String token) throws AccessDeniedException, NotAcceptableException{
        
    // load share connection
    AccountShare share = shareRepository.findOneByInToken(token);
    if(share == null) {
      throw new AccessDeniedException("share connection not found");
    }
    
    // connection must be connected
    if(share.getStatus() != StatusEnum.CONNECTED) {
      throw new AccessDeniedException("share entry not connected");
    }

    // check if disabled
    if(!share.getAccount().getEnabled()) {
      throw new NotAcceptableException("account disabled");
    }

    // retrun valid share
    return share;
  }

  public Insight addInsight(String token, String agent, String dialogueId, Integer revision) throws AccessDeniedException, UnsupportedOperationException, IllegalStateException, NotAcceptableException, NotFoundException {
 
    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.addInsight(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, revision);
  }

  public Blurb addBlurb(String token, String schema, String dialogueId, String agent, String data) throws AccessDeniedException, UnsupportedOperationException, IllegalStateException, NotAcceptableException, NotFoundException {
 
    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.addBlurb(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, schema, data);
  }

  public Dialogue updateDialogue(String token, String agent, String dialogueId, Boolean active, Boolean synced, Integer revision) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {

    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.setDialogue(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, null, synced, active, revision);
  }

  public Insight updateInsight(String token, String agent, String dialogueId, Integer revision) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {
    
    // load share connection
    AccountShare share = getAccountShare(token);
    
    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.setInsight(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, revision);
  }

  public void removeInsight(String token, String agent, String dialogueId) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {
    
    // load share connection
    AccountShare share = getAccountShare(token);
    
    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    conversationService.removeInsight(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId);
  }

  public Blurb updateBlurb(String token, String schema, String dialogueId, String blurbId, String agent, String data) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {
    
    // load share connection
    AccountShare share = getAccountShare(token);
    
    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.setBlurb(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, blurbId, schema, data);
  }

  public void removeBlurb(String token, String dialogueId, String blurbId, String agent) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {
    
    // load share connection
    AccountShare share = getAccountShare(token);
    
    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    conversationService.clearBlurb(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, blurbId);
  }

  public List<TopicView> getTopicViews(String token, String agent, String dialogueId) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {

    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.getTopicViews(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId);
  }

  public Dialogue getDialogue(String token, String agent, String dialogueId) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {

    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.getDialogue(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId);
  }

  public Topic getTopic(String token, String agent, String dialogueId, String topicId, String schema) throws AccessDeniedException, UnsupportedOperationException, NotFoundException, NotAcceptableException, NotFoundException {

    // load share connection
    AccountShare share = getAccountShare(token);

    // validate agent
    agentService.checkAppToken(share.getEmigo(), agent);

    return conversationService.getTopic(share.getAccount(), share.getEmigo().getEmigoId(), dialogueId, topicId, schema);
  }

}
