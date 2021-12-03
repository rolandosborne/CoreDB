package org.coredb.api;

import org.coredb.model.ServiceAccess;
import org.coredb.model.ServiceEntry;
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
import org.coredb.service.AccessService;
import org.coredb.service.AppService;
import org.coredb.jpa.entity.AccountApp;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class ServiceApiController implements ServiceApi {

    private static final Logger log = LoggerFactory.getLogger(ServiceApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccessService accessService;
    
    @org.springframework.beans.factory.annotation.Autowired
    public AppService appService;

    @org.springframework.beans.factory.annotation.Autowired
    public ServiceApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ServiceEntry> getServiceEntry(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of service entry",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
          AccountApp app = authService.appService(token);
          ServiceEntry entry = appService.getApp(app.getAccount(), amigoId);
          return new ResponseEntity<ServiceEntry>(entry, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.BAD_REQUEST);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.NOT_ACCEPTABLE);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.NOT_FOUND);
       }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(DataIntegrityViolationException e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.FORBIDDEN);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<ServiceEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Void> deleteApp(@NotNull @ApiParam(value = "Service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of service entry",required=true) @PathVariable("amigoId") String amigoId
) { 
      try {
          AccountApp app = authService.appService(token);
          appService.deleteApp(app.getAccount(), amigoId);
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

    public ResponseEntity<List<ServiceEntry>> getApps(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp app = authService.appService(token);
          List<ServiceEntry> topics = appService.getApps(app.getAccount());
          return new ResponseEntity<List<ServiceEntry>>(topics, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<List<ServiceEntry>>(HttpStatus.BAD_REQUEST);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<List<ServiceEntry>>(HttpStatus.NOT_ACCEPTABLE);
        }
        //catch(NotFoundException e) {
        //  log.error(e.toString());
        //  return new ResponseEntity<List<ServiceEntry>>(HttpStatus.NOT_FOUND);
        //}
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<ServiceEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(DataIntegrityViolationException e) {
          log.error(e.toString());
          return new ResponseEntity<List<ServiceEntry>>(HttpStatus.FORBIDDEN);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<ServiceEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getServiceRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        AccountApp app = authService.appService(token);
        Integer rev = app.getAccount().getServiceRevision();
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

    public ResponseEntity<ServiceEntry> setServicePermission(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of service entry",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "updated access rights" ,required=true )  @Valid @RequestBody ServiceAccess body
) {
      try {
        AccountApp app = authService.appService(token);
        ServiceEntry access = appService.setAppAuthorizedAccess(app.getAccount(), amigoId, body);
        return new ResponseEntity<ServiceEntry>(access, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      //catch(NotFoundException e) {
      //  log.error(e.toString());
      //  return new ResponseEntity<ServiceEntry>(HttpStatus.NOT_FOUND);
      //}
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ServiceEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}
