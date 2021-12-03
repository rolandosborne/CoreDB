package org.coredb.api;

import org.coredb.model.AlertEntry;
import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
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
import java.nio.file.AccessDeniedException;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.service.AuthService;
import org.coredb.service.AccountService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AccountApiController implements AccountApi {

    private static final Logger log = LoggerFactory.getLogger(AccountApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<AlertEntry>> getAlerts(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.accountService(token);
        List<AlertEntry> alerts = accountService.getAlerts(app.getAccount());
        return new ResponseEntity<List<AlertEntry>>(alerts, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<ConfigEntry> getConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
) {
      try {
        AccountApp app = authService.accountService(token);
        ConfigEntry entry = accountService.getConfig(app.getAccount(), configId);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_FOUND);
     }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<ConfigEntry>> getConfigs(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.accountService(token);
        List<ConfigEntry> configs = accountService.getConfigs(app.getAccount());
        return new ResponseEntity<List<ConfigEntry>>(configs, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeAlert(@NotNull @ApiParam(value = "shared token with app", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of alert entry",required=true) @PathVariable("alertId") String alertId
) {
      try {
        AccountApp app = authService.accountService(token);
        accountService.deleteAlert(app.getAccount(), alertId);
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

    public ResponseEntity<Void> removeConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
) {
      try {
        AccountApp app = authService.accountService(token);
        accountService.deleteConfig(app.getAccount(), configId);
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

    public ResponseEntity<ConfigEntry> setConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
,@ApiParam(value = "new value for config key" ,required=true )  @Valid @RequestBody Config body
) {
    try {
        AccountApp app = authService.accountService(token);
        ConfigEntry entry = accountService.updateConfig(app.getAccount(), configId, body);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.FORBIDDEN);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
}

