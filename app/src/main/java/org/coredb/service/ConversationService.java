package org.coredb.service;
  
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.time.Instant;
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
import javax.ws.rs.NotAcceptableException;
import java.nio.file.AccessDeniedException;

import org.coredb.model.InsightView;
import org.coredb.model.Insight;
import org.coredb.model.DialogueView;
import org.coredb.model.Dialogue;
import org.coredb.model.TopicView;
import org.coredb.model.Topic;
import org.coredb.model.Blurb;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountInsight;
import org.coredb.jpa.entity.AccountDialogue;
import org.coredb.jpa.entity.DialogueTopic;
import org.coredb.jpa.entity.TopicBlurb;

import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountDialogueRepository;
import org.coredb.jpa.repository.AccountInsightRepository;
import org.coredb.jpa.repository.DialogueTopicRepository;
import org.coredb.jpa.repository.TopicBlurbRepository;

import static org.coredb.NameRegistry.*;

@Service
public class ConversationService {

  @Autowired
  private AccountDialogueRepository dialogueRepository;

  @Autowired
  private AccountInsightRepository insightRepository;

  @Autowired
  private DialogueTopicRepository topicRepository;

  @Autowired
  private TopicBlurbRepository blurbRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EmigoEntityRepository emigoRepository;

  @Autowired
  private ConfigService configService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public Insight getInsight(Account account, String amigoId, String dialogueId) throws NotFoundException {
  
    // find specified amigo
    EmigoEntity amigo = emigoRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "specified amigo not found");
    }

    // find specified insight
    AccountInsight insight = insightRepository.findOneByAccountAndEmigoAndDialogueId(account, amigo, dialogueId);
    if(insight == null) {
      throw new NotFoundException(404, "specified insight not found");
    }

    return insight;
  }

  @Transactional 
  public void removeInsight(Account account, String amigoId, String dialogueId) throws NotFoundException {
  
    // find specified amigo
    EmigoEntity amigo = emigoRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "specified amigo not found");
    }

    // find specified insight
    AccountInsight insight = insightRepository.findOneByAccountAndEmigoAndDialogueId(account, amigo, dialogueId);
    if(insight == null) {
      throw new NotFoundException(404, "specified insight not found");
    }

    // increment commentary
    account.setInsightRevision(account.getInsightRevision() + 1);
    accountRepository.save(account);

    insightRepository.delete(insight);
  }
   
  public Dialogue getDialogue(Account account, String amigoId, String dialogueId) throws NotFoundException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // amigo must be assigned
    if(amigoId != null) {
      if(!amigoId.equals(dialogue.getAmigoId())) {
        throw new AccessDeniedException("amigo not assigned to dialogue");
      }
    }

    return dialogue;
  }

  @Transactional 
  public void removeDialogue(Account account, String dialogueId) throws NotFoundException {
 
    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);

    blurbRepository.deleteByDialogue(dialogue);
    topicRepository.deleteByDialogue(dialogue);
    dialogueRepository.delete(dialogue);
  }

  @Transactional
  public Dialogue addDialogue(Account account, String amigoId) throws NotFoundException, IllegalStateException {

    // find specified amigo
    EmigoEntity amigo = emigoRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "specified amigo not found");
    }

    // check if dialogue limit is reached
    Long count = dialogueRepository.countByAccount(account);
    if(count >= configService.getAccountNumValue(account.getEmigoId(), AC_DIALOGUE_LIMIT, (long)4096)) {
      throw new IllegalStateException("dialogue limit reached");
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);

    // construct new dialogue
    AccountDialogue dialogue = new AccountDialogue();
    dialogue.setAccount(account);
    dialogue.setEmigo(amigo);
    return dialogueRepository.save(dialogue);
  }
    
  @Transactional
  public Dialogue setDialogue(Account account, String amigoId, String dialogueId, Boolean linked, Boolean synced, Boolean active, Integer revision) throws NotFoundException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // amigo must be assigned
    if(amigoId != null) {
      if(!amigoId.equals(dialogue.getAmigoId())) {
        throw new AccessDeniedException("amigo not assigned to dialogue");
      }
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);
    
    // apply updated state
    if(linked != null) {
      dialogue.setLinked(linked);
    }
    if(active != null) {
      dialogue.setActive(active);
    }

    // sync only on matching revision
    if(revision == null || revision == dialogue.getRevision()) {
      if(synced != null) {
        dialogue.setSynced(synced);
      }
    }

    // apply changes
    Long cur = Instant.now().getEpochSecond();
    dialogue.setModified(cur.intValue());
    dialogue.setRevision(dialogue.getRevision() + 1);
    return dialogueRepository.save(dialogue);
  }

  @Transactional
  public Blurb setBlurb(Account account, String amigoId, String dialogueId, String blurbId, String schema, String data) throws NotFoundException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // find specified blurb
    TopicBlurb blurb = blurbRepository.findOneByDialogueAndBlurbId(dialogue, blurbId);
    if(blurb == null) {
      throw new NotFoundException(404, "blurb not found");
    }

    // check if amigo is author
    if(!amigoId.equals(blurb.getAmigoId())) {
      throw new AccessDeniedException("amigo not assigned to blurb");
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);

    // update dialogue revision
    Long cur = Instant.now().getEpochSecond();
    dialogue.setModified(cur.intValue());
    Integer revision = dialogue.getRevision() + 1;
    dialogue.setSynced(false);
    dialogue.setRevision(revision);
    dialogueRepository.save(dialogue);
    
    // update topic revision
    DialogueTopic topic = blurb.getTopic();
    topic.setRevision(revision);
    topicRepository.save(topic);

    // update blurb
    if(schema != null) {
      blurb.setSchema(schema);
    }
    blurb.setData(data);    
    blurb.setRevision(revision);
    return blurbRepository.save(blurb);
  }

  @Transactional
  public void clearBlurb(Account account, String amigoId, String dialogueId, String blurbId) throws NotFoundException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // find specified blurb
    TopicBlurb blurb = blurbRepository.findOneByDialogueAndBlurbId(dialogue, blurbId);
    if(blurb == null) {
      throw new NotFoundException(404, "blurb not found");
    }

    // check if amigo is author
    if(amigoId != null && !amigoId.equals(blurb.getAmigoId())) {
      throw new AccessDeniedException("amigo not assigned to blurb");
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);

    // update dialogue revision
    Long cur = Instant.now().getEpochSecond();
    dialogue.setModified(cur.intValue());
    Integer revision = dialogue.getRevision() + 1;
    dialogue.setRevision(revision);
    dialogue.setSynced(false);
    dialogueRepository.save(dialogue);
    
    // update topic revision
    DialogueTopic topic = blurb.getTopic();
    topic.setRevision(revision);
    topicRepository.save(topic);

    // delete blurb
    blurbRepository.delete(blurb);
  }

  @Transactional
  public Insight addInsight(Account account, String amigoId, String dialogueId, Integer revision) throws NotFoundException, IllegalStateException {

    // find specified amigo
    EmigoEntity amigo = emigoRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "specified amigo not found");
    }

    // check if dialogue limit is reached
    Long count = insightRepository.countByAccount(account);
    if(count >= configService.getAccountNumValue(account.getEmigoId(), AC_INSIGHT_LIMIT, (long)4096)) {
      throw new IllegalStateException("insight limit reached");
    }

    // increment commentary
    account.setInsightRevision(account.getInsightRevision() + 1);
    accountRepository.save(account);
  
    // construct new dialogue
    AccountInsight insight = new AccountInsight();
    insight.setAccount(account);
    insight.setEmigo(amigo);
    insight.setRevision(revision);
    insight.setDialogueId(dialogueId);
    return insightRepository.save(insight);
  }

  @Transactional
  public Insight setInsight(Account account, String amigoId, String dialogueId, Integer revision) throws NotFoundException {
 
    // find specified amigo
    EmigoEntity amigo = emigoRepository.findOneByEmigoId(amigoId);
    if(amigo == null) {
      throw new NotFoundException(404, "specified amigo not found");
    }

    // find specified insight
    AccountInsight insight = insightRepository.findOneByAccountAndEmigoAndDialogueId(account, amigo, dialogueId);
    if(insight == null) {
      throw new NotFoundException(404, "specified insight not found");
    }    

    // increment commentary
    account.setInsightRevision(account.getInsightRevision() + 1);
    accountRepository.save(account);

    // update revision if greater     
    if(revision> insight.getRevision()) {
      insight.setRevision(revision);
      insight = insightRepository.save(insight);
    }
    return insight;
  } 

  @Transactional
  public Blurb addBlurb(Account account, String amigoId, String dialogueId, String schema, String data) throws NotFoundException, IllegalStateException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    if(!dialogue.getActive()) {
      throw new NotAcceptableException("conversation is not active");
    }

    // validate amigo access
    if(amigoId != null) {
      if(!amigoId.equals(dialogue.getAmigoId())) {
        throw new AccessDeniedException("amigo not assigned");
      }
    }
    else {
      amigoId = account.getEmigoId();
    }

    // increment conversation
    account.setDialogueRevision(account.getDialogueRevision() + 1);
    accountRepository.save(account);
    
    // increment revisions
    Long cur = Instant.now().getEpochSecond();
    dialogue.setModified(cur.intValue());
    dialogue.setSynced(false);
    dialogue.setRevision(dialogue.getRevision() + 1);
    dialogue = dialogueRepository.save(dialogue);

    // find latest topic
    DialogueTopic topic = topicRepository.findTopByDialogueOrderByIdDesc(dialogue);
    if(topic == null || topic.getTopicBlurbs().size() >= configService.getAccountNumValue(account.getEmigoId(), AC_BLURB_TOPIC_LIMIT, (long)64)) {
      Long count = topicRepository.countByDialogue(dialogue);
      if(count >= configService.getAccountNumValue(account.getEmigoId(), AC_TOPIC_LIMIT, (long)1024)) {
        throw new IllegalStateException("conversation is full");
      }
      topic = new DialogueTopic();
      topic.setDialogue(dialogue);
    }
    topic.setRevision(dialogue.getRevision());
    topic = topicRepository.save(topic);

    // create new blurb
    TopicBlurb blurb = new TopicBlurb();
    blurb.setDialogue(dialogue);
    blurb.setTopic(topic);
    blurb.setAmigoId(amigoId);
    blurb.setSchema(schema);
    blurb.setData(data);
    blurb.setRevision(dialogue.getRevision());
    return blurbRepository.save(blurb);
  }

  public List<DialogueView> getDialogueViews(Account account) {

    // find all dialogues
    List<DialogueView> views = new ArrayList<DialogueView>();
    List<AccountDialogue> dialogues = dialogueRepository.findByAccount(account);
    for(AccountDialogue dialogue: dialogues) {
      DialogueView view = new DialogueView();
      view.setDialogueId(dialogue.getDialogueId());
      view.setRevision(dialogue.getRevision());
      views.add(view);
    }
    return views;
  }

  public List<InsightView> getInsightViews(Account account) {
    
    // find all isngiths
    List<InsightView> views = new ArrayList<InsightView>();
    List<AccountInsight> insights = insightRepository.findByAccount(account);
    for(AccountInsight insight: insights) {
      InsightView view = new InsightView();
      view.setAmigoId(insight.getAmigoId());
      view.setDialogueId(insight.getDialogueId());
      view.setRevision(insight.getRevision());
      views.add(view);
    }
    return views;
  }

  public List<TopicView> getTopicViews(Account account, String amigoId, String dialogueId) throws NotFoundException, AccessDeniedException {

    // find referenced dialoge
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // amigo must be associated
    if(amigoId != null) {
      if(!amigoId.equals(dialogue.getAmigoId())) {
        throw new AccessDeniedException("amigo not assigned to dialogue");
      }
    }

    // find all topics
    List<TopicView> views = new ArrayList<TopicView>();
    List<DialogueTopic> topics = topicRepository.findByDialogueOrderByIdDesc(dialogue);
    Integer position = 0;
    for(DialogueTopic topic: topics) {
      TopicView view = new TopicView();
      view.setTopicId(topic.getTopicId());
      view.setPosition(position++);
      view.setRevision(topic.getRevision());
      views.add(view);
    }
    return views;  
  }

  public Topic getTopic(Account account, String amigoId, String dialogueId, String topicId, String schema) throws NotFoundException, AccessDeniedException {

    // find referenced dialogue
    AccountDialogue dialogue = dialogueRepository.findOneByAccountAndDialogueId(account, dialogueId);
    if(dialogue == null) {
      throw new NotFoundException(404, "dialogue not found");
    }

    // amigo must be associated
    if(amigoId != null) {
      if(!amigoId.equals(dialogue.getAmigoId())) {
        throw new AccessDeniedException("amigo not assigned to dialogue");
      }
    }

    // find referenced topic
    DialogueTopic topic = topicRepository.findOneByDialogueAndTopicId(dialogue, topicId);
    if(topic == null) {
      throw new NotFoundException(404, "dialogue not found");
    }
  
    // remove non matching schemas
    if(schema != null) {
      List<TopicBlurb> blurbs = new ArrayList<TopicBlurb>();
      for(TopicBlurb blurb: topic.getTopicBlurbs()) {
        if(schema.equals(blurb.getSchema())) {
          blurbs.add(blurb);
        }
      }
      topic.setTopicBlurbs(blurbs);
    }

    return topic;
  }

}
