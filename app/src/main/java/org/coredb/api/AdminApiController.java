package org.coredb.api;

import org.coredb.model.AccountEntry;
import org.coredb.model.AccountStatus;
import org.coredb.model.AlertEntry;
import org.coredb.model.Amigo;
import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
import org.coredb.model.AmigoMessage;
import org.coredb.model.AmigoToken;
import org.coredb.model.LinkMessage;
import org.coredb.model.ServerInfo;
import org.coredb.model.Service;
import org.coredb.model.SystemStat;
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

import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;

import org.coredb.service.AuthService;
import org.coredb.service.AdminService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AdminApiController implements AdminApi {

    private static final Logger log = LoggerFactory.getLogger(AdminApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private AdminService adminService;

    @org.springframework.beans.factory.annotation.Autowired
    public AdminApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Service> addRegistryService(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "new service to be added" ,required=true )  @Valid @RequestBody AmigoMessage body
,@ApiParam(value = "enabled state of service") @Valid @RequestParam(value = "enable", required = false) Boolean enable
) {
      try {
        authService.accountToken(token);
        org.coredb.model.Service service = adminService.addService(body, enable);
        return new ResponseEntity<Service>(service, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

        public ResponseEntity<AccountEntry> addServerAccount(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
    try {
      authService.accountToken(token);
      AccountEntry entry = adminService.addAccount();
      return new ResponseEntity<AccountEntry>(entry, HttpStatus.OK);
    }
    catch(InvalidParameterException e) {
      log.error(e.toString());
      return new ResponseEntity<AccountEntry>(HttpStatus.UNAUTHORIZED);
    }
    catch(IllegalArgumentException e) {
      log.error(e.toString());
      return new ResponseEntity<AccountEntry>(HttpStatus.BAD_REQUEST); //400
    }
    catch(NotAcceptableException e) {
      log.error(e.toString());
      return new ResponseEntity<AccountEntry>(HttpStatus.NOT_ACCEPTABLE);
    }
    catch(Exception e) {
      log.error(e.toString());
      return new ResponseEntity<AccountEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

    public ResponseEntity<AlertEntry> addServerAlert(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "amigo to insert" ,required=true )  @Valid @RequestBody String body
) {
      try {
        authService.accountToken(token);
        AlertEntry entry = adminService.addAlert(amigoId, body);
        return new ResponseEntity<AlertEntry>(entry, HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.NOT_FOUND);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AlertEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> addServerStat(@NotNull @Min(0) @Max(100) @ApiParam(value = "time", required = true, allowableValues = "") @Valid @RequestParam(value = "processor", required = true) Integer processor
,@NotNull @ApiParam(value = "current memory free", required = true) @Valid @RequestParam(value = "memory", required = true) Long memory
,@NotNull @ApiParam(value = "current storage free", required = true) @Valid @RequestParam(value = "storage", required = true) Long storage
,@ApiParam(value = "admin stat token") @Valid @RequestParam(value = "token", required = false) String token
) {
      try {
        authService.statToken(token);
        adminService.addStat(processor, memory, storage);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AccountStatus> adminStatus(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.accountToken(token);
        AccountStatus accounts = adminService.getStatus();
        return new ResponseEntity<AccountStatus>(accounts, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountStatus>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountStatus>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AccountStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> deleteAccount(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        adminService.removeLabels(amigoId); // should be able to do this in 1 transaction
        adminService.removeAccount(amigoId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<AmigoMessage>> getAccountMessages(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.accountToken(token);
        List<AmigoMessage> accounts = adminService.getMessages();
        return new ResponseEntity<List<AmigoMessage>>(accounts, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AmigoMessage>>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AmigoMessage>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<AmigoMessage>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<ConfigEntry> getAdminConfig(@NotNull @ApiParam(value = "service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
) {
      try {
        authService.configToken(token);
        ConfigEntry entry = adminService.getServerConfig(configId);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<ConfigEntry>> getAdminConfigs(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.configToken(token);
        List<ConfigEntry> configs = adminService.getServerConfigs();
        return new ResponseEntity<List<ConfigEntry>>(configs, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Service> getRegistryService(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of service item",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        org.coredb.model.Service service = adminService.getService(amigoId);
        return new ResponseEntity<Service>(service, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<Service>> getRegistryServices(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.accountToken(token);
        List<org.coredb.model.Service> services = adminService.getServices();
        return new ResponseEntity<List<Service>>(services, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<Service>>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<Service>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<Service>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Amigo> getServerAccount(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        Amigo entry = adminService.getAccount(amigoId);
        return new ResponseEntity<Amigo>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<AccountEntry>> getServerAccounts(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.accountToken(token);
        List<AccountEntry> accounts = adminService.getAccounts();
        return new ResponseEntity<List<AccountEntry>>(accounts, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AccountEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AccountEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<AccountEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<AlertEntry>> getServerAlerts(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        List<AlertEntry> entries = adminService.getAlerts(amigoId);
        return new ResponseEntity<List<AlertEntry>>(entries, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.NOT_FOUND);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<AlertEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<ConfigEntry> getServerConfig(@NotNull @ApiParam(value = "installation password for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account to access",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
) {
      try {
        authService.accountToken(token);
        ConfigEntry entry = adminService.getAccountConfig(amigoId, configId);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<ConfigEntry>> getServerConfigs(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account to access",required=true) @PathVariable("amigoId") String amigoId
) {
       try {
        authService.accountToken(token);
        List<ConfigEntry> configs = adminService.getAccountConfigs(amigoId);
        return new ResponseEntity<List<ConfigEntry>>(configs, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<ConfigEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<SystemStat>> getServerStats(@NotNull @ApiParam(value = "service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.statToken(token);
        List<SystemStat> stats = adminService.getStats();
        return new ResponseEntity<List<SystemStat>>(stats, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<SystemStat>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeAdminConfig(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
) {
      try {
        authService.configToken(token);
        adminService.clearServerConfig(configId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeRegistryService(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of entry to delete",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        adminService.deleteService(amigoId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeServerAlert(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "id of alert entry",required=true) @PathVariable("alertId") String alertId
) {
      try {
        authService.accountToken(token);
        adminService.clearAlert(amigoId, alertId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
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
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeServerConfig(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account to access",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
) {
      try {
        authService.accountToken(token);
        adminService.clearAccountConfig(amigoId, configId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<ServerInfo> serverInfo(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        authService.configToken(token);
        ServerInfo info = adminService.getInfo();
        return new ResponseEntity<ServerInfo>(info, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ServerInfo>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ServerInfo>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ServerInfo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoToken> setAccountService(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced account",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = ""  )  @Valid @RequestBody LinkMessage body
) {
      try {
        authService.accountToken(token);
        AmigoToken amigo = adminService.setAccountService(amigoId, body);
        return new ResponseEntity<AmigoToken>(amigo, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoToken>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
 
    public ResponseEntity<ConfigEntry> updateAdminConfig(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
,@ApiParam(value = "new value for config key" ,required=true )  @Valid @RequestBody Config body
) {
      try {
        authService.configToken(token);
        ConfigEntry entry = adminService.setServerConfig(configId, body);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Service> updateRegistryService(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "identifier for service entry",required=true) @PathVariable("amigoId") String amigoId
,@NotNull @ApiParam(value = "if service is enabled", required = true) @Valid @RequestParam(value = "enable", required = true) Boolean enable
) {
      try {
        authService.accountToken(token);
        org.coredb.model.Service service = adminService.setService(amigoId, enable);
        return new ResponseEntity<Service>(service, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Service>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AccountEntry> updateServerAccountEnabled(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
,@NotNull @ApiParam(value = "enabled state", required = true) @Valid @RequestParam(value = "flag", required = true) Boolean flag
) {
      try {
        authService.accountToken(token);
        AccountEntry entry = adminService.setAccountState(amigoId, flag);
        return new ResponseEntity<AccountEntry>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AccountEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateServerAccountRegistry(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
,@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "url", required = true) String url
) {
      try {
        authService.accountToken(token);
        AmigoMessage entry = adminService.setAccountRegistry(amigoId, url);
        return new ResponseEntity<AmigoMessage>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateServerAccountRevision(@NotNull @ApiParam(value = "installation token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account entry",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        authService.accountToken(token);
        AmigoMessage entry = adminService.setAccountRevision(amigoId);
        return new ResponseEntity<AmigoMessage>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<ConfigEntry> updateServerConfig(@NotNull @ApiParam(value = "installation token for admin access", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of account to access",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "key of config to update",required=true) @PathVariable("configId") String configId
,@ApiParam(value = "new value for config key" ,required=true )  @Valid @RequestBody Config body
) {
      try {
        authService.accountToken(token);
        ConfigEntry entry = adminService.setAccountConfig(amigoId, configId, body);
        return new ResponseEntity<ConfigEntry>(entry, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<ConfigEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

