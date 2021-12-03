package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.model.Config;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ConfigEntry
 */
@Validated


public class ConfigEntry   {
  @JsonProperty("configId")
  private String configId = null;

  @JsonProperty("config")
  private Config config = null;

  public ConfigEntry configId(String configId) {
    this.configId = configId;
    return this;
  }

  /**
   * Get configId
   * @return configId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getConfigId() {
    return configId;
  }

  public void setConfigId(String configId) {
    this.configId = configId;
  }

  public ConfigEntry config(Config config) {
    this.config = config;
    return this;
  }

  /**
   * Get config
   * @return config
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigEntry configEntry = (ConfigEntry) o;
    return Objects.equals(this.configId, configEntry.configId) &&
        Objects.equals(this.config, configEntry.config);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configId, config);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigEntry {\n");
    
    sb.append("    configId: ").append(toIndentedString(configId)).append("\n");
    sb.append("    config: ").append(toIndentedString(config)).append("\n");
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
