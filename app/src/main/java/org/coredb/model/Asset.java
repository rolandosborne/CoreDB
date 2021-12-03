package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.coredb.model.OriginalAsset;
import org.coredb.model.SubjectAsset;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Asset
 */
@Validated


public class Asset   {
  @JsonProperty("assets")
  @Valid
  private List<SubjectAsset> assets = new ArrayList<SubjectAsset>();

  @JsonProperty("original")
  private OriginalAsset original = null;

  public Asset assets(List<SubjectAsset> assets) {
    this.assets = assets;
    return this;
  }

  public Asset addAssetsItem(SubjectAsset assetsItem) {
    this.assets.add(assetsItem);
    return this;
  }

  /**
   * Get assets
   * @return assets
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<SubjectAsset> getAssets() {
    return assets;
  }

  public void setAssets(List<SubjectAsset> assets) {
    this.assets = assets;
  }

  public Asset original(OriginalAsset original) {
    this.original = original;
    return this;
  }

  /**
   * Get original
   * @return original
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public OriginalAsset getOriginal() {
    return original;
  }

  public void setOriginal(OriginalAsset original) {
    this.original = original;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Asset asset = (Asset) o;
    return Objects.equals(this.assets, asset.assets) &&
        Objects.equals(this.original, asset.original);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assets, original);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Asset {\n");
    
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    original: ").append(toIndentedString(original)).append("\n");
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

