package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "account_token", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountToken implements Serializable {
  private Integer id;
  private Account account;  
  private String token;
  private Long issued;
  private Long expires;
  private Boolean expired;

  public AccountToken() { }

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
  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
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
  public Boolean getExpired() {
    return this.expired;
  }
  public void setExpired(Boolean value) {
    this.expired = value;
  }

}

