package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountPrompt;
import org.coredb.jpa.entity.AccountAnswer;

import org.coredb.model.PromptEntry;
import org.coredb.model.PromptQuestion;
import org.coredb.model.PromptAnswer;

@Entity
@Table(name = "account_prompt", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountPrompt extends PromptEntry implements Serializable {
  private Integer id;
  private Account account;
  private List<AccountAnswer> responses;

  public AccountPrompt() {
    super.setPromptId(UUID.randomUUID().toString().replace("-", ""));
    super.setData(new PromptQuestion());
    super.setAnswers(new ArrayList<PromptAnswer>());
  }

  public AccountPrompt(Account account, PromptQuestion question) {
    this.account = account;
    super.setPromptId(UUID.randomUUID().toString().replace("-", ""));
    super.setData(question); 
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
    return super.getPromptId();
  }
  public void setPromptId(String value) {
    super.setPromptId(value);
  }

  @JsonIgnore
  public String getQuestion() {
    return super.getData().getText();
  }
  public void setQuestion(String value) {
    super.getData().setText(value);
  }

  @JsonIgnore
  public String getImage() {
    return super.getData().getImage();
  }
  public void setImage(String value) {
    super.getData().setImage(value);
  }

  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "prompt_id")
  public List<AccountAnswer> getResponses() {
    return this.responses;
  }
  public void setResponses(List<AccountAnswer> value) {
    @SuppressWarnings("unchecked")
    List<PromptAnswer> answers = (List<PromptAnswer>)(List<?>)value;
    super.setAnswers(answers);
    this.responses = value;
  }

}
