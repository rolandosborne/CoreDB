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
 * ServiceEntry
 */
@Validated


public class ServiceEntry   {
  @JsonProperty("amigoId")
  private String amigoId = null;

  @JsonProperty("accountAccess")
  private ServiceAccess accountAccess = null;

  @JsonProperty("serviceAccess")
  private ServiceAccess serviceAccess = null;

  public ServiceEntry amigoId(String amigoId) {
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

  public ServiceEntry accountAccess(ServiceAccess accountAccess) {
    this.accountAccess = accountAccess;
    return this;
  }

  /**
   * Get accountAccess
   * @return accountAccess
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public ServiceAccess getAccountAccess() {
    return accountAccess;
  }

  public void setAccountAccess(ServiceAccess accountAccess) {
    this.accountAccess = accountAccess;
  }

  public ServiceEntry serviceAccess(ServiceAccess serviceAccess) {
    this.serviceAccess = serviceAccess;
    return this;
  }

  /**
   * Get serviceAccess
   * @return serviceAccess
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public ServiceAccess getServiceAccess() {
    return serviceAccess;
  }

  public void setServiceAccess(ServiceAccess serviceAccess) {
    this.serviceAccess = serviceAccess;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceEntry serviceEntry = (ServiceEntry) o;
    return Objects.equals(this.amigoId, serviceEntry.amigoId) &&
        Objects.equals(this.accountAccess, serviceEntry.accountAccess) &&
        Objects.equals(this.serviceAccess, serviceEntry.serviceAccess);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amigoId, accountAccess, serviceAccess);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceEntry {\n");
    
    sb.append("    amigoId: ").append(toIndentedString(amigoId)).append("\n");
    sb.append("    accountAccess: ").append(toIndentedString(accountAccess)).append("\n");
    sb.append("    serviceAccess: ").append(toIndentedString(serviceAccess)).append("\n");
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
