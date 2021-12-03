package org.coredb.api;

import org.coredb.model.Attribute;
import org.coredb.model.AttributeView;
import org.coredb.model.AuthMessage;
import org.coredb.model.Amigo;
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

import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import javax.ws.rs.ForbiddenException;
import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;
import java.security.InvalidParameterException;

import org.coredb.jpa.entity.AccountApp;
import org.coredb.service.ContactService;
import org.coredb.service.AuthService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class ContactApiController implements ContactApi {

    private static final Logger log = LoggerFactory.getLogger(ContactApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private ContactService contactService;

    @org.springframework.beans.factory.annotation.Autowired
    public ContactApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> addContactAgent(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "updated access rights" ,required=true )  @Valid @RequestBody AuthMessage body
) {
      try {
        String auth = contactService.addAgent(token, body);
        return new ResponseEntity<String>(auth, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<Attribute>> filterContactAttributes(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        List<Attribute> attributes = contactService.filterAttributes(token, body, agent);
        return new ResponseEntity<List<Attribute>>(attributes, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        return new ResponseEntity<List<Attribute>>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(UnsupportedOperationException e) {
        return new ResponseEntity<List<Attribute>>(HttpStatus.PAYMENT_REQUIRED); //402
      }
      catch(Exception e) {
        return new ResponseEntity<List<Attribute>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }


    public ResponseEntity<List<AttributeView>> filterContactAttributeViews(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        List<AttributeView> attributes = contactService.filterAttributeViews(token, body, agent);
        return new ResponseEntity<List<AttributeView>>(attributes, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        return new ResponseEntity<List<AttributeView>>(HttpStatus.UNAUTHORIZED); //401
      }
      catch(UnsupportedOperationException e) {
        return new ResponseEntity<List<AttributeView>>(HttpStatus.PAYMENT_REQUIRED); //402
      }
      catch(Exception e) {
        return new ResponseEntity<List<AttributeView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Attribute> getContactAttribute(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("attributeId") String attributeId
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
) {
      try {
        Attribute attribute = contactService.getAttribute(token, attributeId, agent);
        return new ResponseEntity<Attribute>(attribute, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Attribute>(HttpStatus.NOT_FOUND);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Attribute>(HttpStatus.UNAUTHORIZED);
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Attribute>(HttpStatus.PAYMENT_REQUIRED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Attribute>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getContactRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Integer rev = contactService.getRevision(token, agent);
        return new ResponseEntity<Integer>(rev, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

