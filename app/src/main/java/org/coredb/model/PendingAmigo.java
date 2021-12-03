package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.coredb.model.AmigoMessage;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PendingAmigo
 */
@Validated


public class PendingAmigo   {
  @JsonProperty("shareId")
  private String shareId = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("message")
  private AmigoMessage message = null;

  @JsonProperty("updated")
  private Long updated = null;

  public PendingAmigo shareId(String shareId) {
    this.shareId = shareId;
    return this;
  }

  /**
   * Get shareId
   * @return shareId
   **/
      @NotNull

    public String getShareId() {
    return shareId;
  }

  public void setShareId(String shareId) {
    this.shareId = shareId;
  }

  public PendingAmigo revision(Integer revision) {
    this.revision = revision;
    return this;
  }

  /**
   * Get revision
   * @return revision
   **/
      @NotNull

    public Integer getRevision() {
    return revision;
  }

  public void setRevision(Integer revision) {
    this.revision = revision;
  }

  public PendingAmigo message(AmigoMessage message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   **/
      @NotNull

    @Valid
    public AmigoMessage getMessage() {
    return message;
  }

  public void setMessage(AmigoMessage message) {
    this.message = message;
  }

  public PendingAmigo updated(Long updated) {
    this.updated = updated;
    return this;
  }

  /**
   * Get updated
   * @return updated
   **/
      @NotNull

    public Long getUpdated() {
    return updated;
  }

  public void setUpdated(Long updated) {
    this.updated = updated;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingAmigo pendingAmigo = (PendingAmigo) o;
    return Objects.equals(this.shareId, pendingAmigo.shareId) &&
        Objects.equals(this.revision, pendingAmigo.revision) &&
        Objects.equals(this.message, pendingAmigo.message) &&
        Objects.equals(this.updated, pendingAmigo.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shareId, revision, message, updated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingAmigo {\n");
    
    sb.append("    shareId: ").append(toIndentedString(shareId)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
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

