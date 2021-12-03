package org.coredb.api;

import org.coredb.model.PromptEntry;
import org.coredb.model.PromptQuestion;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;

import org.coredb.service.AuthService;
import org.coredb.service.PromptService;
import org.coredb.jpa.entity.AccountApp;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class PromptApiController implements PromptApi {

    private static final Logger log = LoggerFactory.getLogger(PromptApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private PromptService promptService;

    @org.springframework.beans.factory.annotation.Autowired
    public PromptApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<PromptEntry> addQuestion(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "new question to be appended" ,required=true )  @Valid @RequestBody PromptQuestion body
) {
      try {
        AccountApp app = authService.promptService(token);
        PromptEntry prompt = promptService.addQuestion(app.getAccount(), body);
        return new ResponseEntity<PromptEntry>(prompt, HttpStatus.CREATED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<PromptEntry> addQuestionAnswer(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of associated question",required=true) @PathVariable("promptId") String promptId
,@ApiParam(value = "new answer to be appended" ,required=true )  @Valid @RequestBody String body
) {
      try {
        AccountApp app = authService.promptService(token);
        PromptEntry entry = promptService.addAnswer(app.getAccount(), promptId, body);
        return new ResponseEntity<PromptEntry>(entry, HttpStatus.CREATED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_FOUND);
     }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<PromptEntry> deleteQuestionAnswer(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of question entry",required=true) @PathVariable("promptId") String promptId
,@ApiParam(value = "id of answer entry",required=true) @PathVariable("answerId") String answerId
) {
      try {
        AccountApp app = authService.promptService(token);
        promptService.deleteAnswer(app.getAccount(), promptId, answerId);
        return new ResponseEntity<PromptEntry>(HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_FOUND);
     }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getPromptRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.promptService(token);
        Integer rev = app.getAccount().getPromptRevision();
        return new ResponseEntity<Integer>(rev, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<PromptEntry>> getQuestions(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.promptService(token);
        List<PromptEntry> prompts = promptService.getQuestions(app.getAccount());
        return new ResponseEntity<List<PromptEntry>>(prompts, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<PromptEntry>>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<PromptEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<PromptEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<PromptEntry>>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<PromptEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeQuestion(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of entry to delete",required=true) @PathVariable("promptId") String promptId
) {
      try {
        AccountApp app = authService.promptService(token);
        promptService.deleteQuestion(app.getAccount(), promptId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
     }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<PromptEntry> updateQuestion(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of prompt entry",required=true) @PathVariable("promptId") String promptId
,@ApiParam(value = "new request to be appended" ,required=true )  @Valid @RequestBody PromptQuestion body
) {
      try {
        AccountApp app = authService.promptService(token);
        PromptEntry prompt = promptService.updateQuestion(app.getAccount(), promptId, body);
        return new ResponseEntity<PromptEntry>(prompt, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.NOT_FOUND);
     }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<PromptEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

