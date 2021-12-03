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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.LinkUtil;

import org.coredb.api.*;
import org.coredb.model.*;
import org.coredb.service.*;
import org.coredb.jpa.entity.*;
import org.coredb.jpa.repository.*;
import static org.coredb.NameRegistry.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactAppTest {

  @Autowired
  private ServerConfigRepository configRepository;

  @Autowired
  private ConfigService configService;
  
  @Autowired
  private AccessApi accessApi;

  @Autowired
  private AccountApi accountApi;

  @Autowired
  private PromptApi promptApi;

  @Autowired
  private ProfileApi profileApi;

  @Autowired
  private IdentityApi identityApi;

  @Autowired
  private GroupApi groupApi;

  @Autowired
  private ShareApi shareApi;

  @Autowired
  private IndexApi indexApi;

  @Autowired
  private AgentApi agentApi;

  @Autowired
  private ContactApi contactApi;

  private String appToken;
  private String userAToken;
  private String userBToken;
  private String userAAppToken;
  private String userBAppToken;
  private String userAId;
  private String userBId;
  private AmigoMessage userAMessage;
  private AmigoMessage userBMessage;

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
  
      // account creation
      phase1();

      // account config
      phase2();

      // setup profile
      phase3();

      // request and accept
      phase4();

      // view attribute
      phase5();

      // disconnect and block
      phase6();
  }

  private void phase1() throws Exception {
    ResponseEntity<AmigoToken> resToken;
    ResponseEntity<LinkMessage> resLink;
    ResponseEntity<UserEntry> resUser;
    ResponseEntity<Pass> resPass;
    ServiceAccess access = new ServiceAccess();
    access.setEnableShow(false);
    access.setEnableIdentity(true);
    access.setEnableProfile(true);
    access.setEnableGroup(true);
    access.setEnableShare(true);
    access.setEnablePrompt(true);
    access.setEnableService(true);
    access.setEnableIndex(true);
    access.setEnableUser(true);
    access.setEnableAccess(true);
    access.setEnableAccount(true);
 
    // create app account
    resToken = accessApi.createAmigo("test_case_token");
    assertEquals(HttpStatus.CREATED, resToken.getStatusCode());
    assertNotEquals(resToken.getBody(), null);
    this.appToken = LinkUtil.getTokenObject(resToken.getBody()).getToken();

    // create userA account
    resToken = accessApi.createAmigo("test_case_token");
    assertEquals(HttpStatus.CREATED, resToken.getStatusCode());
    assertNotEquals(resToken.getBody(), null);
    Token token = LinkUtil.getTokenObject(resToken.getBody());
    Amigo emigoA = EmigoUtil.getObject(resToken.getBody().getAmigo());
    this.userAId = emigoA.getAmigoId();

    // generate pass for userA
    resPass = accessApi.generatePass(token.getToken(), null, access);
    assertEquals(HttpStatus.OK, resPass.getStatusCode());
    assertNotEquals(resPass.getBody(), null);
    String pass = resPass.getBody().getData();

    // authorize account creation
    resLink = accessApi.authorizeCreate(this.appToken, access);
    assertEquals(HttpStatus.OK, resLink.getStatusCode());
    assertNotEquals(resLink.getBody(), null);
    
    // create userB account
    resToken = accessApi.createAccount("test_case_token", resLink.getBody());
    assertEquals(HttpStatus.CREATED, resToken.getStatusCode());
    assertNotEquals(resToken.getBody(), null);
    Amigo emigoB = EmigoUtil.getObject(resToken.getBody().getAmigo());
    this.userBId = emigoB.getAmigoId();
    this.userBToken = LinkUtil.getTokenObject(resToken.getBody()).getToken();

    // assign account to service
    resUser = accessApi.assignAccount(this.appToken, resToken.getBody());
    assertEquals(HttpStatus.OK, resUser.getStatusCode());
    assertNotEquals(resUser.getBody(), null);
    this.userBAppToken = resUser.getBody().getServiceToken();

    // authorize account attach
    resLink = accessApi.authorizeAttach(this.appToken, this.userAId, access);
    assertEquals(HttpStatus.OK, resLink.getStatusCode());
    assertNotEquals(resLink.getBody(), null);

    // attach account
    resToken = accessApi.attachAccount(this.userAId, pass, resLink.getBody());
    assertEquals(HttpStatus.OK, resToken.getStatusCode());
    assertNotEquals(resToken.getBody(), null);
    this.userAToken = LinkUtil.getTokenObject(resToken.getBody()).getToken();

    // assign account to service
    resUser = accessApi.assignAccount(this.appToken, resToken.getBody());
    assertEquals(HttpStatus.OK, resUser.getStatusCode());
    assertNotEquals(resUser.getBody(), null);
    this.userAAppToken = resUser.getBody().getServiceToken();
  }

  private void phase2() {
    ResponseEntity<ConfigEntry> resConfig;
    ResponseEntity<PromptEntry> resPrompt;
    ResponseEntity<PromptEntry> resAnswer;
    ResponseEntity<List<PromptEntry>> resList;

    // dont automatically add contact reference
    Config configAdd = new Config();
    configAdd.setBoolValue(false);
    resConfig = accountApi.setConfig(this.userAToken, "auto_add_emigo", configAdd);
    assertEquals(HttpStatus.OK, resConfig.getStatusCode());
    assertNotEquals(resConfig.getBody(), null);

    // require prompt
    Config configPrompt = new Config();
    configPrompt.setBoolValue(true);
    resConfig = accountApi.setConfig(this.userAToken, "prompt_unknown", configPrompt);
    assertEquals(HttpStatus.OK, resConfig.getStatusCode());
    assertNotEquals(resConfig.getBody(), null);

    // add prompt entry
    PromptQuestion question = new PromptQuestion();
    question.setText("What is my middle name?");
    resPrompt = promptApi.addQuestion(this.userAToken, question);
    assertEquals(HttpStatus.CREATED, resPrompt.getStatusCode());
    assertNotEquals(resPrompt.getBody(), null);
    String promptId = resPrompt.getBody().getPromptId();

    // add possible answers
    resAnswer = promptApi.addQuestionAnswer(this.userAToken, promptId, "pierre");
    assertEquals(HttpStatus.CREATED, resAnswer.getStatusCode());
    assertNotEquals(resAnswer.getBody(), null);
    resAnswer = promptApi.addQuestionAnswer(this.userAToken, promptId, "Pierre");
    assertEquals(HttpStatus.CREATED, resAnswer.getStatusCode());
    assertNotEquals(resAnswer.getBody(), null);

    // retrieve prompts
    resList = promptApi.getQuestions(this.userAToken);
    assertEquals(HttpStatus.OK, resList.getStatusCode());
    assertEquals(resList.getBody().size(), 1);
    assertEquals(resList.getBody().get(0).getAnswers().size(), 2);

    // update config
    configService.refresh();
  }

  private void phase3() {
    ResponseEntity<LabelEntry> resLabel;
    ResponseEntity<AttributeEntry> resAttribute;
    ResponseEntity<AmigoMessage> resAmigo;
    ResponseEntity<AttributeView> resView;
    ResponseEntity<Integer> resRevision;
    ResponseEntity<Void> resVoid;

    // set name
    resAmigo = identityApi.updateName(this.userAToken, "test_name");
    assertEquals(HttpStatus.OK, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);

    // set handle
    resAmigo = identityApi.updateHandle(this.userAToken, "test_handle");
    assertEquals(HttpStatus.OK, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);

    // set image
    resAmigo = identityApi.updateImage(this.userAToken, "dGVzdF9pbWFnZQ==");
    assertEquals(HttpStatus.OK, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);

    // get revision
    resRevision = identityApi.getIdentityRevision(this.userAToken);
    assertEquals(HttpStatus.OK, resRevision.getStatusCode());
    assertNotEquals(resRevision.getBody(), null);
    Integer revision = resRevision.getBody();

    // retrieve message
    resAmigo = identityApi.getIdentityMessage(this.userAToken);
    assertEquals(HttpStatus.OK, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);
    this.userAMessage = resAmigo.getBody();

    // clear diry flag
    resVoid = identityApi.setDirtyFlag(this.userAToken, false, revision);
    assertEquals(HttpStatus.OK, resVoid.getStatusCode());

    // add a label
    resLabel = groupApi.addLabel(this.userAToken, "Colleagues");
    assertEquals(HttpStatus.CREATED, resLabel.getStatusCode());
    assertNotEquals(resLabel.getBody(), null);
    String labelId = resLabel.getBody().getLabelId();

    // add an attribute
    resAttribute = profileApi.addAttribute(this.userAToken, "da7084bf8a5187e049577d14030a8c76537e59830d224f6229548f765462c52b", "{ \"email\" : \"example@coredb.org\", \"category\" : \"Work\" }");
    assertEquals(HttpStatus.CREATED, resAttribute.getStatusCode());
    assertNotEquals(resAttribute.getBody(), null);
    String attributeId = resAttribute.getBody().getAttribute().getAttributeId();

    // associate attribute to label
    resAttribute = profileApi.setAttributeLabel(this.userAToken, attributeId, labelId);
    assertEquals(HttpStatus.OK, resAttribute.getStatusCode());
  }

