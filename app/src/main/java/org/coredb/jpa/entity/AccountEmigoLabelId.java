package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "emigo_label", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountEmigoLabelId implements Serializable {
  private Integer id;
  private Integer labelId;
  private Integer emigoId;

  public AccountEmigoLabelId() { }

  public AccountEmigoLabelId(Integer labelId, Integer emigoId) {
    this.labelId = labelId;
    this.emigoId = emigoId;
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

  @Column(name = "emigo_id")
  public Integer getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(Integer value) {
    this.emigoId = value;
  }

}
