package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attribute_label", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountAttributeLabelId implements Serializable {
  private Integer id;
  private Integer labelId;
  private Integer attributeId;

  public AccountAttributeLabelId() { }

  public AccountAttributeLabelId(Integer labelId, Integer attributeId) {
    this.labelId = labelId;
    this.attributeId = attributeId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  public Integer getId() {
    return this.id;
  }
  public void setId(Integer value) {
    this.id = value;
  }

  @Column(name = "label_id")
  public Integer getLabelId() {
    return this.labelId;
  }
  public void setLabelId(Integer value) {
    this.labelId = value;
  }

  @Column(name = "account_attribute_id")
  public Integer getAttributeId() {
    return this.attributeId;
  }
  public void setAttributeId(Integer value) {
    this.attributeId = value;
  }

}
