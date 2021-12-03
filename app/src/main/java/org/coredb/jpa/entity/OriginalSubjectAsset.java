package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.OriginalAsset;

@Entity
@Table(name = "upload", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class OriginalSubjectAsset extends OriginalAsset implements Serializable {
  private Integer id;
  private AccountSubject subject;
  private String status;

  public OriginalSubjectAsset() { }

  public OriginalSubjectAsset(AccountSubject subject) {
    this.subject = subject;
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

  @JsonIgnore
  public String getAssetId() {
    return super.getAssetId();
  }
  public void setAssetId(String value) {
    super.setAssetId(value);
  }

  @JsonIgnore
  public String getOriginalName() {
    return super.getOriginalName();
  }
  public void setOriginalName(String value) {
    super.setOriginalName(value);
  }

  @JsonIgnore
  public String getStatus() {
    return super.getState().toString();
  }
  public void setStatus(String value) {
    super.setState(StateEnum.fromValue(value));
  }

  @JsonIgnore
  public String getHash() {
    return super.getHash();
  }
  public void setHash(String value) {
    super.setHash(value);
  }

  @JsonIgnore
  public Long getSize() {
    return super.getSize();
  }
  public void setSize(Long value) {
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

