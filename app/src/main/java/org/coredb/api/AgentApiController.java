package org.coredb.api;

import org.coredb.model.AuthMessage;
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

import org.coredb.service.AgentService;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
public class AgentApiController implements AgentApi {

    private static final Logger log = LoggerFactory.getLogger(AgentApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    private AgentService agentService;

    @org.springframework.beans.factory.annotation.Autowired
    public AgentApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AuthMessage> getAgentMessage(@NotNull @ApiParam(value = "share token with access", required = true) @Valid @RequestParam(value = "token", required = true) String token) {
    try {
        AuthMessage message = agentService.setUserAccess(token);
        return new ResponseEntity<AuthMessage>(message, HttpStatus.OK);
      }
      catch(IllegalArgumentException e) {
        log.error(e.toString());
        return new ResponseEntity<AuthMessage>(HttpStatus.BAD_REQUEST);
      }
      catch(NotFoundException e) {
        log.error(e.toString());
        return new ResponseEntity<AuthMessage>(HttpStatus.NOT_FOUND);
      }
      catch(IllegalStateException e) {
        log.error(e.toString());
        return new ResponseEntity<AuthMessage>(HttpStatus.TOO_MANY_REQUESTS);
      }
      catch(Exception e) {
        log.error(e.toString());
        return new ResponseEntity<AuthMessage>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}

