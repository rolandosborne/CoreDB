package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.model.SharePrompt;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ShareStatus
 */
@Validated


public class ShareStatus   {
  /**
   * Gets or Sets shareStatus
   */
  public enum ShareStatusEnum {
    PENDING("pending"),
    
    FAILED("failed"),
    
    RECEIVED("received"),
    
    CONNECTED("connected"),
    
    CLOSED("closed");

    private String value;

    ShareStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ShareStatusEnum fromValue(String text) {
      for (ShareStatusEnum b : ShareStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("shareStatus")
  private ShareStatusEnum shareStatus = null;

  @JsonProperty("pending")
  private SharePrompt pending = null;

  @JsonProperty("connected")
  private String connected = null;

  public ShareStatus shareStatus(ShareStatusEnum shareStatus) {
    this.shareStatus = shareStatus;
    return this;
  }

  /**
   * Get shareStatus
   * @return shareStatus
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public ShareStatusEnum getShareStatus() {
    return shareStatus;
  }

  public void setShareStatus(ShareStatusEnum shareStatus) {
    this.shareStatus = shareStatus;
  }

  public ShareStatus pending(SharePrompt pending) {
    this.pending = pending;
    return this;
  }

  /**
   * Get pending
   * @return pending
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public SharePrompt getPending() {
    return pending;
  }

  public void setPending(SharePrompt pending) {
    this.pending = pending;
  }

  public ShareStatus connected(String connected) {
    this.connected = connected;
    return this;
  }

  /**
   * Get connected
   * @return connected
  **/
  @ApiModelProperty(value = "")
  
    public String getConnected() {
    return connected;
  }

  public void setConnected(String connected) {
    this.connected = connected;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShareStatus shareStatus = (ShareStatus) o;
    return Objects.equals(this.shareStatus, shareStatus.shareStatus) &&
        Objects.equals(this.pending, shareStatus.pending) &&
        Objects.equals(this.connected, shareStatus.connected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shareStatus, pending, connected);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShareStatus {\n");
    
    sb.append("    shareStatus: ").append(toIndentedString(shareStatus)).append("\n");
    sb.append("    pending: ").append(toIndentedString(pending)).append("\n");
    sb.append("    connected: ").append(toIndentedString(connected)).append("\n");
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
