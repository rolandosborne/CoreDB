package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SubjectView
 */
@Validated


public class SubjectView   {
  @JsonProperty("subjectId")
  private String subjectId = null;

  @JsonProperty("revision")
  private Integer revision = null;

  @JsonProperty("tagRevision")
  private Integer tagRevision = null;

  public SubjectView subjectId(String subjectId) {
    this.subjectId = subjectId;
    return this;
  }

  /**
   * Get subjectId
   * @return subjectId
   **/
      @NotNull

    public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public SubjectView revision(Integer revision) {
    this.revision = revision;
    return this;
  }

  /**
   * Get revision
   * @return revision
   **/
      @NotNull

    public Integer getRevision() {
    return revision;
  }

  public void setRevision(Integer revision) {
    this.revision = revision;
  }

  public SubjectView tagRevision(Integer tagRevision) {
    this.tagRevision = tagRevision;
    return this;
  }

  /**
   * Get tagRevision
   * @return tagRevision
   **/
      @NotNull

    public Integer getTagRevision() {
    return tagRevision;
  }

  public void setTagRevision(Integer tagRevision) {
    this.tagRevision = tagRevision;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubjectView subjectView = (SubjectView) o;
    return Objects.equals(this.subjectId, subjectView.subjectId) &&
        Objects.equals(this.revision, subjectView.revision) &&
        Objects.equals(this.tagRevision, subjectView.tagRevision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subjectId, revision, tagRevision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubjectView {\n");
    
    sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
    sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
    sb.append("    tagRevision: ").append(toIndentedString(tagRevision)).append("\n");
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

