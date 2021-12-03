package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.coredb.model.OriginalAsset;
import org.coredb.model.Subject;
import org.coredb.model.SubjectAsset;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SubjectEntry
 */
@Validated


public class SubjectEntry   {
  @JsonProperty("subject")
  private Subject subject = null;

  @JsonProperty("share")
  private Boolean share = null;

  @JsonProperty("ready")
  private Boolean ready = null;

  @JsonProperty("assets")
  @Valid
  private List<SubjectAsset> assets = new ArrayList<SubjectAsset>();

  @JsonProperty("originals")
  @Valid
  private List<OriginalAsset> originals = new ArrayList<OriginalAsset>();

  @JsonProperty("labels")
  @Valid
  private List<String> labels = null;

  public SubjectEntry subject(Subject subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   **/
      @NotNull

    @Valid
    public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public SubjectEntry share(Boolean share) {
    this.share = share;
    return this;
  }

  /**
   * Get share
   * @return share
   **/
      @NotNull

    public Boolean isShare() {
    return share;
  }

  public void setShare(Boolean share) {
    this.share = share;
  }

  public SubjectEntry ready(Boolean ready) {
    this.ready = ready;
    return this;
  }

  /**
   * Get ready
   * @return ready
   **/
      @NotNull

    public Boolean isReady() {
    return ready;
  }

  public void setReady(Boolean ready) {
    this.ready = ready;
  }

  public SubjectEntry assets(List<SubjectAsset> assets) {
    this.assets = assets;
    return this;
  }

  public SubjectEntry addAssetsItem(SubjectAsset assetsItem) {
    this.assets.add(assetsItem);
    return this;
  }

  /**
   * Get assets
   * @return assets
   **/
      @NotNull
    @Valid
    public List<SubjectAsset> getAssets() {
    return assets;
  }

  public void setAssets(List<SubjectAsset> assets) {
    this.assets = assets;
  }

  public SubjectEntry originals(List<OriginalAsset> originals) {
    this.originals = originals;
    return this;
  }

  public SubjectEntry addOriginalsItem(OriginalAsset originalsItem) {
    this.originals.add(originalsItem);
    return this;
  }

  /**
   * Get originals
   * @return originals
   **/
      @NotNull
    @Valid
    public List<OriginalAsset> getOriginals() {
    return originals;
  }

  public void setOriginals(List<OriginalAsset> originals) {
    this.originals = originals;
  }

  public SubjectEntry labels(List<String> labels) {
    this.labels = labels;
    return this;
  }

  public SubjectEntry addLabelsItem(String labelsItem) {
    if (this.labels == null) {
      this.labels = new ArrayList<String>();
    }
    this.labels.add(labelsItem);
    return this;
  }

  /**
   * Get labels
   * @return labels
   **/
  
    public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubjectEntry subjectEntry = (SubjectEntry) o;
    return Objects.equals(this.subject, subjectEntry.subject) &&
        Objects.equals(this.share, subjectEntry.share) &&
        Objects.equals(this.ready, subjectEntry.ready) &&
        Objects.equals(this.assets, subjectEntry.assets) &&
        Objects.equals(this.originals, subjectEntry.originals) &&
        Objects.equals(this.labels, subjectEntry.labels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, share, ready, assets, originals, labels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubjectEntry {\n");
    
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    share: ").append(toIndentedString(share)).append("\n");
    sb.append("    ready: ").append(toIndentedString(ready)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    originals: ").append(toIndentedString(originals)).append("\n");
    sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
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

