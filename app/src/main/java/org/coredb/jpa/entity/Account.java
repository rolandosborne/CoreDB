package org.coredb.jpa.entity;

import org.coredb.model.Amigo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import org.coredb.model.AccountEntry;
import org.coredb.model.Amigo;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Account extends AccountEntry implements Serializable {
  private Integer id;
  private Boolean dirty;
  private Integer indexRevision;
  private Integer shareRevision;
  private Integer promptRevision;
  private Integer userRevision;
  private Integer identityRevision;
  private Integer profileRevision;
  private Integer groupRevision;
  private Integer contactRevision;
  private Integer viewRevision;
  private Integer showRevision;
  private Integer serviceRevision;
  private Integer dialogueRevision;
  private Integer insightRevision;
  private Boolean enabled;
  private Set<AccountConfig> configs;

  public Account() {
    this.dirty = true;
    this.indexRevision = 0;
    this.shareRevision = 0;
    this.promptRevision = 0;
    this.userRevision = 0;
    this.identityRevision = 0;
    this.profileRevision = 0;
    this.groupRevision = 0;
    this.contactRevision = 0;
    this.viewRevision = 0;
    this.showRevision = 0;
    this.serviceRevision = 0;
    this.dialogueRevision = 0;
    this.insightRevision = 0;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @JsonIgnore
  public Integer getId() {
    return this.id;
  }
  public void setId(Integer value) {
    this.id = value;
  }

  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "account_id")
  public Set<AccountConfig> getConfigs() {
    return this.configs;
  }
  public void setConfigs(Set<AccountConfig> value) {
    this.configs = value;
  }

  @JsonIgnore
  public String getEmigoId() {
    return super.getAmigoId();
  }
  public void setEmigoId(String value) {
    super.setAmigoId(value);
  }

  @JsonIgnore
  public Boolean getDirty() {
    return this.dirty;
  }
  public void setDirty(Boolean value) {
    this.dirty = value;
  }

  @JsonIgnore
  public Boolean getEnabled() {
    return this.enabled;
  }
  public void setEnabled(Boolean value) {
    this.enabled = value;
  }

  @JsonIgnore
  public Integer getIndexRevision() {
    return this.indexRevision;
  }
  public void setIndexRevision(Integer value) {
    this.indexRevision = value;
  }

  @JsonIgnore
  public Integer getShareRevision() {
    return this.shareRevision;
  }
  public void setShareRevision(Integer value) {
    this.shareRevision = value;
  }

  @JsonIgnore
  public Integer getPromptRevision() {
    return this.promptRevision;
  }
  public void setPromptRevision(Integer value) {
    this.promptRevision = value;
  }

  @JsonIgnore
  public Integer getUserRevision() {
    return this.userRevision;
  }
  public void setUserRevision(Integer value) {
    this.userRevision = value;
  }

  @JsonIgnore
  public Integer getIdentityRevision() {
    return this.identityRevision;
  }
  public void setIdentityRevision(Integer value) {
    this.identityRevision = value;
  }

  @JsonIgnore
  public Integer getProfileRevision() {
    return this.profileRevision;
  }
  public void setProfileRevision(Integer value) {
    this.profileRevision = value;
  }

  @JsonIgnore
  public Integer getGroupRevision() {
    return this.groupRevision;
  }
  public void setGroupRevision(Integer value) {
    this.groupRevision = value;
  }

  @JsonIgnore
  public Integer getContactRevision() {
    return this.contactRevision;
  }
  public void setContactRevision(Integer value) {
    this.contactRevision = value;
  }

  @JsonIgnore
  public Integer getViewRevision() {
    return this.viewRevision;
  }
  public void setViewRevision(Integer value) {
    this.viewRevision = value;
  }

  @JsonIgnore
  public Integer getShowRevision() {
    return this.showRevision;
  }
  public void setShowRevision(Integer value) {
    this.showRevision = value;
  }

  @JsonIgnore
  public Integer getServiceRevision() {
    return this.serviceRevision;
  }
  public void setServiceRevision(Integer value) {
    this.serviceRevision = value;
  }

  @JsonIgnore
  public Integer getDialogueRevision() {
    return this.dialogueRevision;
  }
  public void setDialogueRevision(Integer value) {
    this.dialogueRevision = value;
  }

  @JsonIgnore
  public Integer getInsightRevision() {
    return this.insightRevision;
  }
  public void setInsightRevision(Integer value) {
    this.insightRevision = value;
  }

}

