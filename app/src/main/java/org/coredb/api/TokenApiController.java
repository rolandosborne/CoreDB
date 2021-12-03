package org.coredb.api;

import org.coredb.model.Auth;
import org.coredb.model.AuthMessage;
import org.coredb.model.Revisions;
import org.coredb.model.ServiceAccess;
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

import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.service.AuthService;
import org.coredb.service.AgentService;
import java.security.InvalidParameterException;
import java.nio.file.AccessDeniedException;
import javax.ws.rs.NotAcceptableException;
import java.lang.UnsupportedOperationException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class TokenApiController implements TokenApi {

    private static final Logger log = LoggerFactory.getLogger(TokenApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private AgentService agentService;

    @org.springframework.beans.factory.annotation.Autowired
    public TokenApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ServiceAccess> getAccess(@NotNull @Parameter(in = ParameterIn.QUERY, description = "service access token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        AccountApp app = authService.identityService(token);
        return new ResponseEntity<ServiceAccess>(app.getAccountAccess(), HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceAccess>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceAccess>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceAccess>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Auth> setAgentMessage(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token with access" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.DEFAULT, description = "new share request", schema=@Schema()) @Valid @RequestBody AuthMessage body) {
      try {
        Auth auth = agentService.setAppAuth(token, body);
        return new ResponseEntity<Auth>(auth, HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Auth>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<Auth>(HttpStatus.BAD_REQUEST);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Auth>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Auth>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Revisions> getRevisions(@NotNull @Parameter(in = ParameterIn.QUERY, description = "service access token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message if from contact" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Revisions revisions = new Revisions();
        if(agent != null) {
          AccountShare share = authService.getAccountShare(token);
          agentService.checkAppToken(share.getEmigo(), agent);
          revisions.setContactRevision(share.getAccount().getContactRevision());
          revisions.setViewRevision(share.getAccount().getViewRevision());
          revisions.setListingRevision(share.getAccount().getIdentityRevision());
        }
        else {
          AccountApp app = authService.getAccountApp(token);
          if(app.getAuthorizedIndex()) {
            revisions.setIndexRevision(app.getAccount().getIndexRevision());
          }
          if(app.getAuthorizedShare()) {
            revisions.setShareRevision(app.getAccount().getShareRevision());
          }
          if(app.getAuthorizedPrompt()) {
            revisions.setPromptRevision(app.getAccount().getPromptRevision());
          }
          if(app.getAuthorizedProfile()) {
            revisions.setProfileRevision(app.getAccount().getProfileRevision());
          }
          if(app.getAuthorizedUser()) {
            revisions.setUserRevision(app.getAccount().getUserRevision());
          }
          if(app.getAuthorizedIdentity()) {
            revisions.setIdentityRevision(app.getAccount().getIdentityRevision());
          }
          if(app.getAuthorizedGroup()) {
            revisions.setGroupRevision(app.getAccount().getGroupRevision());
          }
          if(app.getAuthorizedShow()) {
            revisions.setShowRevision(app.getAccount().getShowRevision());
          }
          if(app.getAuthorizedApp()) {
            revisions.setServiceRevision(app.getAccount().getServiceRevision());
          }
          if(app.getAuthorizedConversation()) {
            revisions.setDialogueRevision(app.getAccount().getDialogueRevision());
            revisions.setInsightRevision(app.getAccount().getInsightRevision());
          }
        }
        return new ResponseEntity<Revisions>(revisions, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Revisions>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Revisions>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(UnsupportedOperationException e) {
        return new ResponseEntity<Revisions>(HttpStatus.PAYMENT_REQUIRED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Revisions>(HttpStatus.INTERNAL_SERVER_ERROR);
      }

    }

}


