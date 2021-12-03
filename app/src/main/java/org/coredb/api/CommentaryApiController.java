package org.coredb.api;

import org.coredb.model.Blurb;
import org.coredb.model.Dialogue;
import org.coredb.model.Insight;
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

import org.coredb.service.CommentaryService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class CommentaryApiController implements CommentaryApi {

    private static final Logger log = LoggerFactory.getLogger(CommentaryApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private CommentaryService commentaryService;

    @org.springframework.beans.factory.annotation.Autowired
    public CommentaryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Blurb> addCommentaryBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "schema of blurb" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "schema", required = true) String schema,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent,@Parameter(in = ParameterIn.DEFAULT, description = "blurb data", schema=@Schema()) @Valid @RequestBody String body) {
      try {
        Blurb blurb = commentaryService.addBlurb(token, schema, dialogueId, agent, body);
        return new ResponseEntity<Blurb>(blurb, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.METHOD_NOT_ALLOWED); //405 invalid token or amigoId
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.TOO_MANY_REQUESTS); //429 too many blurbs
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Insight> addCommentaryInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.QUERY, description = "id for new conversation" ,schema=@Schema()) @Valid @RequestParam(value = "dialogueId", required = false) String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "revision of conversation" ,schema=@Schema()) @Valid @RequestParam(value = "revision", required = false) Integer revision,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Insight insight = commentaryService.addInsight(token, agent, dialogueId, revision);
        return new ResponseEntity<Insight>(insight, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.TOO_MANY_REQUESTS); //429 too many insights
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Dialogue> getCommentaryDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Dialogue dialogue = commentaryService.getDialogue(token, agent, dialogueId);
        return new ResponseEntity<Dialogue>(dialogue, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.METHOD_NOT_ALLOWED); //405 access not allowed
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Topic> getCommentaryTopic(@Parameter(in = ParameterIn.PATH, description = "id of specified dialog", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId, @Parameter(in = ParameterIn.PATH, description = "id of specified topic", required=true, schema=@Schema()) @PathVariable("topicId") String topicId, @NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @Parameter(in = ParameterIn.QUERY, description = "only retrieve specified schema" ,schema=@Schema()) @Valid @RequestParam(value = "schema", required = false) String schema, @Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Topic topic = commentaryService.getTopic(token, agent, dialogueId, topicId, schema);
        return new ResponseEntity<Topic>(topic, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.METHOD_NOT_ALLOWED); //405 access not allowed
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Topic>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<TopicView>> getCommentaryTopicViews(@Parameter(in = ParameterIn.PATH, description = "id of specified dialog", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        List<TopicView> views = commentaryService.getTopicViews(token, agent, dialogueId);
        return new ResponseEntity<List<TopicView>>(views, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.METHOD_NOT_ALLOWED); //405 access not allowed
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<TopicView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeCommentaryBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.PATH, description = "id of blurb", required=true, schema=@Schema()) @PathVariable("blurbId") String blurbId,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        commentaryService.removeBlurb(token, dialogueId, blurbId, agent);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeCommentaryInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue to remove", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        commentaryService.removeInsight(token, agent, dialogueId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Blurb> setCommentaryBlurb(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "schema of blurb" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "schema", required = true) String schema,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@Parameter(in = ParameterIn.PATH, description = "id of blurb", required=true, schema=@Schema()) @PathVariable("blurbId") String blurbId,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent,@Parameter(in = ParameterIn.DEFAULT, description = "blurb data", schema=@Schema()) @Valid @RequestBody String body) {
      try {
        Blurb blurb = commentaryService.updateBlurb(token, schema, dialogueId, blurbId, agent, body);
        return new ResponseEntity<Blurb>(blurb, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Blurb>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Dialogue> updateCommentaryDialogue(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "agent", required = true) String agent,@Parameter(in = ParameterIn.PATH, description = "id of dialogue", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId, @Parameter(in = ParameterIn.QUERY, description = "if still active" ,schema=@Schema()) @Valid @RequestParam(value = "active", required = false) Boolean active, @Parameter(in = ParameterIn.QUERY, description = "if insight has been synced" ,schema=@Schema()) @Valid @RequestParam(value = "synced", required = false) Boolean synced,@Parameter(in = ParameterIn.QUERY, description = "apply if version matches" ,schema=@Schema()) @Valid @RequestParam(value = "revision", required = false) Integer revision) {
      try {
        Dialogue dialogue = commentaryService.updateDialogue(token, agent, dialogueId, active, synced, revision);
        return new ResponseEntity<Dialogue>(dialogue, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.NOT_FOUND); //404 dialogue not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Dialogue>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Insight> updateCommentaryInsight(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token for service" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.PATH, description = "id of dialogue to remove", required=true, schema=@Schema()) @PathVariable("dialogueId") String dialogueId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "agent", required = true) String agent,@Parameter(in = ParameterIn.QUERY, description = "revision of remote dialog" ,schema=@Schema()) @Valid @RequestParam(value = "revision", required = false) Integer revision) {
      try {
        Insight insight = commentaryService.updateInsight(token, agent, dialogueId, revision);
        return new ResponseEntity<Insight>(insight, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.METHOD_NOT_ALLOWED); //405 share token not found
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_ACCEPTABLE); //406 account disabled
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.NOT_FOUND); //404 insight not found
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.PAYMENT_REQUIRED); //402 share not connected
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Insight>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  
}

