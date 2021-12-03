package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subject_label", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountSubjectLabelId implements Serializable {
  private Integer id;
  private Integer labelId;
  private Integer accountSubjectId;

  public AccountSubjectLabelId() { }

  public AccountSubjectLabelId(Integer subjectId, Integer labelId) {
    this.accountSubjectId = subjectId;
    this.labelId = labelId;
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

  @Column(name = "subject_id")
  public Integer getSubjectId() {
    return this.accountSubjectId;
  }
  public void setSubjectId(Integer value) {
    this.accountSubjectId = value;
  }

}
