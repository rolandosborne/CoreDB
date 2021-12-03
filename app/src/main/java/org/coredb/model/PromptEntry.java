package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.coredb.model.PromptAnswer;
import org.coredb.model.PromptQuestion;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PromptEntry
 */
@Validated


public class PromptEntry   {
  @JsonProperty("promptId")
  private String promptId = null;

  @JsonProperty("data")
  private PromptQuestion data = null;

  @JsonProperty("answers")
  @Valid
  private List<PromptAnswer> answers = new ArrayList<PromptAnswer>();

  public PromptEntry promptId(String promptId) {
    this.promptId = promptId;
    return this;
  }

  /**
   * Get promptId
   * @return promptId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getPromptId() {
    return promptId;
  }

  public void setPromptId(String promptId) {
    this.promptId = promptId;
  }

  public PromptEntry data(PromptQuestion data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public PromptQuestion getData() {
    return data;
  }

  public void setData(PromptQuestion data) {
    this.data = data;
  }

  public PromptEntry answers(List<PromptAnswer> answers) {
    this.answers = answers;
    return this;
  }

  public PromptEntry addAnswersItem(PromptAnswer answersItem) {
    this.answers.add(answersItem);
    return this;
  }

  /**
   * Get answers
   * @return answers
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull
    @Valid
    public List<PromptAnswer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<PromptAnswer> answers) {
    this.answers = answers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PromptEntry promptEntry = (PromptEntry) o;
    return Objects.equals(this.promptId, promptEntry.promptId) &&
        Objects.equals(this.data, promptEntry.data) &&
        Objects.equals(this.answers, promptEntry.answers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promptId, data, answers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PromptEntry {\n");
    
    sb.append("    promptId: ").append(toIndentedString(promptId)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    answers: ").append(toIndentedString(answers)).append("\n");
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
