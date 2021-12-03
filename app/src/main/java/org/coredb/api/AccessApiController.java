package org.coredb.api;

import org.coredb.model.AmigoToken;
import org.coredb.model.LinkMessage;
import org.coredb.model.Pass;
import org.coredb.model.ServiceAccess;
import org.coredb.model.UserEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.lang.IllegalStateException;

import org.coredb.jpa.entity.AccountApp;

import org.coredb.service.AccessService;
import org.coredb.service.AuthService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AccessApiController implements AccessApi {

    private static final Logger log = LoggerFactory.getLogger(AccessApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AccessService accessService;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccessApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<UserEntry> assignAccount(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,@ApiParam(value = ""  )  @Valid @RequestBody AmigoToken body) {
      try {
        AccountApp app = authService.accessService(token);
        UserEntry entry = accessService.serviceToken(app.getAccount(), body);
        return new ResponseEntity<UserEntry>(entry, HttpStatus.OK);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.LOCKED); //423
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.METHOD_NOT_ALLOWED); //405
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.BAD_REQUEST); // 400
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<UserEntry>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<AmigoToken> attachAccount(@NotNull @ApiParam(value = "id of referenced account", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId,@NotNull @ApiParam(value = "pass token to attaching services", required = true) @Valid @RequestParam(value = "pass", required = true) String pass,@ApiParam(value = ""  )  @Valid @RequestBody LinkMessage body) {
      try {
        AmigoToken et = accessService.accountAttach(amigoId, pass, body);
        return new ResponseEntity<AmigoToken>(et, HttpStatus.OK);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.LOCKED); //423
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.METHOD_NOT_ALLOWED); //405
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<LinkMessage> authorizeAttach(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,@NotNull @ApiParam(value = "id of account to attach", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body) {
      try {
        AccountApp app = authService.accessService(token);
        LinkMessage msg = accessService.serviceAttach(app.getAccount(), amigoId, body);
        return new ResponseEntity<LinkMessage>(msg, HttpStatus.OK);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.LOCKED); //423
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.METHOD_NOT_ALLOWED); //405
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<LinkMessage> authorizeCreate(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body) {
      try {
        AccountApp app = authService.accessService(token);
        LinkMessage msg = accessService.prepareAccountCreate(app.getAccount(), body);
        return new ResponseEntity<LinkMessage>(msg, HttpStatus.OK);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.LOCKED); //423
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.METHOD_NOT_ALLOWED); //405
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<LinkMessage>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<AmigoToken> createAccount(@NotNull @ApiParam(value = "admin token", required = true) @Valid @RequestParam(value = "token", required = true) String token,@ApiParam(value = ""  )  @Valid @RequestBody LinkMessage body) {
      try {
        authService.accessToken(token);
        AmigoToken amigo = accessService.accountCreate(body);
        return new ResponseEntity<AmigoToken>(amigo, HttpStatus.CREATED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.BAD_REQUEST); //400
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<AmigoToken> createAmigo(@NotNull @ApiParam(value = "admin token", required = true) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        authService.accessToken(token);
        AmigoToken et = accessService.addService();
        return new ResponseEntity<AmigoToken>(et, HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.BAD_REQUEST); //400
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

    public ResponseEntity<Pass> generatePass(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "expiration seconds for pass") @Valid @RequestParam(value = "expire", required = false) Integer expire
,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body
) {
      try {
        AccountApp app = authService.accessService(token);
        Pass pass = accessService.generatePass(app.getAccount(), expire, body);
        return new ResponseEntity<Pass>(pass, HttpStatus.OK);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Pass>(HttpStatus.LOCKED); //423
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Pass>(HttpStatus.METHOD_NOT_ALLOWED); //405
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<Pass>(HttpStatus.NOT_ACCEPTABLE); //406
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Pass>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Pass>(HttpStatus.INTERNAL_SERVER_ERROR); //500
      }
    }

}

