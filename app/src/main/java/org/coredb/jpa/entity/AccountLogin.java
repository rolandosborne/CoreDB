package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "account_login", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountLogin implements Serializable {
  private Integer id;
  private Account account;  
  private String username;
  private String salt;
  private String password;

  public AccountLogin() { }

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
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String value) {
    this.username = value;
  }

  @JsonIgnore
  public String getSalt() {
    return this.salt;
  }
  public void setSalt(String value) {
    this.salt = value;
  }

  @JsonIgnore
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String value) {
    this.password = value;
  }

}

