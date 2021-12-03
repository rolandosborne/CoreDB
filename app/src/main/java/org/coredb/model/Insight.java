package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Insight
 */
@Validated


public class Insight   {
  @JsonProperty("dialogueId")
  private String dialogueId = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("amigoRegistry")
  private String amigoRegistry = null;

  public Insight dialogueId(String dialogueId) {
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

  public Insight revision(Integer revision) {
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

  public Insight amigoId(String amigoId) {
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

  public Insight amigoRegistry(String amigoRegistry) {
    this.amigoRegistry = amigoRegistry;
    return this;
  }

  /**
   * Get amigoRegistry
   * @return amigoRegistry
   **/
  @Schema(description = "")
  
    public String getAmigoRegistry() {
    return amigoRegistry;
  }

  public void setAmigoRegistry(String amigoRegistry) {
    this.amigoRegistry = amigoRegistry;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Insight insight = (Insight) o;
    return Objects.equals(this.dialogueId, insight.dialogueId) &&
        Objects.equals(this.revision, insight.revision) &&
        Objects.equals(this.amigoId, insight.amigoId) &&
        Objects.equals(this.amigoRegistry, insight.amigoRegistry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dialogueId, revision, amigoId, amigoRegistry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Insight {\n");
    
    sb.append("    dialogueId: ").append(toIndentedString(dialogueId)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    amigoRegistry: ").append(toIndentedString(amigoRegistry)).append("\n");
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

