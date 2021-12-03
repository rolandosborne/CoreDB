package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.coredb.model.Blurb;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Topic
 */
@Validated


public class Topic   {
  @JsonProperty("topicId")
  private String topicId = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("blurbs")
  @Valid
  private List<Blurb> blurbs = new ArrayList<Blurb>();

  public Topic topicId(String topicId) {
    this.topicId = topicId;
    return this;
  }

  /**
   * Get topicId
   * @return topicId
   **/
  @Schema(required = true, description = "")
      @NotNull

    public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  public Topic revision(Integer revision) {
    this.revision = revision;
    return this;
  }

  /**
   * Get revision
   * @return revision
   **/
  @Schema(required = true, description = "")
      @NotNull

    public Integer getRevision() {
    return revision;
  }

  public void setRevision(Integer revision) {
    this.revision = revision;
  }

  public Topic blurbs(List<Blurb> blurbs) {
    this.blurbs = blurbs;
    return this;
  }

  public Topic addBlurbsItem(Blurb blurbsItem) {
    this.blurbs.add(blurbsItem);
    return this;
  }

  /**
   * Get blurbs
   * @return blurbs
   **/
  @Schema(required = true, description = "")
      @NotNull
    @Valid
    public List<Blurb> getBlurbs() {
    return blurbs;
  }

  public void setBlurbs(List<Blurb> blurbs) {
    this.blurbs = blurbs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Topic topic = (Topic) o;
    return Objects.equals(this.topicId, topic.topicId) &&
        Objects.equals(this.revision, topic.revision) &&
        Objects.equals(this.blurbs, topic.blurbs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topicId, revision, blurbs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Topic {\n");
    
    sb.append("    topicId: ").append(toIndentedString(topicId)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    blurbs: ").append(toIndentedString(blurbs)).append("\n");
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

