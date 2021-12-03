package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Token
 */
@Validated


public class Token   {
  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("token")
  private String token = null;

  @JsonProperty("issued")
  private Long issued = null;

  @JsonProperty("expires")
  private Long expires = null;

  public Token amigoId(String amigoId) {
    this.amigoId = amigoId;
    return this;
  }

  /**
   * Get amigoId
   * @return amigoId
  **/
  @ApiModelProperty(value = "")
  
    public String getAmigoId() {
    return amigoId;
  }

  public void setAmigoId(String amigoId) {
    this.amigoId = amigoId;
  }

  public Token token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Token issued(Long issued) {
    this.issued = issued;
    return this;
  }

  /**
   * Get issued
   * @return issued
  **/
  @ApiModelProperty(value = "")
  
    public Long getIssued() {
    return issued;
  }

  public void setIssued(Long issued) {
    this.issued = issued;
  }

  public Token expires(Long expires) {
    this.expires = expires;
    return this;
  }

  /**
   * Get expires
   * @return expires
  **/
  @ApiModelProperty(value = "")
  
    public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Token token = (Token) o;
    return Objects.equals(this.amigoId, token.amigoId) &&
        Objects.equals(this.token, token.token) &&
        Objects.equals(this.issued, token.issued) &&
        Objects.equals(this.expires, token.expires);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigoId, token, issued, expires);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Token {\n");
    
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    issued: ").append(toIndentedString(issued)).append("\n");
    sb.append("    expires: ").append(toIndentedString(expires)).append("\n");
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

