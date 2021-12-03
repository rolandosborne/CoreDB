package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.model.ServiceAccess;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AttachLink
 */
@Validated


public class AttachLink   {
  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("access")
  private ServiceAccess access = null;

  @JsonProperty("expires")
  private Long expires = null;

  @JsonProperty("issued")
  private Long issued = null;

  public AttachLink amigoId(String amigoId) {
    this.amigoId = amigoId;
    return this;
  }

  /**
   * Get amigoId
   * @return amigoId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getAmigoId() {
    return amigoId;
  }

  public void setAmigoId(String amigoId) {
    this.amigoId = amigoId;
  }

  public AttachLink access(ServiceAccess access) {
    this.access = access;
    return this;
  }

  /**
   * Get access
   * @return access
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public ServiceAccess getAccess() {
    return access;
  }

  public void setAccess(ServiceAccess access) {
    this.access = access;
  }

  public AttachLink expires(Long expires) {
    this.expires = expires;
    return this;
  }

  /**
   * Get expires
   * @return expires
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

  public AttachLink issued(Long issued) {
    this.issued = issued;
    return this;
  }

  /**
   * Get issued
   * @return issued
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public Long getIssued() {
    return issued;
  }

  public void setIssued(Long issued) {
    this.issued = issued;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachLink attachLink = (AttachLink) o;
    return Objects.equals(this.amigoId, attachLink.amigoId) &&
        Objects.equals(this.access, attachLink.access) &&
        Objects.equals(this.expires, attachLink.expires) &&
        Objects.equals(this.issued, attachLink.issued);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigoId, access, expires, issued);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachLink {\n");
    
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    access: ").append(toIndentedString(access)).append("\n");
    sb.append("    expires: ").append(toIndentedString(expires)).append("\n");
    sb.append("    issued: ").append(toIndentedString(issued)).append("\n");
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
