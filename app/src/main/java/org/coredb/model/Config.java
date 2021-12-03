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
 * Config
 */
@Validated


public class Config   {
  @JsonProperty("strValue")
  private String strValue = null;

  @JsonProperty("numValue")
  private Long numValue = null;

  @JsonProperty("boolValue")
  private Boolean boolValue = null;

  public Config strValue(String strValue) {
    this.strValue = strValue;
    return this;
  }

  /**
   * Get strValue
   * @return strValue
  **/
  @ApiModelProperty(value = "")
  
    public String getStrValue() {
    return strValue;
  }

  public void setStrValue(String strValue) {
    this.strValue = strValue;
  }

  public Config numValue(Long numValue) {
    this.numValue = numValue;
    return this;
  }

  /**
   * Get numValue
   * @return numValue
  **/
  @ApiModelProperty(value = "")
  
    public Long getNumValue() {
    return numValue;
  }

  public void setNumValue(Long numValue) {
    this.numValue = numValue;
  }

  public Config boolValue(Boolean boolValue) {
    this.boolValue = boolValue;
    return this;
  }

  /**
   * Get boolValue
   * @return boolValue
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isBoolValue() {
    return boolValue;
  }

  public void setBoolValue(Boolean boolValue) {
    this.boolValue = boolValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Config config = (Config) o;
    return Objects.equals(this.strValue, config.strValue) &&
        Objects.equals(this.numValue, config.numValue) &&
        Objects.equals(this.boolValue, config.boolValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strValue, numValue, boolValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Config {\n");
    
    sb.append("    strValue: ").append(toIndentedString(strValue)).append("\n");
    sb.append("    numValue: ").append(toIndentedString(numValue)).append("\n");
    sb.append("    boolValue: ").append(toIndentedString(boolValue)).append("\n");
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
