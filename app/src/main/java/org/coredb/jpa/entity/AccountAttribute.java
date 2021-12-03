package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.LabelEntry;
import org.coredb.model.Attribute;
import org.coredb.model.AttributeEntry;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountAttribute;
import org.coredb.jpa.entity.AccountLabel;

@Entity
@Table(name = "account_attribute", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountAttribute extends AttributeEntry implements Serializable {
  private Integer id;
  private Account account;  
  private List<AccountLabel> accountLabels;

  public AccountAttribute(Account account, String key, String value) {
    this.account = account;
    super.setAttribute(new Attribute());
    super.getAttribute().setAttributeId(UUID.randomUUID().toString().replace("-", ""));
    super.getAttribute().setSchema(key);
    super.getAttribute().setData(value);
    super.getAttribute().setRevision(0);
    super.setLabels(new ArrayList<String>());
    this.accountLabels = new ArrayList<AccountLabel>();
  }

  public AccountAttribute() {
    super.setAttribute(new Attribute());
    super.getAttribute().setAttributeId(UUID.randomUUID().toString().replace("-", ""));
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
  public String getAttributeId() {
    return super.getAttribute().getAttributeId();
  }
  public void setAttributeId(String value) {
    super.getAttribute().setAttributeId(value);
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
  public Integer getRevision() {
    return super.getAttribute().getRevision();
  }
  public void setRevision(Integer value) {
    super.getAttribute().setRevision(value);
  }

  @JsonIgnore
  public String getSchemaId() {
    return super.getAttribute().getSchema();
  }
  public void setSchemaId(String value) {
    super.getAttribute().setSchema(value);
  }

  @JsonIgnore
  public String getValue() {
    return super.getAttribute().getData();
  }
  public void setValue(String value) {
    super.getAttribute().setData(value);
  }

  @JsonIgnore
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name = "attribute_label", joinColumns = @JoinColumn(name = "account_attribute_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
  public List<AccountLabel> getAccountLabels() {
    return this.accountLabels;
  }
  public void setAccountLabels(List<AccountLabel> value) {
    this.accountLabels = value;
  }
}
