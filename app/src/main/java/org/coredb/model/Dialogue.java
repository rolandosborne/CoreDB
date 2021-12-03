package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Dialogue
 */
@Validated


public class Dialogue   {
  @JsonProperty("dialogueId")
  private String dialogueId = null;

  @JsonProperty("created")
  private Integer created = null;

  @JsonProperty("modified")
  private Integer modified = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("linked")
  private Boolean linked = null;

  @JsonProperty("synced")
  private Boolean synced = null;

  @JsonProperty("amigoId")
  private String amigoId = null;

  public Dialogue dialogueId(String dialogueId) {
    this.dialogueId = dialogueId;
    return this;
  }

  /**
   * Get dialogueId
   * @return dialogueId
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getDialogueId() {
    return dialogueId;
  }

  public void setDialogueId(String dialogueId) {
    this.dialogueId = dialogueId;
  }

  public Dialogue created(Integer created) {
    this.created = created;
    return this;
  }

  /**
   * Get created
   * @return created
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getCreated() {
    return created;
  }

  public void setCreated(Integer created) {
    this.created = created;
  }

  public Dialogue modified(Integer modified) {
    this.modified = modified;
    return this;
  }

  /**
   * Get modified
   * @return modified
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getModified() {
    return modified;
  }

  public void setModified(Integer modified) {
    this.modified = modified;
  }

  public Dialogue revision(Integer revision) {
    this.revision = revision;
    return this;
  }

  /**
   * Get revision
   * @return revision
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getRevision() {
    return revision;
  }

  public void setRevision(Integer revision) {
    this.revision = revision;
  }

  public Dialogue active(Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Dialogue linked(Boolean linked) {
    this.linked = linked;
    return this;
  }

  /**
   * Get linked
   * @return linked
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Boolean isLinked() {
    return linked;
  }

  public void setLinked(Boolean linked) {
    this.linked = linked;
  }

  public Dialogue synced(Boolean synced) {
    this.synced = synced;
    return this;
  }

  /**
   * Get synced
   * @return synced
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Boolean isSynced() {
    return synced;
  }

  public void setSynced(Boolean synced) {
    this.synced = synced;
  }

  public Dialogue amigoId(String amigoId) {
    this.amigoId = amigoId;
    return this;
  }

  /**
   * Get amigoId
   * @return amigoId
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getAmigoId() {
    return amigoId;
  }

  public void setAmigoId(String amigoId) {
    this.amigoId = amigoId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dialogue dialogue = (Dialogue) o;
    return Objects.equals(this.dialogueId, dialogue.dialogueId) &&
        Objects.equals(this.created, dialogue.created) &&
        Objects.equals(this.modified, dialogue.modified) &&
        Objects.equals(this.revision, dialogue.revision) &&
        Objects.equals(this.active, dialogue.active) &&
        Objects.equals(this.linked, dialogue.linked) &&
        Objects.equals(this.synced, dialogue.synced) &&
        Objects.equals(this.amigoId, dialogue.amigoId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dialogueId, created, modified, revision, active, linked, synced, amigoId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dialogue {\n");
    
    sb.append("    dialogueId: ").append(toIndentedString(dialogueId)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    linked: ").append(toIndentedString(linked)).append("\n");
    sb.append("    synced: ").append(toIndentedString(synced)).append("\n");
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

