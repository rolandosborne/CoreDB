package org.coredb.test.integration;

import java.util.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import org.coredb.jpa.entity.*;
import org.coredb.jpa.repository.*;
import org.coredb.model.*;
import org.coredb.service.*;
import static org.coredb.NameRegistry.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.core.io.InputStreamResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.LinkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import org.coredb.api.*;
import org.coredb.model.*;
import org.coredb.service.*;
import org.coredb.jpa.entity.*;
import org.coredb.jpa.repository.*;
import static org.coredb.NameRegistry.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageAppTest {

  @Autowired
  private ServerConfigRepository configRepository;

  @Autowired
  private AccountSubjectRepository subjectRepository;

  @Autowired
  private AccountSubjectAssetRepository assetRepository;

  @Autowired
  private ConfigService configService;
  
  @Autowired
  private AccessApi accessApi;

  @Autowired
  private AccountApi accountApi;

  @Autowired
  private IdentityApi identityApi;

  @Autowired
  private PromptApi promptApi;

  @Autowired
  private GroupApi groupApi;

  @Autowired
  private ProfileApi profileApi;

  @Autowired
  private ShareApi shareApi;

  @Autowired
  private IndexApi indexApi;

  @Autowired
  private AgentApi agentApi;

  @Autowired
  private ContactApi contactApi;

  @Autowired
  private ShowApi showApi;

  @Autowired
  private ViewApi viewApi;

  @Autowired
  private TokenApi tokenApi;

  @Autowired
  private ConversationApi conversationApi;

  @Autowired
  private CommentaryApi commentaryApi;

  private class TokenPair {
    public String a2b;
    public String b2a;
  }

  @Before
  public void setup() throws Exception {
    ServerConfig config;

    // set access token
    config = new ServerConfig();
    config.setConfigId(SC_ACCESS_TOKEN);
    config.setStrValue("test_case_token");
    configRepository.save(config);

    // set node path
    config = new ServerConfig();
    config.setConfigId(SC_SERVER_HOST_PORT);
    config.setStrValue("https://localhost:8432/db");
    configRepository.save(config);
  
    // load config values
    configService.refresh();
  }

  @Test
  public void useCase() throws Exception {

    // setup connection between accounts
    String aToken = addPerson("PERSON_A");
    String bToken = addPerson("PERSON_B"); 
    TokenPair com = connect(aToken, bToken); 

    String aId = identityApi.getIdentity(aToken).getBody().getAmigoId();
    String bId = identityApi.getIdentity(bToken).getBody().getAmigoId();

    // get an agent authorization
    ResponseEntity<AuthMessage> aAuthMessage = agentApi.getAgentMessage(aToken);
    assertEquals(HttpStatus.OK, aAuthMessage.getStatusCode());
    ResponseEntity<Auth> aAuth = tokenApi.setAgentMessage(com.a2b, aAuthMessage.getBody());

    // get an agent authorization
    ResponseEntity<AuthMessage> bAuthMessage = agentApi.getAgentMessage(bToken);
    assertEquals(HttpStatus.OK, bAuthMessage.getStatusCode());
    ResponseEntity<Auth> bAuth = tokenApi.setAgentMessage(com.b2a, bAuthMessage.getBody());

    // setup agent pair
    TokenPair agent = new TokenPair();
    agent.a2b = aAuth.getBody().getToken();
    agent.b2a = bAuth.getBody().getToken();


    // create new dialogue
    ResponseEntity<Dialogue> dialogue = conversationApi.addConversationDialogue(aToken, bId);
    assertEquals(HttpStatus.CREATED, dialogue.getStatusCode());
    String dialogueId = dialogue.getBody().getDialogueId();
    Integer revision = dialogue.getBody().getRevision();

    // create new insight
    ResponseEntity<Insight> insight = commentaryApi.addCommentaryInsight(com.a2b, dialogueId, revision, agent.a2b);
    assertEquals(HttpStatus.CREATED, insight.getStatusCode());

    // set dialogue as linked
    ResponseEntity<Dialogue> linked = conversationApi.setConversationDialogue(aToken, dialogueId, true, true, true, revision);
    assertEquals(HttpStatus.OK, linked.getStatusCode());

    // declare conversation views
    Map<String, Topic> aDialogue = new HashMap<String, Topic>();
    Map<String, Topic> bDialogue = new HashMap<String, Topic>();

    // add to conversation
    Integer loops = 100;
    for(Integer i = 0; i < loops; i++) {

      // a post blurb
      ResponseEntity<Blurb> aBlurb = conversationApi.addConversationBlurb(aToken, "001122", dialogueId, "ABLURB:" + i);
      assertEquals(HttpStatus.CREATED, aBlurb.getStatusCode());

      // update insight
      ResponseEntity<Insight> aInsightSync = commentaryApi.updateCommentaryInsight(com.a2b, dialogueId, agent.a2b, aBlurb.getBody().getRevision());
      assertEquals(HttpStatus.OK, aInsightSync.getStatusCode());

      // update dialogue
      ResponseEntity<Dialogue> aDialogueSync = conversationApi.setConversationDialogue(aToken, dialogueId, null, true, null, aBlurb.getBody().getRevision());
      assertEquals(HttpStatus.OK, aDialogueSync.getStatusCode());

      // a sync dialogue
      aDialogue = syncConversation(aToken, dialogueId, aDialogue);

      // b sync commentary
      bDialogue = syncCommentary(bToken, com.b2a, agent.b2a, dialogueId, bDialogue);

      // a post blurb
      ResponseEntity<Blurb> bBlurb = commentaryApi.addCommentaryBlurb(com.b2a, "001122", dialogueId, agent.b2a, "BBLURA:" + i);
      assertEquals(HttpStatus.CREATED, aBlurb.getStatusCode());

      // update insight
      ResponseEntity<Insight> bInsightSync = conversationApi.updateConversationInsight(bToken, aId, dialogueId, bBlurb.getBody().getRevision());
      assertEquals(HttpStatus.OK, bInsightSync.getStatusCode());

      // update dialogue
      ResponseEntity<Dialogue> bDialogueSync = commentaryApi.updateCommentaryDialogue(com.b2a, agent.b2a, dialogueId, true, null, bBlurb.getBody().getRevision());
      assertEquals(HttpStatus.OK, bDialogueSync.getStatusCode());

      // a sync dialogue
      aDialogue = syncConversation(aToken, dialogueId, aDialogue);

      // b sync commentary
      bDialogue = syncCommentary(bToken, com.b2a, agent.b2a, dialogueId, bDialogue);
    }

    // validate a view of dialogue
    Integer aCount = 0;
    for(Map.Entry<String, Topic> entry : aDialogue.entrySet()) {
      aCount += entry.getValue().getBlurbs().size();
    }
    assertTrue(loops * 2 == aCount);

    // validate b view of dialogue
    Integer bCount = 0;
    for(Map.Entry<String, Topic> entry : bDialogue.entrySet()) {
      bCount += entry.getValue().getBlurbs().size();
    }
    assertTrue(loops * 2 == bCount);

    // test update and delete
    ResponseEntity<Blurb> blurb = conversationApi.addConversationBlurb(aToken, "001122", dialogueId, "SAMPLE");
    assertEquals(HttpStatus.CREATED, blurb.getStatusCode());
    assertTrue(blurb.getBody().getData().equals("SAMPLE"));
    blurb = conversationApi.setConversationBlurb(aToken, "001122", dialogueId, blurb.getBody().getBlurbId(), "UPDATED");
    assertEquals(HttpStatus.OK, blurb.getStatusCode());
    assertTrue(blurb.getBody().getData().equals("UPDATED"));
    ResponseEntity<Void> del = conversationApi.removeConversationBlurb(aToken, dialogueId, blurb.getBody().getBlurbId());
    assertEquals(HttpStatus.OK, del.getStatusCode());  
    del = conversationApi.removeConversationBlurb(aToken, dialogueId, blurb.getBody().getBlurbId());
    assertNotEquals(HttpStatus.OK, del.getStatusCode());  

    // test update and delete
    blurb = commentaryApi.addCommentaryBlurb(com.b2a, "001122", dialogueId, agent.b2a, "SAMPLE");
    assertEquals(HttpStatus.CREATED, blurb.getStatusCode());
    blurb = commentaryApi.setCommentaryBlurb(com.b2a, "001122", dialogueId, blurb.getBody().getBlurbId(), agent.b2a, "UPDATED");
    assertEquals(HttpStatus.OK, blurb.getStatusCode());
    del = commentaryApi.removeCommentaryBlurb(com.b2a, dialogueId, blurb.getBody().getBlurbId(), agent.b2a);
    assertEquals(HttpStatus.OK, del.getStatusCode());
    del = commentaryApi.removeCommentaryBlurb(com.b2a, dialogueId, blurb.getBody().getBlurbId(), agent.b2a);
    assertNotEquals(HttpStatus.OK, del.getStatusCode());

    // test insight deletion
    ResponseEntity<Insight> insighted = conversationApi.getConversationInsight(bToken, aId, dialogueId);
    assertEquals(HttpStatus.OK, insighted.getStatusCode());
    ResponseEntity<Dialogue> dialogued = commentaryApi.getCommentaryDialogue(com.b2a, dialogueId, agent.b2a);
    assertEquals(HttpStatus.OK, dialogued.getStatusCode());
    del = commentaryApi.removeCommentaryInsight(com.a2b, dialogueId, agent.a2b);
    assertEquals(HttpStatus.OK, del.getStatusCode());
    insighted = conversationApi.getConversationInsight(bToken, aId, dialogueId);
    assertNotEquals(HttpStatus.OK, insighted.getStatusCode());

    // test insight deletion
    insighted = commentaryApi.addCommentaryInsight(com.a2b, dialogueId, revision, agent.a2b);
    assertEquals(HttpStatus.CREATED, insighted.getStatusCode());
    del = conversationApi.removeConversationInsight(bToken, aId, dialogueId);
    assertEquals(HttpStatus.OK, del.getStatusCode());
    insighted = conversationApi.getConversationInsight(bToken, aId, dialogueId);
    assertNotEquals(HttpStatus.OK, insighted.getStatusCode());

    // test dialogue deletion
    dialogued = conversationApi.getConversationDialogue(aToken, dialogueId);
    assertEquals(HttpStatus.OK, dialogued.getStatusCode());
    del = conversationApi.removeConversationDialogue(aToken, dialogueId);
    assertEquals(HttpStatus.OK, del.getStatusCode());
    dialogued = conversationApi.getConversationDialogue(aToken, dialogueId);
    assertNotEquals(HttpStatus.OK, dialogued.getStatusCode());
  }

  private Map<String, Topic> syncConversation(String token, String dialogueId, Map<String, Topic> topics) {
   
    // get current view of dialogues
    ResponseEntity<List<DialogueView>> dialogueViews = conversationApi.getConversationDialogueViews(token);
    assertEquals(HttpStatus.OK, dialogueViews.getStatusCode());
    assertEquals(dialogueViews.getBody().size(), 1);
    assertTrue(dialogueViews.getBody().get(0).getDialogueId().equals(dialogueId));

    // get topic view of dialogue
    ResponseEntity<List<TopicView>> topicViews = conversationApi.getConversationTopicViews(dialogueId, token);
    assertEquals(HttpStatus.OK, topicViews.getStatusCode());
    
    // rebuild dialogue
    Map<String, Topic> dialogue = new HashMap<String, Topic>();
    for(TopicView view: topicViews.getBody()) {
      Topic topic = topics.get(view.getTopicId());
      if(topic == null || topic.getRevision() != view.getRevision()) {
        ResponseEntity<Topic> blurbs = conversationApi.getConversationTopic(dialogueId, view.getTopicId(), token, null);
        assertEquals(HttpStatus.OK, blurbs.getStatusCode());
        dialogue.put(view.getTopicId(), blurbs.getBody());
      }
      else {
        dialogue.put(view.getTopicId(), topic);
      }
    }

    return dialogue;
  }

  private Map<String, Topic> syncCommentary(String token, String share, String agent, String dialogueId, Map<String, Topic> topics) {

    // get current view of insights
    ResponseEntity<List<InsightView>> insightViews = conversationApi.getConversationInsightViews(token);
    assertEquals(HttpStatus.OK, insightViews.getStatusCode());
    assertEquals(insightViews.getBody().size(), 1);
    assertTrue(insightViews.getBody().get(0).getDialogueId().equals(dialogueId));

    // get topic view of dialogue
    ResponseEntity<List<TopicView>> topicViews = commentaryApi.getCommentaryTopicViews(dialogueId, share, agent);
    assertEquals(HttpStatus.OK, topicViews.getStatusCode());

    // rebuild insight
    Map<String, Topic> insight = new HashMap<String, Topic>();
    for(TopicView view: topicViews.getBody()) {
      Topic topic = topics.get(view.getTopicId());
      if(topic == null || topic.getRevision() != view.getRevision()) {
        ResponseEntity<Topic> blurbs = commentaryApi.getCommentaryTopic(dialogueId, view.getTopicId(), share, null, agent);
        assertEquals(HttpStatus.OK, blurbs.getStatusCode());
        insight.put(view.getTopicId(), blurbs.getBody());
      }
      else {
        insight.put(view.getTopicId(), topic);
      }
    }

    return insight;
  }

  private TokenPair connect(String a, String b) throws Exception {

    ResponseEntity<AmigoMessage> bMsg = identityApi.getIdentityMessage(b);
    assertEquals(HttpStatus.OK, bMsg.getStatusCode());
    assertNotEquals(bMsg.getBody(), null);

    ResponseEntity<AmigoMessage> aMsg = identityApi.getIdentityMessage(a);
    assertEquals(HttpStatus.OK, aMsg.getStatusCode());
    assertNotEquals(aMsg.getBody(), null);
 
    ResponseEntity<Amigo> bAmigo = identityApi.getIdentity(b);
    assertEquals(HttpStatus.OK, bAmigo.getStatusCode());
    assertNotEquals(bAmigo.getBody(), null);    
 
    ResponseEntity<Amigo> aAmigo = identityApi.getIdentity(a);
    assertEquals(HttpStatus.OK, aAmigo.getStatusCode());
    assertNotEquals(aAmigo.getBody(), null);    
  
    ResponseEntity<AmigoEntry> entry = indexApi.addAmigo(a, bMsg.getBody());
    assertEquals(HttpStatus.CREATED, entry.getStatusCode());
    assertNotEquals(entry.getBody(), null);

    ResponseEntity<ShareEntry> share = shareApi.addConnection(a, entry.getBody().getAmigoId());
    assertEquals(HttpStatus.CREATED, share.getStatusCode());
    assertNotEquals(share.getBody(), null);
    String shareId = share.getBody().getShareId();

    ResponseEntity<ShareMessage> shareMsg = shareApi.getShareMessage(a, shareId);
    assertEquals(HttpStatus.OK, shareMsg.getStatusCode());
    assertNotEquals(shareMsg.getBody(), null);

    ResponseEntity<ShareStatus> status = shareApi.setShareMessage(bAmigo.getBody().getAmigoId(), shareMsg.getBody());
    assertEquals(HttpStatus.CREATED, status.getStatusCode());
    assertNotEquals(status.getBody(), null);

    ResponseEntity<List<PendingAmigoView>> pending = indexApi.getAmigoRequests(b);
    assertEquals(HttpStatus.OK, pending.getStatusCode());
    assertNotEquals(pending.getBody(), null);
    assertEquals(pending.getBody().size(), 1);

    ResponseEntity<AmigoEntry> allow = indexApi.addAmigo(b, aMsg.getBody());
    assertEquals(HttpStatus.CREATED, allow.getStatusCode());
    assertNotEquals(allow.getBody(), null);

    ResponseEntity<List<ShareEntry>> shares = shareApi.getConnections(b);
    assertEquals(HttpStatus.OK, shares.getStatusCode());
    assertNotEquals(shares.getBody(), null);
    assertEquals(shares.getBody().size(), 1);
    
    ResponseEntity<ShareEntry> accept = shareApi.updateConnection(b, shares.getBody().get(0).getShareId(), "requesting", null);
    assertEquals(HttpStatus.OK, accept.getStatusCode());
    assertNotEquals(accept.getBody(), null);

    ResponseEntity<ShareMessage> acceptMsg = shareApi.getShareMessage(b, shares.getBody().get(0).getShareId()); 
    assertEquals(HttpStatus.OK, acceptMsg.getStatusCode());
    assertNotEquals(acceptMsg.getBody(), null);

    ResponseEntity<ShareStatus> accepted = shareApi.setShareMessage(aAmigo.getBody().getAmigoId(), acceptMsg.getBody());
    assertEquals(HttpStatus.CREATED, accepted.getStatusCode());
    assertNotEquals(accepted.getBody(), null);
    assertEquals(accepted.getBody().getShareStatus().toString().equals("connected"), true);

    ResponseEntity<ShareEntry> confirm = shareApi.updateConnection(b, shares.getBody().get(0).getShareId(), "connected", accepted.getBody().getConnected());
    assertEquals(HttpStatus.OK, confirm.getStatusCode());
    assertNotEquals(confirm.getBody(), null);

    TokenPair pair = new TokenPair();
    shares = shareApi.getConnections(a);
    assertEquals(shares.getBody().size(), 1);
    pair.a2b = shares.getBody().get(0).getToken();
    shares = shareApi.getConnections(b);
    assertEquals(shares.getBody().size(), 1);
    pair.b2a = shares.getBody().get(0).getToken();
    return pair;
  }

  private String addPerson(String handle) throws Exception {
    ResponseEntity<AmigoToken> token = accessApi.createAmigo("test_case_token");
    assertEquals(HttpStatus.CREATED, token.getStatusCode());
    assertNotEquals(token.getBody(), null);
    String access = LinkUtil.getTokenObject(token.getBody()).getToken();

    ResponseEntity<AmigoMessage> emigo = identityApi.updateHandle(access, handle);
    emigo = identityApi.updateRegistry(access, "REGGYREGREG");
    assertEquals(HttpStatus.OK, emigo.getStatusCode());
    assertNotEquals(emigo.getBody(), null);

    return access;
  }

}

