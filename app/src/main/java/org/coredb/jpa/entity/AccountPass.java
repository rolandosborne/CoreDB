package org.coredb.jpa.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.ServiceAccess;
import org.coredb.model.Pass;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "pass", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountPass extends Pass implements Serializable {
  private Integer id;
  private Long issued;
  private Long expires;
  private Account account;
  private ServiceAccess access;

  public AccountPass() {
    this.access = new ServiceAccess();
  }

  public AccountPass(Account act, ServiceAccess acc) {
    this.account = act;
    this.access = acc;
  }

  @Transient
  @JsonIgnore
  public ServiceAccess getAccess() {
    return this.access;
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
  @ManyToOne
  @JoinColumn(name = "account_id")
  public Account getAccount() {
    return this.account;
  }
  public void setAccount(Account value) {
    this.account = value;
  }

  @JsonIgnore
  public String getPass() {
    return super.getData();
  }
  public void setPass(String value) {
    super.setData(value);
  }

  @JsonIgnore
  public Long getIssued() {
    return this.issued;
  }
  public void setIssued(Long value) {
    this.issued = value;
  }

  @JsonIgnore
  public Long getExpires() {
    return this.expires;
  }
  public void setExpires(Long value) {
    this.expires = value;
  }

  @JsonIgnore
  public Boolean getAccountShow() {
    if(this.access.isEnableShow() == null) {
      return false;
    }
    return this.access.isEnableShow();
  }
  public void setAccountShow(Boolean value) {
    this.access.setEnableShow(value);
  }

  @JsonIgnore
  public Boolean getAccountIdentity() {
    if(this.access.isEnableIdentity() == null) {
      return false;
    }
    return this.access.isEnableIdentity();
  }
  public void setAccountIdentity(Boolean value) {
    this.access.setEnableIdentity(value);
  }

  @JsonIgnore
  public Boolean getAccountProfile() {
    if(this.access.isEnableProfile() == null) {
      return false;
    }
    return this.access.isEnableProfile();
  }
  public void setAccountProfile(Boolean value) {
    this.access.setEnableProfile(value);
  }

  @JsonIgnore
  public Boolean getAccountGroup() {
    if(this.access.isEnableGroup() == null) {
      return false;
    }
    return this.access.isEnableGroup();
  }
  public void setAccountGroup(Boolean value) {
    this.access.setEnableGroup(value);
  }

  @JsonIgnore
  public Boolean getAccountShare() {
    if(this.access.isEnableShare() == null) {
      return false;
    }
    return this.access.isEnableShare();
  }
  public void setAccountShare(Boolean value) {
    this.access.setEnableShare(value);
  }

  @JsonIgnore
  public Boolean getAccountPrompt() {
    if(this.access.isEnablePrompt() == null) {
      return false;
    }
    return this.access.isEnablePrompt();
  }
  public void setAccountPrompt(Boolean value) {
    this.access.setEnablePrompt(value);
  }

  @JsonIgnore
  public Boolean getAccountApp() {
    if(this.access.isEnableService() == null) {
      return false;
    }
    return this.access.isEnableService();
  }
  public void setAccountApp(Boolean value) {
    this.access.setEnableService(value);
  }

  @JsonIgnore
  public Boolean getAccountIndex() {
    if(this.access.isEnableIndex() == null) {
      return false;
    }
    return this.access.isEnableIndex();
  }
  public void setAccountIndex(Boolean value) {
    this.access.setEnableIndex(value);
  }

  @JsonIgnore
  public Boolean getAccountUser() {
    if(this.access.isEnableUser() == null) {
      return false;
    }
    return this.access.isEnableUser();
  }
  public void setAccountUser(Boolean value) {
    this.access.setEnableUser(value);
  }

  @JsonIgnore
  public Boolean getAccountAccess() {
    if(this.access.isEnableAccess() == null) {
      return false;
    }
    return this.access.isEnableAccess();
  }
  public void setAccountAccess(Boolean value) {
    this.access.setEnableAccess(value);
  }

  @JsonIgnore
  public Boolean getAccountAccount() {
    if(this.access.isEnableAccount() == null) {
      return false;
    }
    return this.access.isEnableAccount();
  }
  public void setAccountAccount(Boolean value) {
    this.access.setEnableAccount(value);
  }

  @JsonIgnore
  public Boolean getAccountConversation() {
    if(this.access.isEnableConversation() == null) {
      return false;
    }
    return this.access.isEnableConversation();
  }
  public void setAccountConversation(Boolean value) {
    this.access.setEnableConversation(value);
  }

}
