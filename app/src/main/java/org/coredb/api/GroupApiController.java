package org.coredb.api;

import org.coredb.model.LabelEntry;
import org.coredb.model.LabelView;
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

import org.coredb.jpa.entity.AccountApp;

import org.coredb.service.AuthService;
import org.coredb.service.GroupService;

import javax.ws.rs.NotAcceptableException;
import java.security.InvalidParameterException;
import java.nio.file.AccessDeniedException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class GroupApiController implements GroupApi {

    private static final Logger log = LoggerFactory.getLogger(GroupApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private GroupService groupService;

    @org.springframework.beans.factory.annotation.Autowired
    public GroupApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<LabelEntry> addLabel(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "name for new label") @Valid @RequestParam(value = "name", required = false) String name
) {
        try {
          AccountApp service = authService.groupService(token);
          LabelEntry entry = groupService.addLabel(service.getAccount(), name);
          return new ResponseEntity<LabelEntry>(entry, HttpStatus.CREATED);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getGroupRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.groupService(token);
        Integer rev = app.getAccount().getGroupRevision();
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

    public ResponseEntity<List<LabelView>> getLabelViews(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp service = authService.groupService(token);
          List<LabelView> labels = groupService.getLabelViews(service.getAccount());
          return new ResponseEntity<List<LabelView>>(labels, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelView>>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelView>>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelView>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelView>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<LabelEntry>> getLabels(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp service = authService.groupService(token);
          List<LabelEntry> labels = groupService.getLabels(service.getAccount());
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

    public ResponseEntity<LabelEntry> getLabel(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
) {
        try {
          AccountApp service = authService.groupService(token);
          LabelEntry entry = groupService.getLabel(service.getAccount(), labelId);
          return new ResponseEntity<LabelEntry>(entry, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Void> removeLabel(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of label to delete",required=true) @PathVariable("labelId") String labelId
) {
        try {
          AccountApp service = authService.groupService(token);
          groupService.delLabel(service.getAccount(), labelId);
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

    public ResponseEntity<LabelEntry> updateLabelName(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of label to update",required=true) @PathVariable("labelId") String labelId
,@NotNull @ApiParam(value = "updated name for label", required = true) @Valid @RequestParam(value = "name", required = true) String name
) {
        try {
          AccountApp service = authService.groupService(token);
          LabelEntry entry = groupService.updLabel(service.getAccount(), labelId, name);
          return new ResponseEntity<LabelEntry>(entry, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<LabelEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

