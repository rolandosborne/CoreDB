/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.25).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.api;

import org.coredb.model.Auth;
import org.coredb.model.AuthMessage;
import org.coredb.model.Revisions;
import org.coredb.model.ServiceAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@Validated
public interface TokenApi {

    @Operation(summary = "", description = "Get get token access", tags={ "token" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ServiceAccess.class))),
        
        @ApiResponse(responseCode = "401", description = "unknown token"),
        
        @ApiResponse(responseCode = "405", description = "permission not granted for token"),
        
        @ApiResponse(responseCode = "500", description = "internal server error") })
    @RequestMapping(value = "/token/access",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ServiceAccess> getAccess(@NotNull @Parameter(in = ParameterIn.QUERY, description = "service access token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token);


    @Operation(summary = "", description = "Set agent authorization for token", tags={ "token" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Auth.class))) })
    @RequestMapping(value = "/token/agent",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Auth> setAgentMessage(@NotNull @Parameter(in = ParameterIn.QUERY, description = "share token with access" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @Parameter(in = ParameterIn.DEFAULT, description = "new share request", schema=@Schema()) @Valid @RequestBody AuthMessage body);


    @Operation(summary = "", description = "Get revision of accessible modules", tags={ "token" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Revisions.class))),
        
        @ApiResponse(responseCode = "401", description = "unknown token"),
        
        @ApiResponse(responseCode = "500", description = "internal server error") })
    @RequestMapping(value = "/token/revisions",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Revisions> getRevisions(@NotNull @Parameter(in = ParameterIn.QUERY, description = "service access token" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "token", required = true) String token, @Parameter(in = ParameterIn.QUERY, description = "token from delivered auth message if from contact" ,schema=@Schema()) @Valid @RequestParam(value = "agent", required = false) String agent);

}


