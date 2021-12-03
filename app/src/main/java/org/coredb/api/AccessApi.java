/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.api;

import org.coredb.model.AmigoToken;
import org.coredb.model.LinkMessage;
import org.coredb.model.Pass;
import org.coredb.model.ServiceAccess;
import org.coredb.model.UserEntry;
import io.swagger.annotations.*;
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
@Api(value = "access", description = "the access API")
public interface AccessApi {

    @ApiOperation(value = "", nickname = "assignAccount", notes = "Register created user account back in service account", response = UserEntry.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = UserEntry.class),
        @ApiResponse(code = 400, message = "invalid amigo message"),
        @ApiResponse(code = 401, message = "unknow token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 406, message = "account limit reached"),
        @ApiResponse(code = 423, message = "account or service is disabled"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/services/tokens",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserEntry> assignAccount(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody AmigoToken body
);


    @ApiOperation(value = "", nickname = "attachAccount", notes = "Attach authorized service to accound specified by id", response = AmigoToken.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoToken.class),
        @ApiResponse(code = 401, message = "unknow token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 406, message = "service limit reached"),
        @ApiResponse(code = 423, message = "account or service is disabled"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/accounts/attached",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<AmigoToken> attachAccount(@NotNull @ApiParam(value = "id of referenced account", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@NotNull @ApiParam(value = "pass token to attaching services", required = true) @Valid @RequestParam(value = "pass", required = true) String pass
,@ApiParam(value = ""  )  @Valid @RequestBody LinkMessage body
);


    @ApiOperation(value = "", nickname = "authorizeAttach", notes = "Generate an authorization message for requesting access to a specified account by the specified service.", response = LinkMessage.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = LinkMessage.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 406, message = "account limit reached"),
        @ApiResponse(code = 423, message = "account or service is disabled"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/services/attached",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<LinkMessage> authorizeAttach(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "id of account to attach", required = true) @Valid @RequestParam(value = "amigoId", required = true) String amigoId
,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body
);


    @ApiOperation(value = "", nickname = "authorizeCreate", notes = "Generate an authorization message for creating a new account on behalf of specified service account and granting the specified access to that service.", response = LinkMessage.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = LinkMessage.class),
        @ApiResponse(code = 401, message = "unknow token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 406, message = "account limit reached"),
        @ApiResponse(code = 423, message = "account or service is disabled"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/services/created",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<LinkMessage> authorizeCreate(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body
);


    @ApiOperation(value = "", nickname = "createAccount", notes = "Create a new account authorized by a service", response = AmigoToken.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoToken.class),
        @ApiResponse(code = 400, message = "invalid link message"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 406, message = "account limit reached"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/accounts/created",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<AmigoToken> createAccount(@NotNull @ApiParam(value = "admin token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody LinkMessage body
);


    @ApiOperation(value = "", nickname = "createAmigo", notes = "Create an independent account or service", response = AmigoToken.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoToken.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 406, message = "account limit reached"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/amigos",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<AmigoToken> createAmigo(@NotNull @ApiParam(value = "admin token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "generatePass", notes = "Create a single use token to allow attachment of service", response = Pass.class, tags={ "access", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Pass.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account or service is disabled"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/access/accounts/tokens",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Pass> generatePass(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "expiration seconds for pass") @Valid @RequestParam(value = "expire", required = false) Integer expire
,@ApiParam(value = ""  )  @Valid @RequestBody ServiceAccess body
);

}


