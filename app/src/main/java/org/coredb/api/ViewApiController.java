package org.coredb.api;

import org.coredb.model.AuthMessage;
import java.io.File;
import org.coredb.model.Subject;
import org.coredb.model.SubjectTag;
import org.coredb.model.SubjectView;
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

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.lang.IllegalStateException;
import java.security.InvalidParameterException;

import org.coredb.service.ViewService;
import org.coredb.model.AssetData;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class ViewApiController implements ViewApi {

    private static final Logger log = LoggerFactory.getLogger(ViewApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private ViewService viewService;

    @org.springframework.beans.factory.annotation.Autowired
    public ViewApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> addViewAgent(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "updated access rights" ,required=true )  @Valid @RequestBody AuthMessage body
) {
      try {
        String auth = viewService.addAgent(token, body);
        return new ResponseEntity<String>(auth, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<List<SubjectView>> filterViewRevisions(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        List<SubjectView> subjects = viewService.getSubjectViewSet(token, body, agent);
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

    public ResponseEntity<List<Subject>> filterViewSubjects(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
) {
      try {
        List<Subject> subjects = viewService.getSubjectSet(token, body, agent);
        return new ResponseEntity<List<Subject>>(subjects, HttpStatus.OK);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<List<Subject>>(HttpStatus.METHOD_NOT_ALLOWED);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<List<Subject>>(HttpStatus.LOCKED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<List<Subject>>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<List<Subject>>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<Integer> getViewRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
) {
      try {
        Integer rev = viewService.getRevision(token, agent);
        return new ResponseEntity<Integer>(rev, HttpStatus.OK);
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

    public ResponseEntity<Subject> getViewSubject(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
) {
      try {
        Subject entry = viewService.getSubject(token, agent, subjectId);
        return new ResponseEntity<Subject>(entry, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.BAD_REQUEST);
      }
      catch(NotAcceptableException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.NOT_ACCEPTABLE);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.NOT_FOUND);
      }
      catch(AccessDeniedException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.UNAUTHORIZED);
      }
      catch(DataIntegrityViolationException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.FORBIDDEN);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Subject>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

	public ResponseEntity<InputStreamResource> getViewSubjectAsset(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of asset",required=true) @PathVariable("assetId") String assetId
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
) {
      try {

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
          AssetData data = viewService.getSubjectAsset(token, agent, subjectId, assetId, min, max);
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
          AssetData data = viewService.getSubjectAsset(token, agent, subjectId, assetId);
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

  public ResponseEntity<SubjectTag> getViewSubjectTags(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
) {
      try {
        SubjectTag tags = viewService.getSubjectTag(token, agent, subjectId, schema, descending);
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

  public ResponseEntity<SubjectTag> addViewSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
,@ApiParam(value = "stringified data object"  )  @Valid @RequestBody String body
) {
      try {
        SubjectTag tags = viewService.addSubjectTag(token, agent, subjectId, schema, descending, body);
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

  public ResponseEntity<SubjectTag> removeViewSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of tag",required=true) @PathVariable("tagId") String tagId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
,@ApiParam(value = "token from delivered auth message") @Valid @RequestParam(value = "agent", required = false) String agent
) {
      try {
        SubjectTag tags = viewService.removeSubjectTag(token, agent, subjectId, tagId, schema, descending);
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

