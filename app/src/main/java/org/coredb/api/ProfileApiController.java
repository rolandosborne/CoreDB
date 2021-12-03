package org.coredb.api;

import org.coredb.model.Attribute;
import org.coredb.model.AttributeEntry;
import org.coredb.model.AttributeView;
import org.coredb.model.LabelEntry;
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

import org.coredb.jpa.entity.AccountApp;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import java.security.InvalidParameterException;

import org.coredb.service.GroupService;
import org.coredb.service.ProfileService;
import org.coredb.service.AuthService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class ProfileApiController implements ProfileApi {

    private static final Logger log = LoggerFactory.getLogger(ProfileApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private ProfileService profileService;

    @org.springframework.beans.factory.annotation.Autowired
    private GroupService groupService;

    @org.springframework.beans.factory.annotation.Autowired
    public ProfileApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AttributeEntry> addAttribute(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "schema of attribute", required = true) @Valid @RequestParam(value = "schema", required = true) String schema
,@ApiParam(value = "payload of attribute"  )  @Valid @RequestBody String body
) {
        try {
          AccountApp app = authService.profileService(token);
          AttributeEntry attributes = profileService.addAttribute(app.getAccount(), schema, body);
          return new ResponseEntity<AttributeEntry>(attributes, HttpStatus.CREATED);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AttributeEntry> clearAttributeLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("attributeId") String attributeId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
) {
        try {
          AccountApp app = authService.profileService(token);
          AttributeEntry view = profileService.deleteAttributeLabel(app.getAccount(), labelId, attributeId);
          return new ResponseEntity<AttributeEntry>(view, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<AttributeEntry>> filterAttributes(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
        try {
          AccountApp app = authService.profileService(token);
          List<AttributeEntry> attributes = profileService.getAttributeSet(app.getAccount(), body);
          return new ResponseEntity<List<AttributeEntry>>(attributes, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeEntry>>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AttributeEntry> getAttribute(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("attributeId") String attributeId
) {
        try {
          AccountApp app = authService.profileService(token);
          AttributeEntry attribute = profileService.getAttributeEntry(app.getAccount(), attributeId);
          return new ResponseEntity<AttributeEntry>(attribute, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<LabelEntry>> getProfileLabels(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp app = authService.profileService(token);
          List<LabelEntry> labels = groupService.getLabels(app.getAccount());
          return new ResponseEntity<List<LabelEntry>>(labels, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getProfileRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.profileService(token);
        Integer rev = app.getAccount().getProfileRevision();
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

    public ResponseEntity<Void> removeAttribute(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("attributeId") String attributeId
) {
        try {
          AccountApp app = authService.profileService(token);
          profileService.deleteAttribute(app.getAccount(), attributeId);
          return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AttributeEntry> setAttribute(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "schema of attribute", required = true) @Valid @RequestParam(value = "schema", required = true) String schema
,@ApiParam(value = "id of attribute",required=true) @PathVariable("attributeId") String attributeId
,@ApiParam(value = "payload of attribute"  )  @Valid @RequestBody String body
) {
        try {
          AccountApp app = authService.profileService(token);
          profileService.updateAttribute(app.getAccount(), attributeId, schema, body);
          return new ResponseEntity<AttributeEntry>(HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AttributeEntry> setAttributeLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("attributeId") String attributeId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
) {
        try {
          AccountApp app = authService.profileService(token);
          AttributeEntry view = profileService.addAttributeLabel(app.getAccount(), labelId, attributeId);
          return new ResponseEntity<AttributeEntry>(view, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AttributeEntry> setAttributeLabels(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @ApiParam(value = "referenced attribute entry",required=true) @PathVariable("attributeId") String attributeId, @ApiParam(value = ""  )  @Valid @RequestBody List<String> body) {
        try {
          AccountApp app = authService.profileService(token);
          AttributeEntry view = profileService.setAttributeLabels(app.getAccount(), attributeId, body);
          return new ResponseEntity<AttributeEntry>(view, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AttributeEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<AttributeView>> viewAttributes(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
        try {
          AccountApp app = authService.profileService(token);
          List<AttributeView> attributes = profileService.getProfileViews(app.getAccount(), body);
          return new ResponseEntity<List<AttributeView>>(attributes, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeView>>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeView>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<AttributeView>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

