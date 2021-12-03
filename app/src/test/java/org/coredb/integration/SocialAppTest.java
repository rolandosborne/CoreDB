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
public class SocialAppTest {

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
    String a = addPerson("PERSON_A");
    String b = addPerson("PERSON_B"); 
    String label = connect(a, b); 

    ResponseEntity<StoreStatus> before = showApi.getShowStatus(a);
    assertEquals(HttpStatus.OK, before.getStatusCode());
    assertNotEquals(before.getBody(), null);
    assertTrue(before.getBody().getUsed() == (long)0);

    addSubject(a, "img1", label);
    addSubject(a, "img2", label);
    String id = addSubject(a, "img3", label);
    addSubject(a, "img4", label);

    // create unassigned subject
    ResponseEntity<LabelEntry> aLabel = groupApi.addLabel(a, "Rando");
    assertEquals(HttpStatus.CREATED, aLabel.getStatusCode());
    assertNotEquals(aLabel.getBody(), null);
    addSubject(a, "img6", aLabel.getBody().getLabelId());
  
    ResponseEntity<StoreStatus> during = showApi.getShowStatus(a);
    assertEquals(HttpStatus.OK, during.getStatusCode());
    assertNotEquals(during.getBody(), null);
    assertTrue(during.getBody().getUsed() == (long)20);
 
    List<AccountSubject> subjects = subjectRepository.findAll();
    for(AccountSubject subject: subjects) {
      for(AccountSubjectAsset asset: subject.getSubjectAssets()) {
        asset.setStatus("ready");
        asset.setAssetSize((long)1025);
        asset.setPending(null);
        assetRepository.save(asset);
      }
    }
    subjectRepository.save(subjects);

    ResponseEntity<StoreStatus> after = showApi.getShowStatus(a);
    assertEquals(HttpStatus.OK, after.getStatusCode());
    assertNotEquals(after.getBody(), null);
    assertTrue(after.getBody().getUsed() == (long)15395);

    // agent message
    ResponseEntity<AuthMessage> auth = agentApi.getAgentMessage(b);
    assertEquals(HttpStatus.OK, auth.getStatusCode());
    assertNotEquals(auth.getBody(), null);

    // set auth message
    ResponseEntity<List<ShareEntry>> shares = shareApi.getConnections(b);
    assertEquals(HttpStatus.OK, shares.getStatusCode());
    assertNotEquals(shares.getBody(), null);
    assertEquals(shares.getBody().size(), 1);
    String token = shares.getBody().get(0).getToken();

    // view api
    ResponseEntity<String> agent = viewApi.addViewAgent(token, auth.getBody());
    assertEquals(HttpStatus.OK, agent.getStatusCode());
    assertNotEquals(agent.getBody(), null);
    String agentToken = agent.getBody().replace("\"", "");

    // peek at subjects
    List<String> schemas = new ArrayList<String>();
    schemas.add("bfa67db321b9a3121dc8f5f47961f2bbee97732cfc1d792832b316ad0c165344");
    ResponseEntity<List<Subject>> peek = viewApi.filterViewSubjects(token, agentToken, schemas);
    assertEquals(HttpStatus.OK, peek.getStatusCode());
    assertNotEquals(peek.getBody(), null);
    assertNotEquals(peek.getBody().size(), 0);

    // retrieve subject
    ResponseEntity<Subject> subject = viewApi.getViewSubject(token, peek.getBody().get(0).getSubjectId(), agentToken);
    assertEquals(HttpStatus.OK, subject.getStatusCode());
    assertNotEquals(subject.getBody(), null);

