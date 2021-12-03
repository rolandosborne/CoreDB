package org.coredb.api;

import org.coredb.model.Amigo;
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

import java.nio.file.AccessDeniedException;
import org.coredb.service.PortalService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class PortalApiController implements PortalApi {

    private static final Logger log = LoggerFactory.getLogger(PortalApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private PortalService portalService;

    @org.springframework.beans.factory.annotation.Autowired
    public PortalApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> accountToken(@NotNull @Parameter(in = ParameterIn.QUERY, description = "device username" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "alias", required = true) String alias,@NotNull @Parameter(in = ParameterIn.QUERY, description = "device password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        String token = "\"" + portalService.generateAccountToken(alias, password) + "\"";
        return new ResponseEntity<String>(token, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Boolean> checkLogin(@NotNull @Parameter(in = ParameterIn.QUERY, description = "id of account" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username,@Parameter(in = ParameterIn.QUERY, description = "id of requestor" ,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = false) String amigoId) {
      try {
        Boolean available = portalService.checkUsername(username, amigoId);
        return new ResponseEntity<Boolean>(available, HttpStatus.OK);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Boolean> checkToken(@NotNull @Parameter(in = ParameterIn.QUERY, description = "admin generated token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        Boolean available = portalService.checkToken(token);
        return new ResponseEntity<Boolean>(available, HttpStatus.OK);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Amigo> getDeviceAmigoProfile(@Parameter(in = ParameterIn.PATH, description = "id of amigo to access", required=true, schema=@Schema()) @PathVariable("amigoId") String amigoId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "handle to use" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "handle", required = true) String handle,@NotNull @Parameter(in = ParameterIn.QUERY, description = "login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        Amigo amigo = portalService.getAmigoProfile(handle, password, amigoId);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
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

    public ResponseEntity<List<String>> getDeviceAmigos(@NotNull @Parameter(in = ParameterIn.QUERY, description = "handle to use" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "handle", required = true) String handle,@NotNull @Parameter(in = ParameterIn.QUERY, description = "login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        List<String> amigos = portalService.getAmigos(handle, password);
        return new ResponseEntity<List<String>>(amigos, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<String>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Amigo> getProfile(@NotNull @Parameter(in = ParameterIn.QUERY, description = "handle to use" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "handle", required = true) String handle,@NotNull @Parameter(in = ParameterIn.QUERY, description = "login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        Amigo amigo = portalService.getProfile(handle, password);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
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

    public ResponseEntity<Amigo> resetLogin(@NotNull @Parameter(in = ParameterIn.QUERY, description = "id of account" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username,@NotNull @Parameter(in = ParameterIn.QUERY, description = "new account handle" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "newUsername", required = true) String newUsername,@NotNull @Parameter(in = ParameterIn.QUERY, description = "new login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        Amigo amigo = portalService.resetUsername(username, password, newUsername);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
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

    public ResponseEntity<Amigo> resetPassword(@NotNull @Parameter(in = ParameterIn.QUERY, description = "new login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password,@NotNull @Parameter(in = ParameterIn.QUERY, description = "token to grant permission" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        Amigo amigo = portalService.resetPassword(token, password);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
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

    public ResponseEntity<String> resetToken(@NotNull @Parameter(in = ParameterIn.QUERY, description = "id of amigo to access" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = true) String amigoId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "device username" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "alias", required = true) String alias,@NotNull @Parameter(in = ParameterIn.QUERY, description = "device password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        String token = "\"" + portalService.generateResetToken(alias, password, amigoId) + "\"";
        return new ResponseEntity<String>(token, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<String> setPassCode(@NotNull @Parameter(in = ParameterIn.QUERY, description = "username of account" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username,@NotNull @Parameter(in = ParameterIn.QUERY, description = "portal login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        String token = "\"" + portalService.setPassCode(username, password) + "\"";
        return new ResponseEntity<String>(token, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Amigo> setProfile(@NotNull @Parameter(in = ParameterIn.QUERY, description = "username to use" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username,@NotNull @Parameter(in = ParameterIn.QUERY, description = "login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password,@NotNull @Parameter(in = ParameterIn.QUERY, description = "account create token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token) {
      try {
        Amigo amigo = portalService.setProfile(username, password, token);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.CREATED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(IOException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeDeviceAmigoProfile(@Parameter(in = ParameterIn.PATH, description = "id of amigo to access", required=true, schema=@Schema()) @PathVariable("amigoId") String amigoId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "device alias" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "alias", required = true) String alias,@NotNull @Parameter(in = ParameterIn.QUERY, description = "login password" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password) {
      try {
        portalService.removeAmigoProfile(alias, password, amigoId);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
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

    public ResponseEntity<Boolean> checkAdmin() {
      try {
        Boolean set = portalService.checkAdmin();
        return new ResponseEntity<Boolean>(set, HttpStatus.OK);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> setAdmin(@NotNull @Parameter(in = ParameterIn.QUERY, description = "username for admin" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "username", required = true) String username, @NotNull @Parameter(in = ParameterIn.QUERY, description = "password for admin" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "password", required = true) String password, @NotNull @Parameter(in = ParameterIn.QUERY, description = "domain of node" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "domain", required = true) String domain) {
    try {
      portalService.setAdmin(username, password, domain);
      return new ResponseEntity<Void>(HttpStatus.OK);
    }
    catch(AccessDeniedException e) {
      log.error(e.toString());
      return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
    }
    catch(Exception e) {
      log.error(e.toString());
      return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

