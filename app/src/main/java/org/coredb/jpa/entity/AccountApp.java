package org.coredb.jpa.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.ServiceAccess;
import org.coredb.model.ServiceEntry;
import org.coredb.model.Amigo;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "service", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountApp extends ServiceEntry implements Serializable {
  private Integer id;
  private String token;
  private Account account;
  private AppEntity appEntity;

  public AccountApp() {
    super.setAccountAccess(new ServiceAccess());
    super.setServiceAccess(new ServiceAccess());
  }

  public AccountApp(Account account, AppEntity entity, String token, ServiceAccess accountAccess, ServiceAccess serviceAccess) {
    this.account = account;
    this.appEntity = entity;
    this.token = token;
    super.setAmigoId(appEntity.getEmigoId()); // necessary for unit test
    super.setAccountAccess(accountAccess);
    super.setServiceAccess(serviceAccess);
  }

  public AccountApp(Account account, AppEntity entity, String token) {
    this.account = account;
    this.appEntity = entity;
    this.token = token;
    super.setAmigoId(appEntity.getEmigoId()); // necessary for unit test
    super.setAccountAccess(new ServiceAccess());
    super.setServiceAccess(new ServiceAccess());
    super.getAccountAccess().setEnableShow(true);
    super.getAccountAccess().setEnableIdentity(true);
    super.getAccountAccess().setEnableProfile(true);
    super.getAccountAccess().setEnableGroup(true);
    super.getAccountAccess().setEnableShare(true);
    super.getAccountAccess().setEnableService(true);
    super.getAccountAccess().setEnablePrompt(true);
    super.getAccountAccess().setEnableIndex(true);
    super.getAccountAccess().setEnableUser(true);
    super.getAccountAccess().setEnableAccess(true);
    super.getAccountAccess().setEnableAccount(true);
    super.getAccountAccess().setEnableConversation(true);
    super.getServiceAccess().setEnableShow(true);
    super.getServiceAccess().setEnableIdentity(true);
    super.getServiceAccess().setEnableProfile(true);
    super.getServiceAccess().setEnableGroup(true);
    super.getServiceAccess().setEnableShare(true);
    super.getServiceAccess().setEnableService(true);
    super.getServiceAccess().setEnablePrompt(true);
    super.getServiceAccess().setEnableIndex(true);
    super.getServiceAccess().setEnableUser(true);
    super.getServiceAccess().setEnableAccess(true);
    super.getServiceAccess().setEnableAccount(true);
    super.getServiceAccess().setEnableConversation(true);
  }

  public AccountApp(Account account, AppEntity entity, String token, ServiceAccess access) {
    this.account = account;
    this.appEntity = entity;
    this.token = token;
    super.setAmigoId(appEntity.getEmigoId()); // necessary for unit test
    super.setAccountAccess(access);
    super.setServiceAccess(access);
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
  @ManyToOne
  @JoinColumn(name = "service_id")
  public AppEntity getAppEntity() {
    return this.appEntity;
  }
  public void setAppEntity(AppEntity value) {
    this.appEntity = value;
    super.setAmigoId(value.getEmigoId());
  }

  @JsonIgnore
  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
  }

  @JsonIgnore
  @Column(name = "account_conversation")
  public Boolean getAuthorizedConversation() {
    if(super.getAccountAccess().isEnableConversation() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableConversation();
  }
  public void setAuthorizedConversation(Boolean value) {
    super.getAccountAccess().setEnableConversation(value);
  }

  @JsonIgnore
  @Column(name = "service_conversation")
  public Boolean getRequestedConversation() {
    if(super.getServiceAccess().isEnableConversation() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableConversation();
  }
  public void setRequestedConversation(Boolean value) {
    super.getServiceAccess().setEnableConversation(value);
  }

  @JsonIgnore
  @Column(name = "account_show")
  public Boolean getAuthorizedShow() {
    if(super.getAccountAccess().isEnableShow() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableShow();
  }
  public void setAuthorizedShow(Boolean value) {
    super.getAccountAccess().setEnableShow(value);
  }

  @JsonIgnore
  @Column(name = "service_show")
  public Boolean getRequestedShow() {
    if(super.getServiceAccess().isEnableShow() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableShow();
  }
  public void setRequestedShow(Boolean value) {
    super.getServiceAccess().setEnableShow(value);
  }

  @JsonIgnore
  @Column(name = "account_identity")
  public Boolean getAuthorizedIdentity() {
    if(super.getAccountAccess().isEnableIdentity() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableIdentity();
  }
  public void setAuthorizedIdentity(Boolean value) {
    super.getAccountAccess().setEnableIdentity(value);
  }

  @JsonIgnore
  @Column(name = "service_identity")
  public Boolean getRequestedIdentity() {
    if(super.getServiceAccess().isEnableIdentity() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableIdentity();
  }
  public void setRequestedIdentity(Boolean value) {
    super.getServiceAccess().setEnableIdentity(value);
  }

  @JsonIgnore
  @Column(name = "account_profile")
  public Boolean getAuthorizedProfile() {
    if(super.getAccountAccess().isEnableProfile() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableProfile();
  }
  public void setAuthorizedProfile(Boolean value) {
    super.getAccountAccess().setEnableProfile(value);
  }

  @JsonIgnore
  @Column(name = "service_profile")
  public Boolean getRequestedProfile() {
    if(super.getServiceAccess().isEnableProfile() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableProfile();
  }
  public void setRequestedProfile(Boolean value) {
    super.getServiceAccess().setEnableProfile(value);
  }

  @JsonIgnore
  @Column(name = "account_group")
  public Boolean getAuthorizedGroup() {
    if(super.getAccountAccess().isEnableGroup() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableGroup();
  }
  public void setAuthorizedGroup(Boolean value) {
    super.getAccountAccess().setEnableGroup(value);
  }

  @JsonIgnore
  @Column(name = "service_group")
  public Boolean getRequestedGroup() {
    if(super.getServiceAccess().isEnableGroup() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableGroup();
  }
  public void setRequestedGroup(Boolean value) {
    super.getServiceAccess().setEnableGroup(value);
  }

  @JsonIgnore
  @Column(name = "account_share")
  public Boolean getAuthorizedShare() {
    if(super.getAccountAccess().isEnableShare() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableShare();
  }
  public void setAuthorizedShare(Boolean value) {
    super.getAccountAccess().setEnableShare(value);
  }

  @JsonIgnore
  @Column(name = "service_share")
  public Boolean getRequestedShare() {
    if(super.getServiceAccess().isEnableShare() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableShare();
  }
  public void setRequestedShare(Boolean value) {
    super.getServiceAccess().setEnableShare(value);
  }

  @JsonIgnore
  @Column(name = "account_prompt")
  public Boolean getAuthorizedPrompt() {
    if(super.getAccountAccess().isEnablePrompt() == null) {
      return false;
    }
    return super.getAccountAccess().isEnablePrompt();
  }
  public void setAuthorizedPrompt(Boolean value) {
    super.getAccountAccess().setEnablePrompt(value);
  }

  @JsonIgnore
  @Column(name = "service_prompt")
  public Boolean getRequestedPrompt() {
    if(super.getServiceAccess().isEnablePrompt() == null) {
      return false;
    }
    return super.getServiceAccess().isEnablePrompt();
  }
  public void setRequestedPrompt(Boolean value) {
    super.getServiceAccess().setEnablePrompt(value);
  }

  @JsonIgnore
  @Column(name = "account_app")
  public Boolean getAuthorizedApp() {
    if(super.getAccountAccess().isEnableService() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableService();
  }
  public void setAuthorizedApp(Boolean value) {
    super.getAccountAccess().setEnableService(value);
  }

  @JsonIgnore
  @Column(name = "service_app")
  public Boolean getRequestedApp() {
    if(super.getServiceAccess().isEnableService() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableService();
  }
  public void setRequestedApp(Boolean value) {
    super.getServiceAccess().setEnableService(value);
  }

  @JsonIgnore
  @Column(name = "account_index")
  public Boolean getAuthorizedIndex() {
    if(super.getAccountAccess().isEnableIndex() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableIndex();
  }
  public void setAuthorizedIndex(Boolean value) {
    super.getAccountAccess().setEnableIndex(value);
  }

  @JsonIgnore
  @Column(name = "service_index")
  public Boolean getRequestedIndex() {
    if(super.getServiceAccess().isEnableIndex() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableIndex();
  }
  public void setRequestedIndex(Boolean value) {
    super.getServiceAccess().setEnableIndex(value);
  }

  @JsonIgnore
  @Column(name = "account_user")
  public Boolean getAuthorizedUser() {
    if(super.getAccountAccess().isEnableUser() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableUser();
  }
  public void setAuthorizedUser(Boolean value) {
    super.getAccountAccess().setEnableUser(value);
  }

  @JsonIgnore
  @Column(name = "service_user")
  public Boolean getRequestedUser() {
    if(super.getServiceAccess().isEnableUser() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableUser();
  }
  public void setRequestedUser(Boolean value) {
    super.getServiceAccess().setEnableUser(value);
  }

  @JsonIgnore
  @Column(name = "account_access")
  public Boolean getAuthorizedAccess() {
    if(super.getAccountAccess().isEnableAccess() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableAccess();
  }
  public void setAuthorizedAccess(Boolean value) {
    super.getAccountAccess().setEnableAccess(value);
  }

  @JsonIgnore
  @Column(name = "service_access")
  public Boolean getRequestedAccess() {
    if(super.getServiceAccess().isEnableAccess() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableAccess();
  }
  public void setRequestedAccess(Boolean value) {
    super.getServiceAccess().setEnableAccess(value);
  }

  @JsonIgnore
  @Column(name = "account_account")
  public Boolean getAuthorizedAccount() {
    if(super.getAccountAccess().isEnableAccount() == null) {
      return false;
    }
    return super.getAccountAccess().isEnableAccount();
  }
  public void setAuthorizedAccount(Boolean value) {
    super.getAccountAccess().setEnableAccount(value);
  }

  @JsonIgnore
  @Column(name = "service_account")
  public Boolean getRequestedAccount() {
    if(super.getServiceAccess().isEnableAccount() == null) {
      return false;
    }
    return super.getServiceAccess().isEnableAccount();
  }
  public void setRequestedAccount(Boolean value) {
    super.getServiceAccess().setEnableAccount(value);
  }

}
