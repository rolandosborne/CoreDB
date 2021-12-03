package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "share_prompt", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountSharePrompt implements Serializable {
  private Integer id;
  private Account account;
  private String promptId;
  private String emigoId;
  private String emigoMessageKey;
  private String emigoMessageKeyType;
  private String emigoMessageSignature;
  private String emigoMessageData;
  private String promptToken;
  private String shareToken;
  private Integer failCount;
  private Long expires;

  public AccountSharePrompt() { }

  public AccountSharePrompt(Account act) {
    this.account = act;
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
  public String getPromptId() {
    return this.promptId;
  }
  public void setPromptId(String value) {
    this.promptId = value;
  }

  @JsonIgnore
  public String getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(String value) {
    this.emigoId = value;
  }

  @JsonIgnore 
  @Column(length = 4096)
  public String getEmigoMessageKey() {
    return this.emigoMessageKey;
  }
  public void setEmigoMessageKey(String value) {
    this.emigoMessageKey = value;
  }

  @JsonIgnore
  public String getEmigoMessageKeyType() {
    return this.emigoMessageKeyType;
  }
  public void setEmigoMessageKeyType(String value) {
    this.emigoMessageKeyType = value;
  }

  @JsonIgnore
  @Column(length = 4096)
  public String getEmigoMessageSignature() {
    return this.emigoMessageSignature;
  }
  public void setEmigoMessageSignature(String value) {
    this.emigoMessageSignature = value;
  }

  @JsonIgnore
  public String getEmigoMessageData() {
    return this.emigoMessageData;
  }
  public void setEmigoMessageData(String value) {
    this.emigoMessageData = value;
  }

  @JsonIgnore
  public String getShareToken() {
    return this.shareToken;
  }
  public void setShareToken(String value) {
    this.shareToken = value;
  }

  @JsonIgnore
  public String getPromptToken() {
    return this.promptToken;
  }
  public void setPromptToken(String value) {
    this.promptToken = value;
  }

  @JsonIgnore
  public Integer getFailCount() {
    return this.failCount;
  }
  public void setFailCount(Integer value) {
    this.failCount = value;
  }

  @JsonIgnore
  public Long getExpires() {
    return this.expires;
  }
  public void setExpires(Long value) {
    this.expires = value;
  }
}
