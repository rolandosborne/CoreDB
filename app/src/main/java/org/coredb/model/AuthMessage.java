package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.model.AmigoMessage;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AuthMessage
 */
@Validated


public class AuthMessage   {
  @JsonProperty("amigo")
  private AmigoMessage amigo = null;

  @JsonProperty("data")
  private String data = null;

  @JsonProperty("signature")
  private String signature = null;

  public AuthMessage amigo(AmigoMessage amigo) {
    this.amigo = amigo;
    return this;
  }

  /**
   * Get amigo
   * @return amigo
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public AmigoMessage getAmigo() {
    return amigo;
  }

  public void setAmigo(AmigoMessage amigo) {
    this.amigo = amigo;
  }

  public AuthMessage data(String data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public AuthMessage signature(String signature) {
    this.signature = signature;
    return this;
  }

  /**
   * Get signature
   * @return signature
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthMessage authMessage = (AuthMessage) o;
    return Objects.equals(this.amigo, authMessage.amigo) &&
        Objects.equals(this.data, authMessage.data) &&
        Objects.equals(this.signature, authMessage.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigo, data, signature);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthMessage {\n");
    
    sb.append("    amigo: ").append(toIndentedString(amigo)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
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
