/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.21).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.coredb.api;

import org.coredb.model.LabelEntry;
import org.springframework.core.io.Resource;
import org.coredb.model.StoreStatus;
import org.coredb.model.SubjectAsset;
import org.coredb.model.Asset;
import org.coredb.model.SubjectTag;
import org.coredb.model.SubjectEntry;
import org.coredb.model.SubjectView;
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
@Api(value = "show", description = "the show API")
public interface ShowApi {



    @ApiOperation(value = "", nickname = "addSubject", notes = "Add a new subject", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<SubjectEntry> addSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "schema of subject") @Valid @RequestParam(value = "schema", required = false) String schema
);


    @ApiOperation(value = "", nickname = "addSubjectAsset", notes = "Add asset to subject", response = Asset.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Asset.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/assets",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<Asset> addSubjectAsset(@ApiParam(value = "new asset entry" ,required=true ) @Valid @RequestParam("file") MultipartFile file,@NotNull @ApiParam(value = "share token with Service service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "asset transformations 0. BIN pass through 1. MP4 -vf scale=720:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 2. MP4 -vf scale=1280:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 3. MP4 -vf scale=1920:-1 -vcodec libx264 -crf 27 -preset veryfast -acodec aac 4. MP3 -codec:a libmp3lame -qscale:a 6 5. MP3 -codec:a libmp3lame -qscale:a 2 6. MP3 -codec:a libmp3lame -qscale:a 0 7. JPG -resize 1280x720\\> 8. JPG -resize 2688x1520\\> 9. JPG -resize 3840x2160\\>", required = true) @Valid @RequestParam(value = "transforms", required = true) List<String> transforms
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
);


    @ApiOperation(value = "", nickname = "addSubjectLabel", notes = "Assign a label to a subject entry", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 404, message = "attribute not found"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/labels/{labelId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<SubjectEntry> addSubjectLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of attribute",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
);

    @ApiOperation(value = "", nickname = "setSubjectLabels", notes = "Set label association", response = SubjectEntry.class, responseContainer = "List", tags={ "index", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 404, message = "subject not found"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/labels",
        produces = { "application/json" },
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<SubjectEntry> setSubjectLabels(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token, @ApiParam(value = "referenced subject entry",required=true) @PathVariable("subjectId") String subjectId, @ApiParam(value = ""  )  @Valid @RequestBody List<String> body);


    @ApiOperation(value = "", nickname = "clearSubjectLabel", notes = "Remove label assignment from subject entry", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 404, message = "attribute not found"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/labels/{labelId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<SubjectEntry> clearSubjectLabel(@NotNull @ApiParam(value = "share token with service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of label",required=true) @PathVariable("labelId") String labelId
);


    @ApiOperation(value = "", nickname = "filterSubjects", notes = "Retrieve filtered set of subjects", response = SubjectEntry.class, responseContainer = "List", tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/filter",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<List<SubjectEntry>> filterSubjects(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "show editing subjects") @Valid @RequestParam(value = "editing", required = false) Boolean editing
,@ApiParam(value = "show pending subjects") @Valid @RequestParam(value = "pending", required = false) Boolean pending
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
);


    @ApiOperation(value = "", nickname = "getShowLabels", notes = "Retrieve list of labels. (functionally the same as /group/labels)", response = LabelEntry.class, responseContainer = "List", tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = LabelEntry.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/labels",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<LabelEntry>> getShowLabels(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getShowRevision", notes = "Retrieve revision of show module", response = Integer.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Integer.class),
        @ApiResponse(code = 403, message = "access denied") })
    @RequestMapping(value = "/show/revision",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> getShowRevision(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getShowStatus", notes = "Retrieve store status", response = StoreStatus.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = StoreStatus.class),
        @ApiResponse(code = 403, message = "access denied") })
    @RequestMapping(value = "/show/status",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<StoreStatus> getShowStatus(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
);


    @ApiOperation(value = "", nickname = "getSubject", notes = "Retrieve specified subject", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<SubjectEntry> getSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
);


    @ApiOperation(value = "", nickname = "getSubjectAsset", notes = "Retrieve specified asset", response = InputStreamResource.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InputStreamResource.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "token or id not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/assets/{assetId}",
        produces = { "application/octet-stream" }, 
        method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getSubjectAsset(@NotNull @ApiParam(value = "share token for service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of asset",required=true) @PathVariable("assetId") String assetId
);


    @ApiOperation(value = "", nickname = "removeSubject", notes = "Remove specified subject", tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation"),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> removeSubject(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
);


    @ApiOperation(value = "", nickname = "removeSubjectAsset", notes = "Delete asset from subject", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/assets/{assetId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<SubjectEntry> removeSubjectAsset(@NotNull @ApiParam(value = "service access token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of asset",required=true) @PathVariable("assetId") String assetId
);


    @ApiOperation(value = "", nickname = "updateSubjectAccess", notes = "Update specified subject data", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/access",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<SubjectEntry> updateSubjectAccess(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@NotNull @ApiParam(value = "set if subject is to be shared", required = true) @Valid @RequestParam(value = "share", required = true) Boolean share
);


    @ApiOperation(value = "", nickname = "updateSubjectData", notes = "Update specified subject data", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/data",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<SubjectEntry> updateSubjectData(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@NotNull @ApiParam(value = "schema of subject", required = true) @Valid @RequestParam(value = "schema", required = true) String schema
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "payload of subject"  )  @Valid @RequestBody String body
);


    @ApiOperation(value = "", nickname = "updateSubjectExpire", notes = "Update specified subject expiration", response = SubjectEntry.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectEntry.class),
        @ApiResponse(code = 403, message = "access denied"),
        @ApiResponse(code = 404, message = "not found") })
    @RequestMapping(value = "/show/subjects/{subjectId}/expire",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<SubjectEntry> updateSubjectExpire(@NotNull @ApiParam(value = "share service token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "set if subject is to expire") @Valid @RequestParam(value = "expire", required = false) Long expire
);


    @ApiOperation(value = "", nickname = "viewSubjects", notes = "Retrieve subject label association", response = SubjectView.class, responseContainer = "List", tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectView.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/view",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<List<SubjectView>> viewSubjects(@NotNull @ApiParam(value = "share token", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = ""  )  @Valid @RequestBody List<String> body
);


    @ApiOperation(value = "", nickname = "getSubjectTags", notes = "Retrieve tags attached to specified subject", response = SubjectTag.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectTag.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/tags",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<SubjectTag> getSubjectTags(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
);


    @ApiOperation(value = "", nickname = "addSubjectTag", notes = "Retrieve tags attached to specified subject", response = SubjectTag.class, tags={ "show", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = SubjectTag.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/tags",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<SubjectTag> addSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "type of tags to add") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
,@ApiParam(value = "stringified data object"  )  @Valid @RequestBody String body
);


    @ApiOperation(value = "", nickname = "removeSubjectTag", notes = "Remove tag attached to subject", response = SubjectTag.class, tags={ "show", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response = SubjectTag.class),
        @ApiResponse(code = 401, message = "unknown token"),
        @ApiResponse(code = 404, message = "tag not found"),
        @ApiResponse(code = 405, message = "permission not granted for token"),
        @ApiResponse(code = 423, message = "account locked"),
        @ApiResponse(code = 500, message = "internal server error") })
    @RequestMapping(value = "/show/subjects/{subjectId}/tags/{tagId}",
        produces = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<SubjectTag> removeSubjectTag(@NotNull @ApiParam(value = "token assigned to service", required = true) @Valid @RequestParam(value = "token", required = true) String token
,@ApiParam(value = "id of subject",required=true) @PathVariable("subjectId") String subjectId
,@ApiParam(value = "id of tag",required=true) @PathVariable("tagId") String tagId
,@ApiParam(value = "type of tags to retrieve") @Valid @RequestParam(value = "schema", required = false) String schema
,@ApiParam(value = "order by earliest first") @Valid @RequestParam(value = "descending", required = false) Boolean descending
);

}


