package org.coredb.api;

import org.coredb.model.AmigoMessage;
import org.springframework.core.io.Resource;
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

import java.io.InputStream;
import java.net.URLConnection;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.coredb.service.RegistryService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class RegistryApiController implements RegistryApi {

    private static final Logger log = LoggerFactory.getLogger(RegistryApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private RegistryService registryService;

    @org.springframework.beans.factory.annotation.Autowired
    public RegistryApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<String> getId(@NotNull @Parameter(in = ParameterIn.QUERY, description = "handle to validate" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "handle", required = true) String handle,@Parameter(in = ParameterIn.QUERY, description = "wrap response in quotes" ,schema=@Schema()) @Valid @RequestParam(value = "wrap", required = false) Boolean wrap) {
      try {
        String id = registryService.getAccountId(handle);
        return new ResponseEntity<String>(id, HttpStatus.OK);
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

    public ResponseEntity<InputStreamResource> getLogo(@Parameter(in = ParameterIn.QUERY, description = "referenced id" ,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = false) String amigoId,@Parameter(in = ParameterIn.QUERY, description = "referenced handle" ,schema=@Schema()) @Valid @RequestParam(value = "handle", required = false) String handle) {
      try {
        InputStream stream = registryService.getLogo(amigoId, handle);
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
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    public ResponseEntity<AmigoMessage> getMessage(@Parameter(in = ParameterIn.QUERY, description = "referenced id" ,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = false) String amigoId,@Parameter(in = ParameterIn.QUERY, description = "referenced handle" ,schema=@Schema()) @Valid @RequestParam(value = "handle", required = false) String handle) {
      try {
        AmigoMessage msg = registryService.getMessage(amigoId, handle);
        return new ResponseEntity<AmigoMessage>(msg, HttpStatus.OK);
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

    public ResponseEntity<String> getName(@Parameter(in = ParameterIn.QUERY, description = "referenced id" ,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = false) String amigoId,@Parameter(in = ParameterIn.QUERY, description = "referenced handle" ,schema=@Schema()) @Valid @RequestParam(value = "handle", required = false) String handle) {
      try {
        String name = registryService.getName(amigoId, handle);
        return new ResponseEntity<String>(name, HttpStatus.OK);
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

    public ResponseEntity<Integer> getRegistryRevision(@Parameter(in = ParameterIn.QUERY, description = "referenced id" ,schema=@Schema()) @Valid @RequestParam(value = "amigoId", required = false) String amigoId,@Parameter(in = ParameterIn.QUERY, description = "referenced handle" ,schema=@Schema()) @Valid @RequestParam(value = "handle", required = false) String handle) {
      try {
        Integer revision = registryService.getRevision(amigoId, handle);
        return new ResponseEntity<Integer>(revision, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

