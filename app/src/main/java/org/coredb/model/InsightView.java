package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InsightView
 */
@Validated


public class InsightView   {
  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("dialogueId")
  private String dialogueId = null;

  @JsonProperty("revision")
  private Integer revision = null;

  public InsightView amigoId(String amigoId) {
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

  public InsightView dialogueId(String dialogueId) {
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

  public InsightView revision(Integer revision) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsightView insightView = (InsightView) o;
    return Objects.equals(this.amigoId, insightView.amigoId) &&
        Objects.equals(this.dialogueId, insightView.dialogueId) &&
        Objects.equals(this.revision, insightView.revision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigoId, dialogueId, revision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsightView {\n");
    
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    dialogueId: ").append(toIndentedString(dialogueId)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
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

