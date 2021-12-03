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
 * StoreStatus
 */
@Validated


public class StoreStatus   {
  @JsonProperty("available")
  private Long available = null;

  @JsonProperty("used")
  private Long used = null;

  public StoreStatus available(Long available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public Long getAvailable() {
    return available;
  }

  public void setAvailable(Long available) {
    this.available = available;
  }

  public StoreStatus used(Long used) {
    this.used = used;
    return this;
  }

  /**
   * Get used
   * @return used
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public Long getUsed() {
    return used;
  }

  public void setUsed(Long used) {
    this.used = used;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreStatus storeStatus = (StoreStatus) o;
    return Objects.equals(this.available, storeStatus.available) &&
        Objects.equals(this.used, storeStatus.used);
  }

  @Override
  public int hashCode() {
    return Objects.hash(available, used);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreStatus {\n");
    
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    used: ").append(toIndentedString(used)).append("\n");
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
