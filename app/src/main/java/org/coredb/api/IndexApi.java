/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.22).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.api;

import org.coredb.model.Amigo;
import org.coredb.model.AmigoEntry;
import org.coredb.model.AmigoMessage;
import org.coredb.model.AmigoView;
import org.coredb.model.LabelEntry;
import org.coredb.model.PendingAmigo;
import org.coredb.model.PendingAmigoView;
import org.coredb.model.ShareMessage;
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
import org.springframework.core.io.InputStreamResource;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@Api(value = "index", description = "the index API")
public interface IndexApi {

    @ApiOperation(value = "", nickname = "addAmigo", notes = "Add a new amigo", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<AmigoEntry> addAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "message describing amigo"  )  @Valid @RequestBody AmigoMessage body
);


    @ApiOperation(value = "", nickname = "addAmigoLabel", notes = "Associate amigo and label", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class) })
    @RequestMapping(value = "/index/amigos/{amigoId}/labels/{labelId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<AmigoEntry> addAmigoLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "referenced label entry",required=true) @PathVariable("labelId") String labelId
,@ApiParam(value = "referenced amigo entry",required=true) @PathVariable("amigoId") String amigoId
);

    @ApiOperation(value = "", nickname = "setAmigoLabels", notes = "Set label association", response = AmigoEntry.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 404, message = "amigo not found"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/index/amigos/{amigoId}/labels",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<AmigoEntry> setAmigoLabels(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @ApiParam(value = "referenced amigo entry",required=true) @PathVariable("amigoId") String amigoId, @ApiParam(value = ""  )  @Valid @RequestBody List<String> body);


    @ApiOperation(value = "", nickname = "addAmigoReject", notes = "Add entry to blocked amigo list", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/index/rejects/{amigoId}",
        method = RequestMethod.POST)
    ResponseEntity<Void> addAmigoReject(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "getAmigo", notes = "Retrieve specified attribute", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<AmigoEntry> getAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "getAmigoIdentity", notes = "Retrieve specified attribute", response = Amigo.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Amigo.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}/identity",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Amigo> getAmigoIdentity(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "getAmigoLogo", notes = "Retrieve specified asset", response = InputStreamResource.class, tags={ "show", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response = InputStreamResource.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "token or id not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}/logo",
        produces = { "application/octet-stream" },
        method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getAmigoLogo(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "getAmigoRejects", notes = "Retieve blocked amigo list", response = String.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = String.class, responseContainer = "List") })
    @RequestMapping(value = "/index/rejects",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getAmigoRejects(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getAmigoRequests", notes = "Retrieve list of unregistered amigos requested to share with", response = PendingAmigoView.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = PendingAmigoView.class, responseContainer = "List") })
    @RequestMapping(value = "/index/requests",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<PendingAmigoView>> getAmigoRequests(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getAmigoRevision", notes = "Retrieve specified amigo revision", response = Integer.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Integer.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}/revision",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> getAmigoRevision(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "getAmigos", notes = "Retrieve amigo data", response = AmigoEntry.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/index/amigos",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AmigoEntry>> getAmigos(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getIndexLabels", notes = "Retrieve list of labels (functionally the same as /group/labels)", response = LabelEntry.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = LabelEntry.class, responseContainer = "List") })
    @RequestMapping(value = "/index/labels",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<LabelEntry>> getIndexLabels(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getIndexRevision", notes = "Retrieve revision of index module", response = Integer.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Integer.class),
        @ApiResponse(code = 403, message = "access denied") })
    @RequestMapping(value = "/index/revision",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> getIndexRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getPendingRequest", notes = "Retrieve unregistered amigo requested to share with", response = PendingAmigo.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = PendingAmigo.class) })
    @RequestMapping(value = "/index/requests/{shareId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<PendingAmigo> getPendingRequest(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced request",required=true) @PathVariable("shareId") String shareId
);


    @ApiOperation(value = "", nickname = "removeAmigo", notes = "Remove specified amigo", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "removeAmigoLabel", notes = "Delete amigo from label", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class) })
    @RequestMapping(value = "/index/amigos/{amigoId}/labels/{labelId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<AmigoEntry> removeAmigoLabel(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of label entry",required=true) @PathVariable("labelId") String labelId
,@ApiParam(value = "id of amigo entry",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "removeAmigoNotes", notes = "Remove specified amigo notes", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}/notes",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<AmigoEntry> removeAmigoNotes(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "removeAmigoReject", notes = "Delete entry from blocked amigo list", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/index/rejects/{amigoId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeAmigoReject(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced amigo",required=true) @PathVariable("amigoId") String amigoId
);


    @ApiOperation(value = "", nickname = "removeAmigoRequest", notes = "Delete amigo request entry", response = ShareMessage.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = ShareMessage.class) })
    @RequestMapping(value = "/index/requests/{shareId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<ShareMessage> removeAmigoRequest(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of referenced request",required=true) @PathVariable("shareId") String shareId
,@ApiParam(value = "add to reject list") @Valid @RequestParam(value = "reject", required = false) Boolean reject
);


    @ApiOperation(value = "", nickname = "updateAmigo", notes = "Update amigo public data", response = Amigo.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Amigo.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<Amigo> updateAmigo(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "message describing amigo"  )  @Valid @RequestBody AmigoMessage body
);


    @ApiOperation(value = "", nickname = "updateAmigoNotes", notes = "Update specified amigo notes", response = AmigoEntry.class, tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/index/amigos/{amigoId}/notes",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<AmigoEntry> updateAmigoNotes(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of amigo",required=true) @PathVariable("amigoId") String amigoId
,@ApiParam(value = "notes for amigo"  )  @Valid @RequestBody String body
);


    @ApiOperation(value = "", nickname = "viewAmigos", notes = "Retrieve amigo label association", response = AmigoView.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = AmigoView.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/index/amigos/view",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AmigoView>> viewAmigos(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);

}


