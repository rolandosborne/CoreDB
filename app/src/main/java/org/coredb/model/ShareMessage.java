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
 * ShareMessage
 */
@Validated


public class ShareMessage   {
  @JsonProperty("amigo")
  private AmigoMessage amigo = null;

  @JsonProperty("signature")
  private String signature = null;

  @JsonProperty("open")
  private String open = null;

  @JsonProperty("close")
  private String close = null;

  public ShareMessage amigo(AmigoMessage amigo) {
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

  public ShareMessage signature(String signature) {
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

  public ShareMessage open(String open) {
    this.open = open;
    return this;
  }

  /**
   * Get open
   * @return open
  **/
  @ApiModelProperty(value = "")
  
    public String getOpen() {
    return open;
  }

  public void setOpen(String open) {
    this.open = open;
  }

  public ShareMessage close(String close) {
    this.close = close;
    return this;
  }

  /**
   * Get close
   * @return close
  **/
  @ApiModelProperty(value = "")
  
    public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShareMessage shareMessage = (ShareMessage) o;
    return Objects.equals(this.amigo, shareMessage.amigo) &&
        Objects.equals(this.signature, shareMessage.signature) &&
        Objects.equals(this.open, shareMessage.open) &&
        Objects.equals(this.close, shareMessage.close);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigo, signature, open, close);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShareMessage {\n");
    
    sb.append("    amigo: ").append(toIndentedString(amigo)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
    sb.append("    open: ").append(toIndentedString(open)).append("\n");
    sb.append("    close: ").append(toIndentedString(close)).append("\n");
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