    // extract assets
    Boolean set = false;
    JSONObject obj = new JSONObject(subject.getBody().getData());
    JSONArray data = obj.getJSONArray("assets");
    for (int i = 0; i < data.length(); i++) {
      String assetId = data.getJSONObject(i).getString("assetId");
      String transformCode = data.getJSONObject(i).getString("transformCode");
      if(transformCode.equals("P01")) {
        // wont download because transcode was pretend
        set = true;
      }
    }
    assertEquals(set, true);
  }

  private String addSubject(String token, String name, String label) {

    // add subjects
    ResponseEntity<SubjectEntry> subject = showApi.addSubject(token, null);
    assertEquals(HttpStatus.OK, subject.getStatusCode());
    assertNotEquals(subject.getBody(), null);

    // load image
    List<String> transforms = new ArrayList<String>();
    transforms.add("P01");
    transforms.add("P02");
    transforms.add("P03");
    MultipartFile file1 = new MockMultipartFile("file", name + ".jpg", "application/octent_stream", name.getBytes());
    ResponseEntity<Asset> entry = showApi.addSubjectAsset(file1, token, transforms, subject.getBody().getSubject().getSubjectId());
    assertEquals(HttpStatus.OK, entry.getStatusCode());
    assertNotEquals(entry.getBody(), null);

    // construct subject view
    String data = "{ \"assets\": [ ";
    Boolean set = false;
    for(SubjectAsset asset: entry.getBody().getAssets()) {
      if(set) {
        data += ", ";
      }
      data += "{ \"assetId\": \"" + asset.getAssetId() + "\", \"transformCode\": \"" + asset.getTransform() + "\" }"; 
      set = true;
    }
    data += " ] }";

    // save view
    ResponseEntity<SubjectEntry> updated = showApi.updateSubjectData(token, "bfa67db321b9a3121dc8f5f47961f2bbee97732cfc1d792832b316ad0c165344", subject.getBody().getSubject().getSubjectId(), data);
    assertEquals(HttpStatus.OK, updated.getStatusCode());
    assertNotEquals(updated.getBody(), null);

    // add subject label
    ResponseEntity<SubjectEntry> tagged = showApi.addSubjectLabel(token, subject.getBody().getSubject().getSubjectId(), label);
    assertEquals(HttpStatus.OK, tagged.getStatusCode());
    assertNotEquals(tagged.getBody(), null);

    // set viewable
    ResponseEntity<SubjectEntry> ready = showApi.updateSubjectAccess(token, subject.getBody().getSubject().getSubjectId(), true);
    assertEquals(HttpStatus.OK, ready.getStatusCode());
    assertNotEquals(ready.getBody(), null);

    return subject.getBody().getSubject().getSubjectId();
  }

  private String connect(String a, String b) throws Exception {

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
   
    // create labels
    ResponseEntity<LabelEntry> aLabel = groupApi.addLabel(a, "Colleagues");
    assertEquals(HttpStatus.CREATED, aLabel.getStatusCode());
    assertNotEquals(aLabel.getBody(), null);
    ResponseEntity<LabelEntry> bLabel = groupApi.addLabel(b, "Colleagues");
    assertEquals(HttpStatus.CREATED, bLabel.getStatusCode());
    assertNotEquals(bLabel.getBody(), null);

    // assign emigo to labels
    ResponseEntity<AmigoEntry> aAssign = indexApi.addAmigoLabel(a, aLabel.getBody().getLabelId(), bAmigo.getBody().getAmigoId());
    assertEquals(HttpStatus.CREATED, aAssign.getStatusCode());
    assertNotEquals(aAssign.getBody(), null);
    ResponseEntity<AmigoEntry> bAssign = indexApi.addAmigoLabel(b, bLabel.getBody().getLabelId(), aAmigo.getBody().getAmigoId());
    assertEquals(HttpStatus.CREATED, bAssign.getStatusCode());
    assertNotEquals(bAssign.getBody(), null);

    return aLabel.getBody().getLabelId();
  }

  private String addPerson(String handle) throws Exception {
    ResponseEntity<AmigoToken> token = accessApi.createAmigo("test_case_token");
    assertEquals(HttpStatus.CREATED, token.getStatusCode());
    assertNotEquals(token.getBody(), null);
    String access = LinkUtil.getTokenObject(token.getBody()).getToken();

    ResponseEntity<AmigoMessage> emigo = identityApi.updateHandle(access, handle);
    assertEquals(HttpStatus.OK, emigo.getStatusCode());
    assertNotEquals(emigo.getBody(), null);

    return access;
  }

}

