package org.coredb.api;

import org.coredb.model.Amigo;
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

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.security.InvalidParameterException;
import java.nio.file.AccessDeniedException;

import org.coredb.service.AuthService;
import org.coredb.service.UserService;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountApp;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<UserEntry> getUser(@NotNull @ApiParam(value = "Service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "uuid of user entry",required=true) @PathVariable("amigoId") String amigoId
) { 
        try {
          AccountApp app = authService.userService(token);
          UserEntry entry = userService.getUser(app.getAccount(), amigoId);
          return new ResponseEntity<UserEntry>(entry, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.BAD_REQUEST);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.NOT_ACCEPTABLE);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.NOT_FOUND);
       }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(DataIntegrityViolationException e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.FORBIDDEN);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<UserEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Amigo> getUserIdentity(@NotNull @ApiParam(value = "Service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "uuid of user entry",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp app = authService.userService(token);
          Amigo entry = userService.getIdentity(amigoId);
          return new ResponseEntity<Amigo>(entry, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.BAD_REQUEST);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.NOT_ACCEPTABLE);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.NOT_FOUND);
       }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
        }
        catch(DataIntegrityViolationException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.FORBIDDEN);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<UserEntry>> getUsers(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp app = authService.userService(token);
          List<UserEntry> entry = userService.getEmigos(app.getAccount());
          return new ResponseEntity<List<UserEntry>>(entry, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<List<UserEntry>>(HttpStatus.BAD_REQUEST);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<List<UserEntry>>(HttpStatus.NOT_ACCEPTABLE);
        }
        //catch(NotFoundException e) {
        //  log.error(e.toString());
        //  return new ResponseEntity<List<UserEntry>>(HttpStatus.NOT_FOUND);
        //}
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<UserEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(DataIntegrityViolationException e) {
          log.error(e.toString());
          return new ResponseEntity<List<UserEntry>>(HttpStatus.FORBIDDEN);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<UserEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getUserRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.userService(token);
        Integer rev = app.getAccount().getUserRevision();
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

    public ResponseEntity<Void> removeUser(@NotNull @ApiParam(value = "service-access-token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of service entry",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp app = authService.userService(token);
          userService.deleteEmigo(app.getAccount(), amigoId);
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

}
