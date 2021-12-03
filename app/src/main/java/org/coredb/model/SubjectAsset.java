package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SubjectAsset
 */
@Validated


public class SubjectAsset   {
  @JsonProperty("assetId")
  private String assetId = null;

  @JsonProperty("originalId")
  private String originalId = null;

  @JsonProperty("transform")
  private String transform = null;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    UPLOADING("uploading"),
    
    PENDING("pending"),
    
    PROCESSING("processing"),
    
    READY("ready"),
    
    FAILED("failed"),
    
    DELETED("deleted");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("state")
  private StateEnum state = null;

  @JsonProperty("size")
  private Long size = null;

  @JsonProperty("hash")
  private String hash = null;

  @JsonProperty("created")
  private Long created = null;

  public SubjectAsset assetId(String assetId) {
    this.assetId = assetId;
    return this;
  }

  /**
   * Get assetId
   * @return assetId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  public SubjectAsset originalId(String originalId) {
    this.originalId = originalId;
    return this;
  }

  /**
   * Get originalId
   * @return originalId
  **/
  @ApiModelProperty(value = "")
  
    public String getOriginalId() {
    return originalId;
  }

  public void setOriginalId(String originalId) {
    this.originalId = originalId;
  }

  public SubjectAsset transform(String transform) {
    this.transform = transform;
    return this;
  }

  /**
   * Get transform
   * @return transform
  **/
  @ApiModelProperty(value = "")
  
    public String getTransform() {
    return transform;
  }

  public void setTransform(String transform) {
    this.transform = transform;
  }

  public SubjectAsset state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  
    public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public SubjectAsset size(Long size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
  **/
  @ApiModelProperty(value = "")
  
    public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public SubjectAsset hash(String hash) {
    this.hash = hash;
    return this;
  }

  /**
   * Get hash
   * @return hash
  **/
  @ApiModelProperty(value = "")
  
    public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public SubjectAsset created(Long created) {
    this.created = created;
    return this;
  }

  /**
   * Get created
   * @return created
  **/
  @ApiModelProperty(value = "")
  
    public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubjectAsset subjectAsset = (SubjectAsset) o;
    return Objects.equals(this.assetId, subjectAsset.assetId) &&
        Objects.equals(this.originalId, subjectAsset.originalId) &&
        Objects.equals(this.transform, subjectAsset.transform) &&
        Objects.equals(this.state, subjectAsset.state) &&
        Objects.equals(this.size, subjectAsset.size) &&
        Objects.equals(this.hash, subjectAsset.hash) &&
        Objects.equals(this.created, subjectAsset.created);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assetId, originalId, transform, state, size, hash, created);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubjectAsset {\n");
    
    sb.append("    assetId: ").append(toIndentedString(assetId)).append("\n");
    sb.append("    originalId: ").append(toIndentedString(originalId)).append("\n");
    sb.append("    transform: ").append(toIndentedString(transform)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
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
