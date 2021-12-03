package org.coredb.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import org.coredb.model.UserEntry;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountUser extends UserEntry implements Serializable {
  private Integer id;
  private Account account;

  public AccountUser() {
  }

  public AccountUser(Account act, String emigoId, String accountToken, String serviceToken) {
    this.account = act;
    super.setAmigoId(emigoId);
    super.setAccountToken(accountToken);
    super.setServiceToken(serviceToken);
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

  @ManyToOne
  @JoinColumn(name = "account_id")
  public Account getAccount() {
    return this.account;
  }
  public void setAccount(Account value) {
    this.account = value;
  }

  @JsonIgnore
  public String getEmigoId() {
    return super.getAmigoId();
  }
  public void setEmigoId(String value) {
    super.setAmigoId(value);
  } 

  @JsonIgnore
  public String getAccountToken() {
    return super.getAccountToken();
  }
  public void setAccountToken(String value) {
    super.setAccountToken(value);
  }

  @JsonIgnore
  public String getServiceToken() {
    return super.getServiceToken();
  }
  public void setServiceToken(String value) {
    super.setServiceToken(value);
  }

}

