package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Blurb
 */
@Validated


public class Blurb   {
  @JsonProperty("blurbId")
  private String blurbId = null;

  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("schema")
  private String schema = null;

  @JsonProperty("data")
  private String data = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("created")
  private Integer created = null;

  @JsonProperty("updated")
  private Integer updated = null;

  public Blurb blurbId(String blurbId) {
    this.blurbId = blurbId;
    return this;
  }

  /**
   * Get blurbId
   * @return blurbId
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getBlurbId() {
    return blurbId;
  }

  public void setBlurbId(String blurbId) {
    this.blurbId = blurbId;
  }

  public Blurb amigoId(String amigoId) {
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

  public Blurb schema(String schema) {
    this.schema = schema;
    return this;
  }

  /**
   * Get schema
   * @return schema
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public Blurb data(String data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Blurb revision(Integer revision) {
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

  public Blurb created(Integer created) {
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

  public Blurb updated(Integer updated) {
    this.updated = updated;
    return this;
  }

  /**
   * Get updated
   * @return updated
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getUpdated() {
    return updated;
  }

  public void setUpdated(Integer updated) {
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
    Blurb blurb = (Blurb) o;
    return Objects.equals(this.blurbId, blurb.blurbId) &&
        Objects.equals(this.amigoId, blurb.amigoId) &&
        Objects.equals(this.schema, blurb.schema) &&
        Objects.equals(this.data, blurb.data) &&
        Objects.equals(this.revision, blurb.revision) &&
        Objects.equals(this.created, blurb.created) &&
        Objects.equals(this.updated, blurb.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blurbId, amigoId, schema, data, revision, created, updated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Blurb {\n");
    
    sb.append("    blurbId: ").append(toIndentedString(blurbId)).append("\n");
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
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

