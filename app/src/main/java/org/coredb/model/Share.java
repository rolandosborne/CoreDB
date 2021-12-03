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
 * Share
 */
@Validated


public class Share   {
  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    OPEN("open"),
    
    CLOSE("close");

    private String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionEnum fromValue(String text) {
      for (ActionEnum b : ActionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("action")
  private ActionEnum action = null;

  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("token")
  private String token = null;

  @JsonProperty("expires")
  private Long expires = null;

  @JsonProperty("issued")
  private Long issued = null;

  public Share action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public Share amigoId(String amigoId) {
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

  public Share token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
  **/
  @ApiModelProperty(value = "")
  
    public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Share expires(Long expires) {
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

  public Share issued(Long issued) {
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
    Share share = (Share) o;
    return Objects.equals(this.action, share.action) &&
        Objects.equals(this.amigoId, share.amigoId) &&
        Objects.equals(this.token, share.token) &&
        Objects.equals(this.expires, share.expires) &&
        Objects.equals(this.issued, share.issued);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, amigoId, token, expires, issued);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Share {\n");
    
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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
