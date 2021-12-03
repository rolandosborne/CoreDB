package org.coredb.jpa.entity;

import org.coredb.model.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "tag", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Tagged extends Tag implements Serializable {

  private Integer id;
  private EmigoEntity emigo;
  private AccountSubject subject;

  public Tagged() {
    super.setTagId(UUID.randomUUID().toString().replace("-", ""));
  }

  public Tagged(AccountSubject subject) {
    super.setTagId(UUID.randomUUID().toString().replace("-", ""));
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
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name = "emigo_id")
  public EmigoEntity getEmigo() {
    return this.emigo;
  }
  public void setEmigo(EmigoEntity value) {
    if(value == null) {
      super.setAmigoId(null);
      super.setAmigoName(null);
      super.setAmigoRegistry(null);
    }
    else {
      super.setAmigoId(value.getEmigoId());
      super.setAmigoName(value.getName());
      super.setAmigoRegistry(value.getRegistry());
    }
    this.emigo = value;
  }

  @JsonIgnore
  public String getSchemaId() {
    return super.getSchema();
  }
  public void setSchemaId(String value) {
    super.setSchema(value);
  }

  @JsonIgnore
  public String getData() {
    return super.getData();
  }
  public void setData(String value) {
    super.setData(value);
  }

  @JsonIgnore
  public String getTagId() {
    return super.getTagId();
  }
  public void setTagId(String value) {
    super.setTagId(value);
  }

  @JsonIgnore
  public Long getCreated() {
    return super.getCreated();
  }
  public void setCreated(Long value) {
    super.setCreated(value);
  }

}
