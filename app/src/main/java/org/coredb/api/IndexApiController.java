package org.coredb.api;

import java.net.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import org.coredb.model.Amigo;
import org.coredb.model.AmigoView;
import org.coredb.model.AmigoEntry;
import org.coredb.model.AmigoMessage;
import org.coredb.model.LabelEntry;
import org.coredb.model.PendingAmigo;
import org.coredb.model.PendingAmigoView;
import org.coredb.model.ShareMessage;
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
import org.springframework.core.io.InputStreamResource;

import javax.ws.rs.NotAcceptableException;
import java.nio.file.AccessDeniedException;
import org.coredb.api.NotFoundException;
import java.security.InvalidParameterException;

import org.coredb.service.GroupService;
import org.coredb.service.IndexService;
import org.coredb.service.AuthService;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountApp;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class IndexApiController implements IndexApi {

    private static final Logger log = LoggerFactory.getLogger(IndexApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private IndexService indexService;

    @org.springframework.beans.factory.annotation.Autowired
    private GroupService groupService;

    @org.springframework.beans.factory.annotation.Autowired
    public IndexApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AmigoEntry> addAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "message describing amigo"  )  @Valid @RequestBody AmigoMessage body
) {
        try {
          AccountApp service = authService.indexService(token);
          AmigoEntry entry = indexService.addEmigo(service.getAccount(), body);
          return new ResponseEntity<AmigoEntry>(entry, HttpStatus.CREATED);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
          log.error(e.toString());
         return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Amigo> updateAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "message describing amigo"  )  @Valid @RequestBody AmigoMessage body
) {
      try {
          AccountApp service = authService.indexService(token);
          Amigo entry = indexService.setEmigo(service.getAccount(), body);
          return new ResponseEntity<Amigo>(entry, HttpStatus.CREATED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
         return new ResponseEntity<Amigo>(HttpStatus.NOT_FOUND);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<Amigo>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
          log.error(e.toString());
         return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AmigoEntry> addAmigoLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "referenced label entry",required=true) @PathVariable("labelId") String labelId
,@ApiParam(value = "referenced amigo entry",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp service = authService.indexService(token);
          AmigoEntry amigo = indexService.addEmigoLabel(service.getAccount(), amigoId, labelId);
          return new ResponseEntity<AmigoEntry>(amigo, HttpStatus.CREATED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    public ResponseEntity<AmigoEntry> setAmigoLabels(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @ApiParam(value = "referenced amigo entry",required=true) @PathVariable("amigoId") String amigoId, @ApiParam(value = ""  )  @Valid @RequestBody List<String> body) {
        try {
          AccountApp service = authService.indexService(token);
          AmigoEntry amigo = indexService.setEmigoLabels(service.getAccount(), amigoId, body);
          return new ResponseEntity<AmigoEntry>(amigo, HttpStatus.OK);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 
    public ResponseEntity<Void> addAmigoReject(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        indexService.addEmigoReject(service.getAccount(), amigoId);
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
   
    public ResponseEntity<AmigoEntry> getAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        AmigoEntry amigo = indexService.getEmigo(service.getAccount(), amigoId);
        return new ResponseEntity<AmigoEntry>(amigo, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getAmigoRevision(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        Integer revision = indexService.getRevision(amigoId);
        return new ResponseEntity<Integer>(revision, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
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

    public ResponseEntity<Amigo> getAmigoIdentity(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        Amigo amigo = indexService.getIdentity(amigoId);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<InputStreamResource> getAmigoLogo(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        InputStream stream = indexService.getLogo(amigoId);
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

    public ResponseEntity<List<String>> getAmigoRejects(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp service = authService.indexService(token);
        List<String> ids = indexService.getEmigoRejects(service.getAccount());
        return new ResponseEntity<List<String>>(ids, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<List<String>>(HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<List<PendingAmigoView>> getAmigoRequests(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp service = authService.indexService(token);
        List<PendingAmigoView> amigos = indexService.getEmigoRequests(service.getAccount());
        return new ResponseEntity<List<PendingAmigoView>>(amigos, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        return new ResponseEntity<List<PendingAmigoView>>(HttpStatus.BAD_REQUEST);
      }
      catch(AccessDeniedException e) {
        return new ResponseEntity<List<PendingAmigoView>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        return new ResponseEntity<List<PendingAmigoView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<AmigoEntry>> getAmigos(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp service = authService.indexService(token);
          List<AmigoEntry> amigos = indexService.getEmigos(service.getAccount());
          return new ResponseEntity<List<AmigoEntry>>(amigos, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<AmigoEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<AmigoEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<LabelEntry>> getIndexLabels(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp service = authService.groupService(token);
          List<LabelEntry> labels = groupService.getLabels(service.getAccount());
          return new ResponseEntity<List<LabelEntry>>(labels, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<List<LabelEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getIndexRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.indexService(token);
        Integer rev = app.getAccount().getIndexRevision();
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

    public ResponseEntity<PendingAmigo> getPendingRequest(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced request",required=true) @PathVariable("shareId") String shareId
) {
      try {
        AccountApp service = authService.indexService(token);
        PendingAmigo amigo = indexService.getPendingAmigo(service.getAccount(), shareId);
        return new ResponseEntity<PendingAmigo>(amigo, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<PendingAmigo>(HttpStatus.NOT_FOUND);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<PendingAmigo>(HttpStatus.BAD_REQUEST);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<PendingAmigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<PendingAmigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp service = authService.indexService(token);
          indexService.deleteEmigo(service.getAccount(), amigoId);
          return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<AmigoEntry> removeAmigoLabel(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of label entry",required=true) @PathVariable("labelId") String labelId
,@ApiParam(value = "id of amigo entry",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp service = authService.indexService(token);
          AmigoEntry view = indexService.deleteEmigoLabel(service.getAccount(), amigoId, labelId);
          return new ResponseEntity<AmigoEntry>(view, HttpStatus.OK);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AmigoEntry> removeAmigoNotes(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
) {
      try {
        AccountApp service = authService.indexService(token);
        AmigoEntry entry = indexService.clearEmigoNotes(service.getAccount(), amigoId);
        return new ResponseEntity<AmigoEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeAmigoReject(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced amigo",required=true) @PathVariable("amigoId") String amigoId
) {
        try {
          AccountApp service = authService.indexService(token);
          indexService.deleteEmigoReject(service.getAccount(), amigoId);
          return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<ShareMessage> removeAmigoRequest(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced request",required=true) @PathVariable("shareId") String shareId
,@ApiParam(value = "add to reject list") @Valid @RequestParam(value = "reject", required = false) Boolean reject
) {
        try {
          AccountApp service = authService.indexService(token);
          ShareMessage msg = indexService.deleteEmigoRequest(service.getAccount(), shareId, reject);
          return new ResponseEntity<ShareMessage>(msg, HttpStatus.OK);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<ShareMessage>(HttpStatus.NOT_FOUND);
        }
        catch(IllegalArgumentException e) {
          log.error(e.toString());
          return new ResponseEntity<ShareMessage>(HttpStatus.BAD_REQUEST);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<ShareMessage>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<ShareMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AmigoEntry> updateAmigoNotes(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "notes for amigo"  )  @Valid @RequestBody String body
) {
      try {
        AccountApp service = authService.indexService(token);
        AmigoEntry entry = indexService.setEmigoNotes(service.getAccount(), amigoId, body);
        return new ResponseEntity<AmigoEntry>(entry, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AmigoEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<AmigoView>> viewAmigos(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp service = authService.indexService(token);
        List<AmigoView> amigos = indexService.getEmigoViews(service.getAccount());
        return new ResponseEntity<List<AmigoView>>(amigos, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<AmigoView>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<AmigoView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
}

