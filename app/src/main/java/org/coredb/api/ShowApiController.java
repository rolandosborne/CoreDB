package org.coredb.api;

import java.io.File;
import org.coredb.model.LabelEntry;
import org.coredb.model.StoreStatus;
import org.coredb.model.SubjectAsset;
import org.coredb.model.SubjectTag;
import org.coredb.model.Asset;
import org.coredb.model.SubjectEntry;
import org.coredb.model.SubjectView;
import org.coredb.model.AssetData;
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

import java.util.Enumeration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.InputStream;
import org.springframework.core.io.InputStreamResource;
import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.lang.IllegalStateException;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.service.ShowService;
import org.coredb.service.GroupService;
import org.coredb.service.AuthService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class ShowApiController implements ShowApi {

    private static final Logger log = LoggerFactory.getLogger(ShowApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    private GroupService groupService;

    @org.springframework.beans.factory.annotation.Autowired
    private ShowService showService;

    @org.springframework.beans.factory.annotation.Autowired
    public ShowApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

public ResponseEntity<SubjectEntry> addSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "schema of subject") @Valid @RequestParam(value = "schema", required = false) String schema
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectEntry entry = showService.addSubject(app.getAccount(), schema);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

	  public ResponseEntity<Asset> addSubjectAsset(@ApiParam(value = "new asset entry" ,required=true ) @Valid @RequestParam("file") MultipartFile file,@NotNull @ApiParam(value = "share token with Service service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "asset transformations 0. BIN pass through 1. MP4 -vf scale=720:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 2. MP4 -vf scale=1280:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 3. MP4 -vf scale=1920:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 4. MP3 -codec:a libmp3lame -qscale:a 6 5. MP3 -codec:a libmp3lame -qscale:a 2 6. MP3 -codec:a libmp3lame -qscale:a 0 7. JPG -resize 1280x720\\> 8. JPG -resize 2688x1520\\> 9. JPG -resize 3840x2160\\>", required = true) @Valid @RequestParam(value = "transforms", required = true) List<String> transforms
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
) {
	  try {
        AccountApp app = authService.showService(token);
        Asset entry = showService.addSubjectAsset(app.getAccount(), subjectId, transforms, file);
        return new ResponseEntity<Asset>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Asset>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<SubjectEntry> addSubjectLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
) {
      try {
          AccountApp app = authService.showService(token);
          SubjectEntry view = showService.addSubjectLabel(app.getAccount(), subjectId, labelId);
          return new ResponseEntity<SubjectEntry>(view, HttpStatus.OK);
        }
        catch(AccessDeniedException e) {
          log.error(e.toString());
          return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch(NotFoundException e) {
          log.error(e.toString());
          return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
        }
        catch(NotAcceptableException e) {
          log.error(e.toString());
          return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
        }
        catch(InvalidParameterException e) {
          log.error(e.toString());
          return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e) {
          log.error(e.toString());
          return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<SubjectEntry> setSubjectLabels(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @ApiParam(value = "referenced subject entry",required=true) @PathVariable("subjectId") String subjectId, @ApiParam(value = ""  )  @Valid @RequestBody List<String> body) {
      try {
        AccountApp app = authService.showService(token);
        SubjectEntry view = showService.setSubjectLabels(app.getAccount(), subjectId, body);
        return new ResponseEntity<SubjectEntry>(view, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<SubjectEntry> clearSubjectLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectEntry view = showService.deleteSubjectLabel(app.getAccount(), subjectId, labelId);
        return new ResponseEntity<SubjectEntry>(view, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<SubjectEntry>> filterSubjects(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "show editing subjects") @Valid @RequestParam(value = "editing", required = false) Boolean editing
,@ApiParam(value = "show pending subjects") @Valid @RequestParam(value = "pending", required = false) Boolean pending
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        AccountApp app = authService.showService(token);
        List<SubjectEntry> subjects = showService.getSubjectSet(app.getAccount(), body, editing, pending);
        return new ResponseEntity<List<SubjectEntry>>(subjects, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectEntry>>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectEntry>>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectEntry>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectEntry>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<LabelEntry>> getShowLabels(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
        try {
          AccountApp service = authService.showService(token);
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

    public ResponseEntity<Integer> getShowRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.showService(token);
        Integer rev = app.getAccount().getShowRevision();
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
	
	public ResponseEntity<StoreStatus> getShowStatus(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
) {
      try {
        AccountApp app = authService.showService(token);
        StoreStatus status = showService.getStatus(app.getAccount());
        return new ResponseEntity<StoreStatus>(status, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<StoreStatus>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<StoreStatus>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<StoreStatus>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<StoreStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<SubjectEntry> getSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectEntry entry = showService.getSubject(app.getAccount(), subjectId);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

	public ResponseEntity<InputStreamResource> getSubjectAsset(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of asset",required=true) @PathVariable("assetId") String assetId
) {
      try {
        AccountApp app = authService.showService(token);

        // determine if range request
        Long min = (long)-1;
        Long max = (long)-1;
        Boolean rangeSet = false;
        Enumeration<String> nameIt = request.getHeaderNames();
        while (nameIt.hasMoreElements()) {
          String name = nameIt.nextElement();
          if(name.equals("range")) {
            Enumeration<String> valueIt = request.getHeaders(name);
            while (valueIt.hasMoreElements()) {
              String value = valueIt.nextElement();
              String[] range = value.split("=");
              String[] values = range[1].split("-");
              if(values.length > 0) {
                min = Long.parseLong(values[0]);
              }
              if(values.length > 1) {
                max = Long.parseLong(values[1]);
              }
              rangeSet = true;
            }
          }
        }

        if(rangeSet) {
          // retrieve byte range
          AssetData data = showService.getSubjectAsset(app.getAccount(), subjectId, assetId, min, max);
          HttpHeaders responseHeaders = new HttpHeaders();
          responseHeaders.set("Accept-Ranges", "bytes");
          responseHeaders.set("Content-Range", "bytes " + data.getBegin().toString() + "-" + data.getEnd().toString() + "/" + data.getSize().toString());
          Long len = (data.getEnd() + 1) - data.getBegin();
          return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(responseHeaders)
              .contentType(MediaType.parseMediaType(data.getContentType()))
              .contentLength(len).body(data.getResource());
        }
        else {
          // retrieve full file
          AssetData data = showService.getSubjectAsset(app.getAccount(), subjectId, assetId);
          return ResponseEntity.status(HttpStatus.OK)
              .contentType(MediaType.parseMediaType(data.getContentType()))
              .contentLength(data.getSize()).body(data.getResource());
        }
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Void> removeSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
) {
      try {
        AccountApp app = authService.showService(token);
        showService.delSubject(app.getAccount(), subjectId);
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
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<SubjectEntry> removeSubjectAsset(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of asset",required=true) @PathVariable("assetId") String assetId
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectEntry entry = showService.delSubjectAsset(app.getAccount(), subjectId, assetId);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<SubjectEntry> updateSubjectAccess(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@NotNull @ApiParam(value = "set if subject is ready", required = true) @Valid @RequestParam(value = "done", required = true) Boolean done
) {
      try {
        AccountApp service = authService.showService(token);
        SubjectEntry entry = showService.updateSubjectAccess(service.getAccount(), subjectId, done);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

	public ResponseEntity<SubjectEntry> updateSubjectData(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "schema of subject", required = true) @Valid @RequestParam(value = "schema", required = true) String schema
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "payload of subject"  )  @Valid @RequestBody String body
) {
      try {
        AccountApp service = authService.showService(token);
        SubjectEntry entry = showService.updateSubject(service.getAccount(), subjectId, schema, body);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
	}

  public ResponseEntity<SubjectEntry> updateSubjectExpire(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "set if subject is to expire") @Valid @RequestParam(value = "expire", required = false) Long expire
) {
      try {
        AccountApp service = authService.showService(token);
        SubjectEntry entry = showService.updateExpire(service.getAccount(), subjectId, expire);
        return new ResponseEntity<SubjectEntry>(entry, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.NOT_FOUND);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

	public ResponseEntity<List<SubjectView>> viewSubjects(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        AccountApp app = authService.showService(token);
        List<SubjectView> subjects = showService.getSubjectViews(app.getAccount(), body);
        return new ResponseEntity<List<SubjectView>>(subjects, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectView>>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectView>>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectView>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<SubjectView>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  public ResponseEntity<SubjectTag> getSubjectTags(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectTag tags = showService.getAccountSubjectTag(app.getAccount(), subjectId, schema, descending);
        return new ResponseEntity<SubjectTag>(tags, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  public ResponseEntity<SubjectTag> addSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
,@ApiParam(value = "stringified data object"  )  @Valid @RequestBody String body
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectTag tags = showService.addAccountSubjectTag(app.getAccount(), subjectId, schema, descending, body);
        return new ResponseEntity<SubjectTag>(tags, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.UNAUTHORIZED);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }        

  public ResponseEntity<SubjectTag> removeSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of tag",required=true) @PathVariable("tagId") String tagId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
) {
      try {
        AccountApp app = authService.showService(token);
        SubjectTag tags = showService.removeAccountSubjectTag(app.getAccount(), subjectId, tagId, schema, descending);
        return new ResponseEntity<SubjectTag>(tags, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.UNAUTHORIZED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<SubjectTag>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

}

