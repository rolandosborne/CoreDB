package org.coredb.api;

import org.coredb.model.Blurb;
import org.coredb.model.Dialogue;
import org.coredb.model.DialogueView;
import org.coredb.model.Insight;
import org.coredb.model.InsightView;
import org.coredb.model.Topic;
import org.coredb.model.TopicView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import java.security.InvalidParameterException;

import org.coredb.jpa.entity.AccountApp;

import org.coredb.service.AuthService;
import org.coredb.service.ConversationService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ConversationApiController implements ConversationApi {

    private static final Logger log = LoggerFactory.getLogger(ConversationApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private ConversationService conversationService;

    @org.springframework.beans.factory.annotation.Autowired
    public ConversationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Blurb> addConversationBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "schema of blurb" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "schema", required = true) String schema,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.DEFAULT, description = "blurb data", schema=@Schema()) @Valid @RequestBody String body) {
     try {
        AccountApp app = authService.conversationService(token);
        Blurb blurb = conversationService.addBlurb(app.getAccount(), null, dialogueId, schema, body);
        return new ResponseEntity<Blurb>(blurb, HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.TOO_MANY_REQUESTS); //429 too many blurbs
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Dialogue> addConversationDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "amigo to converse with" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = true) String amigoId) {
      try {
        AccountApp app = authService.conversationService(token);
        Dialogue dialogue = conversationService.addDialogue(app.getAccount(), amigoId);
        return new ResponseEntity<Dialogue>(dialogue, HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_FOUND); //404 amigo not found
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.TOO_MANY_REQUESTS); //429 too many dialogues
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<DialogueView>> getConversationDialogueViews(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        AccountApp app = authService.conversationService(token);
        List<DialogueView> views = conversationService.getDialogueViews(app.getAccount());
        return new ResponseEntity<List<DialogueView>>(views, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<DialogueView>>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<DialogueView>>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<DialogueView>>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<DialogueView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Dialogue> getConversationDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId) {
      try {
        AccountApp app = authService.conversationService(token);
        Dialogue dialogue = conversationService.getDialogue(app.getAccount(), null, dialogueId);
        return new ResponseEntity<Dialogue>(dialogue, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_ACCEPTABLE); //406 disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_FOUND); //404 insight not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Insight> getConversationInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @NotNull @Parameter(in = ParameterIn.QUERY, description = "id of amigo" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = true) String amigoId, @Parameter(in = ParameterIn.PATH, description = "id of insight", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId) {
      try {
        AccountApp app = authService.conversationService(token);
        Insight insight = conversationService.getInsight(app.getAccount(), amigoId, dialogueId);    
        return new ResponseEntity<Insight>(insight, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_ACCEPTABLE); //406 disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_FOUND); //404 insight not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<InsightView>> getConversationInsightViews(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        AccountApp app = authService.conversationService(token);
        List<InsightView> views = conversationService.getInsightViews(app.getAccount());
        return new ResponseEntity<List<InsightView>>(views, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<InsightView>>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<InsightView>>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<InsightView>>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<InsightView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Topic> getConversationTopic(@Parameter(in = ParameterIn.PATH, description = "id of specified dialog", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId, @Parameter(in = ParameterIn.PATH, description = "id of specified topic", required=true, schema=@Schema()) @PathVariable("topicId") String topicId, @NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @Parameter(in = ParameterIn.QUERY, description = "only retrieve specified schema" ,schema=@Schema()) @Valid @RequestParam(value = "schema", required = false) String schema) {
      try {
        AccountApp app = authService.conversationService(token);
        Topic topic = conversationService.getTopic(app.getAccount(), null, dialogueId, topicId, schema);
        return new ResponseEntity<Topic>(topic, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.NOT_ACCEPTABLE); //406 ac or topiccount disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.NOT_FOUND); //404 dialogue or topic not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<TopicView>> getConversationTopicViews(@Parameter(in = ParameterIn.PATH, description = "id of specified dialog", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        AccountApp app = authService.conversationService(token);
        List<TopicView> views = conversationService.getTopicViews(app.getAccount(), null, dialogueId);
        return new ResponseEntity<List<TopicView>>(views, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeConversationBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.PATH, description = "id of blurb", required=true, schema=@Schema()) @PathVariable("blurbId") String blurbId) {
      try {
        AccountApp app = authService.conversationService(token);
        conversationService.clearBlurb(app.getAccount(), null, dialogueId, blurbId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeConversationDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId) {
      try {
        AccountApp app = authService.conversationService(token);
        conversationService.removeDialogue(app.getAccount(), dialogueId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE); //406 disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //404 insight not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeConversationInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @NotNull @Parameter(in = ParameterIn.QUERY, description = "id of contact" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = true) String amigoId, @Parameter(in = ParameterIn.PATH, description = "id of insight", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId) {
      try {
        AccountApp app = authService.conversationService(token);
        conversationService.removeInsight(app.getAccount(), amigoId, dialogueId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE); //406 disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //404 insight not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Blurb> setConversationBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "schema of blurb" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "schema", required = true) String schema,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.PATH, description = "id of blurb", required=true, schema=@Schema()) @PathVariable("blurbId") String blurbId,@Parameter(in = ParameterIn.DEFAULT, description = "blurb data", schema=@Schema()) @Valid @RequestBody String body) {
      try {
        AccountApp app = authService.conversationService(token);
        Blurb blurb = conversationService.setBlurb(app.getAccount(), app.getAccount().getEmigoId(), dialogueId, blurbId, schema, body);
        return new ResponseEntity<Blurb>(blurb, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Dialogue> setConversationDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "if insight has been created" ,schema=@Schema()) @Valid @RequestParam(value = "linked", required = false) Boolean linked,@Parameter(in = ParameterIn.QUERY, description = "if insight has been synced" ,schema=@Schema()) @Valid @RequestParam(value = "synced", required = false) Boolean synced,@Parameter(in = ParameterIn.QUERY, description = "if conversation can recived comments" ,schema=@Schema()) @Valid @RequestParam(value = "active", required = false) Boolean active,@Parameter(in = ParameterIn.QUERY, description = "apply if version matches" ,schema=@Schema()) @Valid @RequestParam(value = "revision", required = false) Integer revision) {
      try {
        AccountApp app = authService.conversationService(token);
        Dialogue dialogue = conversationService.setDialogue(app.getAccount(), null, dialogueId, linked, synced, active, revision);
        return new ResponseEntity<Dialogue>(dialogue, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.UNAUTHORIZED); //401 token not authorized
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Insight> updateConversationInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "id of contact" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = true) String amigoId,@Parameter(in = ParameterIn.PATH, description = "id of insight", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "revision of linked dialogue" ,schema=@Schema()) @Valid @RequestParam(value = "revision", required = false) Integer revision) {
      try {
        AccountApp app = authService.conversationService(token);
        Insight insight = conversationService.setInsight(app.getAccount(), amigoId, dialogueId, revision);
        return new ResponseEntity<Insight>(insight, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.BAD_REQUEST); //400 unknown token
      }
      catch(NotAcceptableException e) {
       log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
       log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.UNAUTHORIZED); //401 token not authorized
     }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

