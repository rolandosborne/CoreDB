package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountPrompt;
import org.coredb.model.PromptAnswer;

@Entity
@Table(name = "account_answer", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountAnswer extends PromptAnswer implements Serializable {
  private Integer id;
  private AccountPrompt prompt;

  public AccountAnswer(AccountPrompt prompt, String value) {
    super.setAnswerId(UUID.randomUUID().toString().replace("-", ""));
    super.setData(value);
    this.prompt = prompt;
  }

  public AccountAnswer() {
    super.setAnswerId(UUID.randomUUID().toString().replace("-", ""));
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
  @JoinColumn(name = "prompt_id")
  public AccountPrompt getPrompt() {
    return this.prompt;
  }
  public void setPrompt(AccountPrompt value) {
    this.prompt = value;
  }

  @JsonIgnore
  public String getAnswerId() {
    return super.getAnswerId();
  }
  public void setAnswerId(String value) {
    super.setAnswerId(value);
  }

  @JsonIgnore
  public String getAnswer() {
    return super.getData();
  }
  public void setAnswer(String value) {
    super.setData(value);
  }

}
