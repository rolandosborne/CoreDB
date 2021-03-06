/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.api;

import org.coredb.model.AlertEntry;
import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
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
@Api(value = "account", description = "the account API")
public interface AccountApi {

    @ApiOperation(value = "", nickname = "getAlerts", notes = "Get registered system events", response = AlertEntry.class, responseContainer = "List", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AlertEntry.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "key not found") })
    @RequestMapping(value = "/account/alerts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AlertEntry>> getAlerts(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getConfig", notes = "Get config value for specified key", response = ConfigEntry.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = ConfigEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "key not found") })
    @RequestMapping(value = "/account/configs/{configId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ConfigEntry> getConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
);


    @ApiOperation(value = "", nickname = "getConfigs", notes = "Retrieve configs set for account", response = ConfigEntry.class, responseContainer = "List", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = ConfigEntry.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = "access denied") })
    @RequestMapping(value = "/account/configs",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<ConfigEntry>> getConfigs(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "removeAlert", notes = "Delete specified system event", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/account/alert/{alertId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeAlert(@NotNull @ApiParam(value = "shared token with app", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of alert entry",required=true) @PathVariable("alertId") String alertId
);


    @ApiOperation(value = "", nickname = "removeConfig", notes = "Restore default value for specified config key", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "key not found") })
    @RequestMapping(value = "/account/configs/{configId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
);


    @ApiOperation(value = "Set config value to specified value", nickname = "setConfig", notes = "Set config value to specified value in request payload", response = ConfigEntry.class, tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = ConfigEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "key not found") })
    @RequestMapping(value = "/account/configs/{configId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<ConfigEntry> setConfig(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of config entry",required=true) @PathVariable("configId") String configId
,@ApiParam(value = "new value for config key" ,required=true )  @Valid @RequestBody Config body
);

}

