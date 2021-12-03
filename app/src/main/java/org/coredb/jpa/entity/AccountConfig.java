package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.Config;
import org.coredb.model.ConfigEntry;
import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "account_config", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountConfig extends ConfigEntry implements Serializable {
  private Integer id;
  private Account account;  

  public AccountConfig(Account account, Config config, String configId) {
    super.setConfig(config);
    super.setConfigId(configId);
    this.account = account;
  }

  public AccountConfig(Account account, String configId) {
    super.setConfig(new Config());
    super.setConfigId(configId);
    this.account = account;
  }

  public AccountConfig() {
    super.setConfig(new Config());
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
  public String getConfigId() {
    return super.getConfigId();
  }
  public void setConfigId(String value) {
    super.setConfigId(value);
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
  public Long getNumValue() {
    return super.getConfig().getNumValue();
  }
  public void setNumValue(Long value) {
    super.getConfig().setNumValue(value);
  }

  @JsonIgnore
  public String getStrValue() {
    return super.getConfig().getStrValue();
  }
  public void setStrValue(String value) {
    super.getConfig().setStrValue(value);
  }

  @JsonIgnore
  public Boolean getBoolValue() {
    return super.getConfig().isBoolValue();
  }
  public void setBoolValue(Boolean value) {
    super.getConfig().setBoolValue(value);
  }

}
