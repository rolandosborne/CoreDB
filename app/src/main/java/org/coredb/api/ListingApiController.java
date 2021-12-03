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

import java.security.InvalidParameterException;
import java.lang.UnsupportedOperationException;
import org.coredb.api.NotFoundException;
import org.coredb.service.ContactService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ListingApiController implements ListingApi {

    private static final Logger log = LoggerFactory.getLogger(ListingApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private ContactService contactService;

    @org.springframework.beans.factory.annotation.Autowired
    public ListingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Amigo> getListingIdentity(@NotNull @Parameter(in = ParameterIn.QUERY, description = "service access token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token,@Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent) {
      try {
        Amigo amigo = contactService.getIdentity(token, agent);
        return new ResponseEntity<Amigo>(amigo, HttpStatus.OK);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.NOT_FOUND);
      }
      catch(UnsupportedOperationException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.PAYMENT_REQUIRED);
      }
      catch(InvalidParameterException e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.UNAUTHORIZED);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<Amigo>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

