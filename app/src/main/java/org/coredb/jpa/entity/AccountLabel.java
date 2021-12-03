package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.LabelEntry;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;

@Entity
@Table(name = "label", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountLabel extends LabelEntry implements Serializable {
  private Integer id;
  private Account account;

  public AccountLabel(Account account, String name) {
    super.setLabelId(UUID.randomUUID().toString().replace("-", ""));
    this.account = account;
    super.setRevision(1);
    super.setName(name);
  }

  public AccountLabel() { }

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
  public String getLabelId() {
    return super.getLabelId();
  }
  public void setLabelId(String value) {
    super.setLabelId(value);
  }

  @JsonIgnore
  public String getName() {
    return super.getName();
  }
  public void setName(String value) {
    super.setName(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

}
