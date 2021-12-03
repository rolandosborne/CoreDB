package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.coredb.model.Attribute;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AttributeEntry
 */
@Validated


public class AttributeEntry   {
  @JsonProperty("attribute")
  private Attribute attribute = null;

  @JsonProperty("labels")
  @Valid
  private List<String> labels = new ArrayList<String>();

  public AttributeEntry attribute(Attribute attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Get attribute
   * @return attribute
   **/
      @NotNull

    @Valid
    public Attribute getAttribute() {
    return attribute;
  }

  public void setAttribute(Attribute attribute) {
    this.attribute = attribute;
  }

  public AttributeEntry labels(List<String> labels) {
    this.labels = labels;
    return this;
  }

  public AttributeEntry addLabelsItem(String labelsItem) {
    this.labels.add(labelsItem);
    return this;
  }

  /**
   * Get labels
   * @return labels
   **/
      @NotNull

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
    AttributeEntry attributeEntry = (AttributeEntry) o;
    return Objects.equals(this.attribute, attributeEntry.attribute) &&
        Objects.equals(this.labels, attributeEntry.labels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attribute, labels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeEntry {\n");
    
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
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

