package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.AlertEntry;
import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "account_alert", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountAlert extends AlertEntry implements Serializable {
  private Integer id;
  private Account account;  

  public AccountAlert() { }

  public AccountAlert(Account account, String message) {
    Long cur = Instant.now().getEpochSecond();
    super.setAlertId(UUID.randomUUID().toString().replace("-", ""));
    super.setCreated(cur);
    super.setMessage(message);
    this.account = account;
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
  public String getAlertId() {
    return super.getAlertId();
  }
  public void setAlertId(String value) {
    super.setAlertId(value);
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
  public Long getCreated() {
    return super.getCreated();
  }
  public void setCreated(Long value) {
    super.setCreated(value);
  }

  @JsonIgnore
  public String getMessage() {
    return this.getMessage();
  }
  public void setMessage(String value) {
    this.setMessage(value);
  }

}
