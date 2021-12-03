package org.coredb.api;

import java.io.*;
import java.net.*;
import org.coredb.model.Amigo;
import org.coredb.model.AmigoMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
import org.springframework.core.io.InputStreamResource;

import javax.ws.rs.NotAcceptableException;
import java.security.InvalidParameterException;
import org.springframework.dao.DataIntegrityViolationException;
import javax.ws.rs.ForbiddenException;
import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;

import org.coredb.service.IdentityService;
import org.coredb.service.AuthService;

import org.coredb.jpa.entity.AccountApp;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class IdentityApiController implements IdentityApi {

    private static final Logger log = LoggerFactory.getLogger(IdentityApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private IdentityService identityService;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public IdentityApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Boolean> getDirtyFlag(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.identityService(token);
        Boolean res = identityService.getDirty(app.getAccount());
        return new ResponseEntity<Boolean>(res, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Amigo> getIdentity(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.identityService(token);
        Amigo amigo = identityService.getIdentity(app.getAccount());
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> getIdentityMessage(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.getMessage(app.getAccount());
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getIdentityRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.identityService(token);
        Integer rev = identityService.getRevision(app.getAccount());
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

    public ResponseEntity<Void> setDirtyFlag(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "dirty value to set", required = true) @Valid @RequestParam(value = "flag", required = true) Boolean flag
,@ApiParam(value = "apply only if at given revision") @Valid @RequestParam(value = "revision", required = false) Integer revision
) {
      try {
        AccountApp app = authService.identityService(token);
        identityService.setDirty(app.getAccount(), flag, revision);
        return new ResponseEntity<Void>(HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateDescription(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateDescription(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateHandle(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateHandle(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<InputStreamResource> getIdentityLogo(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.identityService(token);
        InputStream stream = identityService.getLogo(app.getAccount());
        InputStreamResource resource = new InputStreamResource(stream);
        String mimeType = URLConnection.guessContentTypeFromStream(stream);
        return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.parseMediaType(mimeType))
              .contentLength(stream.available()).body(resource);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateImage(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateLogo(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateLocation(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateLocation(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateName(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateName(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> updateRegistry(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody(required = false) String body
) {
      try {
        AccountApp app = authService.identityService(token);
        AmigoMessage msg = identityService.updateRegistry(app.getAccount(), body);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.UNAUTHORIZED);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

