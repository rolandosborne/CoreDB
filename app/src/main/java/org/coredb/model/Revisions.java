package org.coredb.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Revisions
 */
@Validated


public class Revisions   {
  @JsonProperty("showRevision")
  private Integer showRevision = null;

  @JsonProperty("identityRevision")
  private Integer identityRevision = null;

  @JsonProperty("profileRevision")
  private Integer profileRevision = null;

  @JsonProperty("groupRevision")
  private Integer groupRevision = null;

  @JsonProperty("shareRevision")
  private Integer shareRevision = null;

  @JsonProperty("promptRevision")
  private Integer promptRevision = null;

  @JsonProperty("serviceRevision")
  private Integer serviceRevision = null;

  @JsonProperty("indexRevision")
  private Integer indexRevision = null;

  @JsonProperty("userRevision")
  private Integer userRevision = null;

  @JsonProperty("listingRevision")
  private Integer listingRevision = null;

  @JsonProperty("contactRevision")
  private Integer contactRevision = null;

  @JsonProperty("viewRevision")
  private Integer viewRevision = null;

  @JsonProperty("insightRevision")
  private Integer insightRevision = null;

  @JsonProperty("dialogueRevision")
  private Integer dialogueRevision = null;

  public Revisions showRevision(Integer showRevision) {
    this.showRevision = showRevision;
    return this;
  }

  /**
   * Get showRevision
   * @return showRevision
   **/
  @Schema(description = "")
  
    public Integer getShowRevision() {
    return showRevision;
  }

  public void setShowRevision(Integer showRevision) {
    this.showRevision = showRevision;
  }

  public Revisions identityRevision(Integer identityRevision) {
    this.identityRevision = identityRevision;
    return this;
  }

  /**
   * Get identityRevision
   * @return identityRevision
   **/
  @Schema(description = "")
  
    public Integer getIdentityRevision() {
    return identityRevision;
  }

  public void setIdentityRevision(Integer identityRevision) {
    this.identityRevision = identityRevision;
  }

  public Revisions profileRevision(Integer profileRevision) {
    this.profileRevision = profileRevision;
    return this;
  }

  /**
   * Get profileRevision
   * @return profileRevision
   **/
  @Schema(description = "")
  
    public Integer getProfileRevision() {
    return profileRevision;
  }

  public void setProfileRevision(Integer profileRevision) {
    this.profileRevision = profileRevision;
  }

  public Revisions groupRevision(Integer groupRevision) {
    this.groupRevision = groupRevision;
    return this;
  }

  /**
   * Get groupRevision
   * @return groupRevision
   **/
  @Schema(description = "")
  
    public Integer getGroupRevision() {
    return groupRevision;
  }

  public void setGroupRevision(Integer groupRevision) {
    this.groupRevision = groupRevision;
  }

  public Revisions shareRevision(Integer shareRevision) {
    this.shareRevision = shareRevision;
    return this;
  }

  /**
   * Get shareRevision
   * @return shareRevision
   **/
  @Schema(description = "")
  
    public Integer getShareRevision() {
    return shareRevision;
  }

  public void setShareRevision(Integer shareRevision) {
    this.shareRevision = shareRevision;
  }

  public Revisions promptRevision(Integer promptRevision) {
    this.promptRevision = promptRevision;
    return this;
  }

  /**
   * Get promptRevision
   * @return promptRevision
   **/
  @Schema(description = "")
  
    public Integer getPromptRevision() {
    return promptRevision;
  }

  public void setPromptRevision(Integer promptRevision) {
    this.promptRevision = promptRevision;
  }

  public Revisions serviceRevision(Integer serviceRevision) {
    this.serviceRevision = serviceRevision;
    return this;
  }

  /**
   * Get serviceRevision
   * @return serviceRevision
   **/
  @Schema(description = "")
  
    public Integer getServiceRevision() {
    return serviceRevision;
  }

  public void setServiceRevision(Integer serviceRevision) {
    this.serviceRevision = serviceRevision;
  }

  public Revisions indexRevision(Integer indexRevision) {
    this.indexRevision = indexRevision;
    return this;
  }

  /**
   * Get indexRevision
   * @return indexRevision
   **/
  @Schema(description = "")
  
    public Integer getIndexRevision() {
    return indexRevision;
  }

  public void setIndexRevision(Integer indexRevision) {
    this.indexRevision = indexRevision;
  }

  public Revisions userRevision(Integer userRevision) {
    this.userRevision = userRevision;
    return this;
  }

  /**
   * Get userRevision
   * @return userRevision
   **/
  @Schema(description = "")
  
    public Integer getUserRevision() {
    return userRevision;
  }

