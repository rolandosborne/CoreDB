package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Tag
 */
@Validated


public class Tag   {
  @JsonProperty("tagId")
  private String tagId = null;

  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("amigoName")
  private String amigoName = null;

  @JsonProperty("amigoRegistry")
  private String amigoRegistry = null;

  @JsonProperty("created")
  private Long created = null;

  @JsonProperty("schema")
  private String schema = null;

  @JsonProperty("data")
  private String data = null;

  public Tag tagId(String tagId) {
    this.tagId = tagId;
    return this;
  }

  /**
   * Get tagId
   * @return tagId
   **/
      @NotNull

    public String getTagId() {
    return tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public Tag amigoId(String amigoId) {
    this.amigoId = amigoId;
    return this;
  }

  /**
   * Get amigoId
   * @return amigoId
   **/
      @NotNull

    public String getAmigoId() {
    return amigoId;
  }

  public void setAmigoId(String amigoId) {
    this.amigoId = amigoId;
  }

  public Tag amigoName(String amigoName) {
    this.amigoName = amigoName;
    return this;
  }

  /**
   * Get amigoName
   * @return amigoName
   **/

    public String getAmigoName() {
    return amigoName;
  }

  public void setAmigoName(String amigoName) {
    this.amigoName = amigoName;
  }

  public Tag amigoRegistry(String amigoRegistry) {
    this.amigoRegistry = amigoRegistry;
    return this;
  }

  /**
   * Get amigoRegistry
   * @return amigoRegistry
   **/

    public String getAmigoRegistry() {
    return amigoRegistry;
  }

  public void setAmigoRegistry(String amigoRegistry) {
    this.amigoRegistry = amigoRegistry;
  }

  public Tag created(Long created) {
    this.created = created;
    return this;
  }

  /**
   * Get created
   * @return created
   **/
      @NotNull

    public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public Tag schema(String schema) {
    this.schema = schema;
    return this;
  }

  /**
   * Get schema
   * @return schema
   **/
      @NotNull

    public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public Tag data(String data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
   **/
      @NotNull

    public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Tag tag = (Tag) o;
    return Objects.equals(this.tagId, tag.tagId) &&
        Objects.equals(this.amigoId, tag.amigoId) &&
        Objects.equals(this.amigoName, tag.amigoName) &&
        Objects.equals(this.amigoRegistry, tag.amigoRegistry) &&
        Objects.equals(this.created, tag.created) &&
        Objects.equals(this.schema, tag.schema) &&
        Objects.equals(this.data, tag.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagId, amigoId, amigoName, amigoRegistry, created, schema, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Tag {\n");
    
    sb.append("    tagId: ").append(toIndentedString(tagId)).append("\n");
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    amigoName: ").append(toIndentedString(amigoName)).append("\n");
    sb.append("    amigoRegistry: ").append(toIndentedString(amigoRegistry)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

