package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountEmigo;

import org.coredb.model.AmigoMessage;
import org.coredb.model.PendingAmigo;

@Entity
@Table(name = "share_pending", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountSharePending extends PendingAmigo implements Serializable {
  private Integer id;
  private Account account;
  private AccountEmigo emigo;
  private String emigoId;

  public AccountSharePending() { 
    super.setMessage(new AmigoMessage());
  }

  public AccountSharePending(Account act) {
    this.account = act;
    super.setShareId(UUID.randomUUID().toString().replace("-", ""));
    super.setMessage(new AmigoMessage());
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
  @ManyToOne(optional = true)
  @JoinColumn(name = "emigo_id")
  public AccountEmigo getEmigo() {
    return this.emigo;
  }
  public void setEmigo(AccountEmigo value) {
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
  @Column(name="emigo")
  public String getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(String value) {
    this.emigoId = value;
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getEmigoMessageKey() {
    return super.getMessage().getKey();
  }
  public void setEmigoMessageKey(String value) {
    super.getMessage().setKey(value);
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getEmigoMessageKeyType() {
    return super.getMessage().getKeyType();
  }
  public void setEmigoMessageKeyType(String value) {
    super.getMessage().setKeyType(value);
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getEmigoMessageSignature() {
    return super.getMessage().getSignature();
  }
  public void setEmigoMessageSignature(String value) {
    super.getMessage().setSignature(value);
  }

  @JsonIgnore
  @Column(length = 65535)
  public String getEmigoMessageData() {
    return super.getMessage().getData();
  }
  public void setEmigoMessageData(String value) {
    super.getMessage().setData(value);
  }

}