  public void setUserRevision(Integer userRevision) {
    this.userRevision = userRevision;
  }

  public Revisions listingRevision(Integer listingRevision) {
    this.listingRevision = listingRevision;
    return this;
  }

  /**
   * Get listingRevision
   * @return listingRevision
   **/
  @Schema(description = "")
  
    public Integer getListingRevision() {
    return listingRevision;
  }

  public void setListingRevision(Integer listingRevision) {
    this.listingRevision = listingRevision;
  }

  public Revisions contactRevision(Integer contactRevision) {
    this.contactRevision = contactRevision;
    return this;
  }

  /**
   * Get contactRevision
   * @return contactRevision
   **/
  @Schema(description = "")
  
    public Integer getContactRevision() {
    return contactRevision;
  }

  public void setContactRevision(Integer contactRevision) {
    this.contactRevision = contactRevision;
  }

  public Revisions viewRevision(Integer viewRevision) {
    this.viewRevision = viewRevision;
    return this;
  }

  /**
   * Get viewRevision
   * @return viewRevision
   **/
  @Schema(description = "")
  
    public Integer getViewRevision() {
    return viewRevision;
  }

  public void setViewRevision(Integer viewRevision) {
    this.viewRevision = viewRevision;
  }

  public Revisions insightRevision(Integer insightRevision) {
    this.insightRevision = insightRevision;
    return this;
  }

  /**
   * Get insightRevision
   * @return insightRevision
   **/
  @Schema(description = "")
  
    public Integer getInsightRevision() {
    return insightRevision;
  }

  public void setInsightRevision(Integer insightRevision) {
    this.insightRevision = insightRevision;
  }

  public Revisions dialogueRevision(Integer dialogueRevision) {
    this.dialogueRevision = dialogueRevision;
    return this;
  }

  /**
   * Get dialogueRevision
   * @return dialogueRevision
   **/
  @Schema(description = "")
  
    public Integer getDialogueRevision() {
    return dialogueRevision;
  }

  public void setDialogueRevision(Integer dialogueRevision) {
    this.dialogueRevision = dialogueRevision;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Revisions revisions = (Revisions) o;
    return Objects.equals(this.showRevision, revisions.showRevision) &&
        Objects.equals(this.identityRevision, revisions.identityRevision) &&
        Objects.equals(this.profileRevision, revisions.profileRevision) &&
        Objects.equals(this.groupRevision, revisions.groupRevision) &&
        Objects.equals(this.shareRevision, revisions.shareRevision) &&
        Objects.equals(this.promptRevision, revisions.promptRevision) &&
        Objects.equals(this.serviceRevision, revisions.serviceRevision) &&
        Objects.equals(this.indexRevision, revisions.indexRevision) &&
        Objects.equals(this.userRevision, revisions.userRevision) &&
        Objects.equals(this.listingRevision, revisions.listingRevision) &&
        Objects.equals(this.contactRevision, revisions.contactRevision) &&
        Objects.equals(this.viewRevision, revisions.viewRevision) &&
        Objects.equals(this.insightRevision, revisions.insightRevision) &&
        Objects.equals(this.dialogueRevision, revisions.dialogueRevision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(showRevision, identityRevision, profileRevision, groupRevision, shareRevision, promptRevision, serviceRevision, indexRevision, userRevision, listingRevision, contactRevision, viewRevision, insightRevision, dialogueRevision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Revisions {\n");
    
    sb.append("    showRevision: ").append(toIndentedString(showRevision)).append("\n");
    sb.append("    identityRevision: ").append(toIndentedString(identityRevision)).append("\n");
    sb.append("    profileRevision: ").append(toIndentedString(profileRevision)).append("\n");
    sb.append("    groupRevision: ").append(toIndentedString(groupRevision)).append("\n");
    sb.append("    shareRevision: ").append(toIndentedString(shareRevision)).append("\n");
    sb.append("    promptRevision: ").append(toIndentedString(promptRevision)).append("\n");
    sb.append("    serviceRevision: ").append(toIndentedString(serviceRevision)).append("\n");
    sb.append("    indexRevision: ").append(toIndentedString(indexRevision)).append("\n");
    sb.append("    userRevision: ").append(toIndentedString(userRevision)).append("\n");
    sb.append("    listingRevision: ").append(toIndentedString(listingRevision)).append("\n");
    sb.append("    contactRevision: ").append(toIndentedString(contactRevision)).append("\n");
    sb.append("    viewRevision: ").append(toIndentedString(viewRevision)).append("\n");
    sb.append("    insightRevision: ").append(toIndentedString(insightRevision)).append("\n");
    sb.append("    dialogueRevision: ").append(toIndentedString(dialogueRevision)).append("\n");
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