private void phase4() {
    ResponseEntity<Void> resVoid;
    ResponseEntity<String> resText;
    ResponseEntity<AmigoEntry> resAmigo;
    ResponseEntity<ShareEntry> resShare;
    ResponseEntity<ShareMessage> resMessage;
    ResponseEntity<ShareStatus> resStatus;
    ResponseEntity<List<ShareEntry>> resShares;
    ResponseEntity<List<PendingAmigoView>> resPendings;
    ResponseEntity<PendingAmigo> resPending;
    ResponseEntity<AmigoEntry> resEntry;

    // add emigo reference
    resAmigo = indexApi.addAmigo(this.userBToken, this.userAMessage);
    assertEquals(HttpStatus.CREATED, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);

    // create new share entry
    resShare = shareApi.addConnection(this.userBToken, this.userAId);
    assertEquals(HttpStatus.CREATED, resShare.getStatusCode());
    assertNotEquals(resShare.getBody(), null);
    String shareId = resShare.getBody().getShareId();

    // retrieve share request
    resMessage = shareApi.getShareMessage(this.userBToken, shareId);
    assertEquals(HttpStatus.OK, resMessage.getStatusCode());
    assertNotEquals(resMessage.getBody(), null);
    ShareMessage requestMsg = resMessage.getBody();

    // deliver share request
    resStatus = shareApi.setShareMessage(this.userAId, requestMsg);
    assertEquals(HttpStatus.CREATED, resStatus.getStatusCode());
    assertNotEquals(resStatus.getBody(), null);
    ShareStatus status = resStatus.getBody();

    // answer question
    resStatus = shareApi.setAnswer(status.getPending().getToken(), "pierre");
    assertEquals(HttpStatus.OK, resStatus.getStatusCode());
    assertNotEquals(resStatus.getBody(), null);

    // get list of pending emigos
    resPendings = indexApi.getAmigoRequests(this.userAToken);
    assertEquals(HttpStatus.OK, resPendings.getStatusCode());
    assertNotEquals(resPendings.getBody(), null);
    assertEquals(resPendings.getBody().size(), 1);

    // get pending emigo
    resPending = indexApi.getPendingRequest(this.userAToken, resPendings.getBody().get(0).getShareId());
    assertEquals(HttpStatus.OK, resPending.getStatusCode());
    assertNotEquals(resPending.getBody(), null);
    PendingAmigo emigo = resPending.getBody();

    // accept emigo
    resAmigo = indexApi.addAmigo(this.userAToken, emigo.getMessage());
    assertEquals(HttpStatus.CREATED, resAmigo.getStatusCode());
    assertNotEquals(resAmigo.getBody(), null);

    // get list of entries
    resShares = shareApi.getConnections(this.userAToken);
    assertEquals(HttpStatus.OK, resShares.getStatusCode());
    assertNotEquals(resShares.getBody(), null);
    assertEquals(resShares.getBody().size(), 1);
    ShareEntry entry = resShares.getBody().get(0);

    // get emigo entry
    resEntry = indexApi.getAmigo(this.userAToken, entry.getAmigoId());
    assertEquals(HttpStatus.OK, resEntry.getStatusCode());
    assertNotEquals(resEntry.getBody(), null);

    // set notes
    resEntry = indexApi.updateAmigoNotes(this.userAToken, entry.getAmigoId(), "test notes");
    assertEquals(HttpStatus.OK, resEntry.getStatusCode());
    assertEquals(resEntry.getBody().getNotes(), "test notes");

    // update share status
    resShare = shareApi.updateConnection(this.userAToken, entry.getShareId(), "requesting", null);
    assertEquals(HttpStatus.OK, resShare.getStatusCode());
    assertNotEquals(resShare.getBody(), null);

    // retrieve share request
    resMessage = shareApi.getShareMessage(this.userAToken, entry.getShareId());
    assertEquals(HttpStatus.OK, resMessage.getStatusCode());
    assertNotEquals(resMessage.getBody(), null);
    ShareMessage acceptMsg = resMessage.getBody();

    // deliver share request-accept
    resStatus = shareApi.setShareMessage(this.userBId, acceptMsg);
    assertEquals(HttpStatus.CREATED, resStatus.getStatusCode());
    assertNotEquals(resStatus.getBody(), null);
    assertEquals(resStatus.getBody().getShareStatus().toString(), "connected");
    ShareStatus accepted = resStatus.getBody();

    // update share status
    resShare = shareApi.updateConnection(this.userAToken, entry.getShareId(), "connected", accepted.getConnected());
    assertEquals(HttpStatus.OK, resShare.getStatusCode());
    assertNotEquals(resShare.getBody(), null);
  }


  private void phase5() {
    ResponseEntity<List<ShareEntry>> resShares;
    ResponseEntity<Void> resVoid;
    ResponseEntity<Integer> resRevision;
    ResponseEntity<AuthMessage> resAuth;
    ResponseEntity<String> resToken;
    ResponseEntity<List<LabelEntry>> resLabels;
    ResponseEntity<List<AmigoEntry>> resAmigos;
    ResponseEntity<List<Attribute>> resAttributes;
    ResponseEntity<AmigoEntry> resAmigo;
    ResponseEntity<AmigoEntry> resEntry;
    ResponseEntity<AttributeEntry> resAttribute;
    ResponseEntity<List<Attribute>> resAttr;
    ResponseEntity<List<AttributeEntry>> resEntries;
    List<String> filter = new ArrayList<String>();
    filter.add("da7084bf8a5187e049577d14030a8c76537e59830d224f6229548f765462c52b");
    Attribute a;

    // retrieve access token
    resShares = shareApi.getConnections(this.userBToken);
    assertEquals(HttpStatus.OK, resShares.getStatusCode());
    assertNotEquals(resShares.getBody(), null);
    assertEquals(resShares.getBody().size(), 1);
    ShareEntry entry = resShares.getBody().get(0);
    assertEquals(entry.getStatus().toString(), "connected");

    // authorize agent access
    resAuth = agentApi.getAgentMessage(this.userBAppToken);
    assertEquals(HttpStatus.OK, resAuth.getStatusCode());
    assertNotEquals(resAuth.getBody(), null);
    AuthMessage auth = resAuth.getBody();

    // deliver authorization
    resToken = contactApi.addContactAgent(entry.getToken(), auth);
    assertEquals(HttpStatus.OK, resToken.getStatusCode());
    assertNotEquals(resToken.getBody(), null);
    String agent = resToken.getBody().replace("\"", "");

    // retrieve revision
    resRevision = contactApi.getContactRevision(entry.getToken(), agent);
    assertEquals(HttpStatus.OK, resRevision.getStatusCode());
    assertNotEquals(resRevision.getBody(), null);
    Integer r1 = resRevision.getBody();

    // retrieve attributes
    resAttr = contactApi.filterContactAttributes(entry.getToken(), agent, filter);
    assertEquals(HttpStatus.OK, resAttr.getStatusCode());
    assertNotEquals(resAttr.getBody(), null);
    assertEquals(resAttr.getBody().size(), 0);

    // get available labels
    resLabels = indexApi.getIndexLabels(this.userAToken);
    assertEquals(HttpStatus.OK, resLabels.getStatusCode());
    assertNotEquals(resLabels.getBody(), null);
    LabelEntry label = resLabels.getBody().get(0);

    // get avilable contacts
    resAmigos = indexApi.getAmigos(this.userAToken);
    assertEquals(HttpStatus.OK, resAmigos.getStatusCode());
    assertNotEquals(resAmigos.getBody(), null);
    AmigoEntry emigo = resAmigos.getBody().get(0);

    // assign label to emigo
    resEntry = indexApi.addAmigoLabel(this.userAToken, label.getLabelId(), emigo.getAmigoId());
    assertEquals(HttpStatus.CREATED, resEntry.getStatusCode());
    assertNotEquals(resEntry.getBody(), null);

    // retrieve revision
    resRevision = contactApi.getContactRevision(entry.getToken(), agent);
    assertEquals(HttpStatus.OK, resRevision.getStatusCode());
    assertNotEquals(resRevision.getBody(), null);
    Integer r2 = resRevision.getBody();
    assertNotEquals(r1, r2);

    // retrieve attributes
    resAttr = contactApi.filterContactAttributes(entry.getToken(), agent, filter);
    assertEquals(HttpStatus.OK, resAttr.getStatusCode());
    assertNotEquals(resAttr.getBody(), null);
    assertEquals(resAttr.getBody().size(), 1);
    a = resAttr.getBody().get(0);
    assertEquals(a.getData(), "{ \"email\" : \"example@coredb.org\", \"category\" : \"Work\" }");

    // get list of attributes
    resEntries = profileApi.filterAttributes(this.userAToken, null);
    assertEquals(HttpStatus.OK, resEntries.getStatusCode());
    assertNotEquals(resEntries.getBody(), null);
    Attribute attr = resEntries.getBody().get(0).getAttribute();

    // update attribute
    Attribute attribute = new Attribute();
    attribute.setSchema("da7084bf8a5187e049577d14030a8c76537e59830d224f6229548f765462c52b");
    attribute.setData("{ \"email\" : \"sample@coredb.org\", \"category\" : \"Work\" }");
    resAttribute = profileApi.setAttribute(this.userAToken, attribute.getSchema(), attr.getAttributeId(), attribute.getData());
    assertEquals(HttpStatus.OK, resAttribute.getStatusCode());

    // retrieve revision
    resRevision = contactApi.getContactRevision(entry.getToken(), agent);
    assertEquals(HttpStatus.OK, resRevision.getStatusCode());
    assertNotEquals(resRevision.getBody(), null);
    Integer r3 = resRevision.getBody();
    assertNotEquals(r2, r3);

    // retrieve attributes
    resAttr = contactApi.filterContactAttributes(entry.getToken(), agent, filter);
    assertEquals(HttpStatus.OK, resAttr.getStatusCode());
    assertNotEquals(resAttr.getBody(), null);
    assertEquals(resAttr.getBody().size(), 1);
    a = resAttr.getBody().get(0);
    assertEquals(a.getData(), "{ \"email\" : \"sample@coredb.org\", \"category\" : \"Work\" }");
  }

  void phase6() {
    ResponseEntity<ShareEntry> resShare;
    ResponseEntity<ShareMessage> resMessage;
    ResponseEntity<ShareStatus> resStatus;
    ResponseEntity<List<ShareEntry>> resShares;
    ResponseEntity<Void> resVoid;
    ShareEntry entry;
    ShareMessage msg;

    // get list of entries
    resShares = shareApi.getConnections(this.userAToken);
    assertEquals(HttpStatus.OK, resShares.getStatusCode());
    assertNotEquals(resShares.getBody(), null);
    assertEquals(resShares.getBody().size(), 1);
    entry = resShares.getBody().get(0);

    // update share status
    resShare = shareApi.updateConnection(this.userAToken, entry.getShareId(), "closing", null);
    assertEquals(HttpStatus.OK, resShare.getStatusCode());
    assertNotEquals(resShare.getBody(), null);

    // retrieve share request
    resMessage = shareApi.getShareMessage(this.userAToken, entry.getShareId());
    assertEquals(HttpStatus.OK, resMessage.getStatusCode());
    assertNotEquals(resMessage.getBody(), null);
    msg = resMessage.getBody();

    // deliver share request-accept
    resStatus = shareApi.setShareMessage(this.userBId, msg);
    assertEquals(HttpStatus.CREATED, resStatus.getStatusCode());
    assertNotEquals(resStatus.getBody(), null);
    assertEquals(resStatus.getBody().getShareStatus().toString(), "closed");

    // remove share connection
    resVoid = shareApi.removeConnection(this.userAToken, entry.getShareId());
    assertEquals(HttpStatus.OK, resShare.getStatusCode());

    // remove index entry
    resVoid = indexApi.removeAmigo(this.userAToken, entry.getAmigoId());
    assertEquals(HttpStatus.OK, resVoid.getStatusCode());

    // block future requests
    resVoid = indexApi.addAmigoReject(this.userAToken, entry.getAmigoId());
    assertEquals(HttpStatus.OK, resVoid.getStatusCode());

    // get list of entries
    resShares = shareApi.getConnections(this.userBToken);
    assertEquals(HttpStatus.OK, resShares.getStatusCode());
    assertNotEquals(resShares.getBody(), null);
    assertEquals(resShares.getBody().size(), 1);
    entry = resShares.getBody().get(0);

    // try and reopen
    resShare = shareApi.updateConnection(this.userBToken, entry.getShareId(), "requesting", null);
    assertEquals(HttpStatus.OK, resShare.getStatusCode());
    assertNotEquals(resShare.getBody(), null);

    // retrieve share request
    resMessage = shareApi.getShareMessage(this.userBToken, entry.getShareId());
    assertEquals(HttpStatus.OK, resMessage.getStatusCode());
    assertNotEquals(resMessage.getBody(), null);
    msg = resMessage.getBody();

    // deliver share request-accept
    resStatus = shareApi.setShareMessage(this.userAId, msg);
    assertEquals(HttpStatus.CREATED, resStatus.getStatusCode());
    assertNotEquals(resStatus.getBody(), null);
    assertEquals(resStatus.getBody().getShareStatus().toString(), "received");

    // get list of entries
    resShares = shareApi.getConnections(this.userAToken);
    assertEquals(HttpStatus.OK, resShares.getStatusCode());
    assertNotEquals(resShares.getBody(), null);
    assertEquals(resShares.getBody().size(), 0);
  }

}

