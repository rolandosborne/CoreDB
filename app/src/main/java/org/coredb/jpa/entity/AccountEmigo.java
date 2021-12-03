package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.Amigo;
import org.coredb.model.AmigoEntry;
import org.coredb.model.LabelEntry;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;

@Entity
@Table(name = "emigo", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountEmigo extends AmigoEntry implements Serializable {
  private Integer id;
  private Account account;
  private List<AccountLabel> accountLabels;

  public AccountEmigo() {
    this.accountLabels = new ArrayList<AccountLabel>();
    super.setLabels(new ArrayList<String>());
  }

  public AccountEmigo(Account account, String emigoId) {
    this.account = account;
    super.setAmigoId(emigoId);
    super.setRevision(1);
    super.setLabels(new ArrayList<String>());
    this.accountLabels = new ArrayList<AccountLabel>();
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
  public String getEmigoId() {
    return super.getAmigoId();
  }
  public void setEmigoId(String value) {
    super.setAmigoId(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  public String getNotes() {
    return super.getNotes();
  }
  public void setNotes(String value) {
    super.setNotes(value);
  }

  @JsonIgnore
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name = "emigo_label", joinColumns = @JoinColumn(name = "emigo_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
  public List<AccountLabel> getAccountLabels() {
    return this.accountLabels;
  }
  public void setAccountLabels(List<AccountLabel> value) {
    this.accountLabels = value;
  }
}
