package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_agent", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountUserAgent implements Serializable {
  private Integer id;
  private AccountUser user;
  private String message;
  private String signature;
  private Long issued;
  private Long expires;
  private String token;

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
  @JoinColumn(name = "user_id")
  public AccountUser getUser() {
    return this.user;
  }
  public void setUser(AccountUser value) {
    this.user = value;
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getMessage() {
    return this.message;
  }
  public void setMessage(String value) {
    this.message = value;
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getSignature() {
    return this.signature;
  }
  public void setSignature(String value) {
    this.signature = value;
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
  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
  }
}

