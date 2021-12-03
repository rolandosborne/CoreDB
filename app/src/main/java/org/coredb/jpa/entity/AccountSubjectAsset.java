package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.SubjectAsset;

@Entity
@Table(name = "subject_asset", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountSubjectAsset extends SubjectAsset implements Serializable {
  private Integer id;
  private AccountSubject subject;
  private AccountSubject pending;

  public AccountSubjectAsset() { }

  public AccountSubjectAsset(AccountSubject subject) {
    this.subject = subject;
    super.setAssetId(UUID.randomUUID().toString().replace("-", ""));
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
  @JoinColumn(name = "subject_id")
  public AccountSubject getSubject() {
    return this.subject;
  }
  public void setSubject(AccountSubject value) {
    this.subject = value;
  }

  // this clunky field, but i dont know of another way to
  // know if there are any pending assets in a repository query

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "pending_id")
  public AccountSubject getPending() {
    return this.pending;
  }
  public void setPending(AccountSubject value) {
    this.pending = value;
  }

  @JsonIgnore
  public String getOriginalId() {
    return super.getOriginalId();
  }
  public void setOriginalId(String value) {
    super.setOriginalId(value);
  }

  @JsonIgnore
  public String getAssetId() {
    return super.getAssetId();
  }
  public void setAssetId(String value) {
    super.setAssetId(value);
  }

  @JsonIgnore
  public String getTransform() {
    return super.getTransform();
  }
  public void setTransform(String value) {
    super.setTransform(value);
  }

  @JsonIgnore
  @Column(nullable = false)
  public String getStatus() {
    if(super.getState() == null) {
      System.out.println("using default state value");
      return "pending";
    }
    return super.getState().toString();
  }
  public void setStatus(String value) {
    super.setState(StateEnum.fromValue(value));
  }

  @JsonIgnore
  public String getAssetHash() {
    return super.getHash();
  }
  public void setAssetHash(String value) {
    super.setHash(value);
  }

  @JsonIgnore
  public Long getAssetSize() {
    return super.getSize();
  }
  public void setAssetSize(Long value) {
    super.setSize(value);
  }

  @JsonIgnore
  public Long getCreated() {
    return super.getCreated();
  }
  public void setCreated(Long value) {
    super.setCreated(value);
  }

}

