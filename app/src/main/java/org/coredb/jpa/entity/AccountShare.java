package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.ShareEntry;
import org.coredb.model.ShareEntry.StatusEnum;

import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "share_connection", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountShare extends ShareEntry implements Serializable {
  private Integer id;
  private Account account;
  private AccountEmigo emigo;
  private String inToken;

  public AccountShare() {
  }

  public AccountShare(Account account, AccountEmigo emigo) {
    super.setShareId(UUID.randomUUID().toString().replace("-", ""));
    super.setRevision(1);
    this.account = account;
    this.setEmigo(emigo);
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
  @JoinColumn(name = "emigo_id")
  public AccountEmigo getEmigo() {
    return this.emigo;
  }
  public void setEmigo(AccountEmigo value) {
    if(value == null) {
      super.setAmigoId(null);
    }
    else {
      super.setAmigoId(value.getEmigoId());
    }
    this.emigo = value;
  }

  @JsonIgnore
  public String getShareId() {
    return super.getShareId();
  }
  public void setShareId(String value) {
    super.setShareId(value);
  }

  @JsonIgnore
  public Long getUpdated() {
    return super.getUpdated();
  }
  public void setUpdated(Long value) {
    super.setUpdated(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  public String getOutToken() {
    return super.getToken();
  }
  public void setOutToken(String value) {
    super.setToken(value);
  }

  @JsonIgnore
  public String getInToken() {
    return this.inToken;
  }
  public void setInToken(String value) {
    this.inToken = value;
  }

  @JsonIgnore
  public String getState() {
    if(super.getStatus() == null) {
      return null;
    }
    else {
      return super.getStatus().toString();
    }
  }
  public void setState(String value) {
    super.setStatus(StatusEnum.fromValue(value));
  }
}

