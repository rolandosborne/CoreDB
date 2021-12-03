package org.coredb.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import org.coredb.model.AmigoMessage;

@Entity
@Table(name = "account_message", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountMessage extends AmigoMessage implements Serializable {
  private Integer id;
  private Account account;

  public AccountMessage() {
  }

  public AccountMessage(Account account) {
    this.account = account;
  }

  public AccountMessage(Account account, AmigoMessage message) {
    this.account = account;
    super.setKey(message.getKey());
    super.setKeyType(message.getKeyType());
    super.setData(message.getData());
    super.setSignature(message.getSignature());
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
  @Column(length = 65535)
  public String getMessage() {
    return super.getData();
  }
  public void setMessage(String value) {
    super.setData(value);;
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getSignature() {
    return super.getSignature();
  }
  public void setSignature(String value) {
    super.setSignature(value);
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getPubkey() {
    return super.getKey();
  }
  public void setPubkey(String value) {
    super.setKey(value);
  }

  @JsonIgnore
  @Column(length = 32)
  public String getPubkeyType() {
    return super.getKeyType();
  }
  public void setPubkeyType(String value) {
    super.setKeyType(value);
  }
}
