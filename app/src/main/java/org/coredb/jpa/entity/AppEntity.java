package org.coredb.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import org.coredb.model.Amigo;
import org.coredb.model.Service;

@Entity
@Table(name = "service_registry", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AppEntity extends Service implements Serializable {
  private Integer id;

  public AppEntity(String emigoId) {
    super.setAmigoId(emigoId);
    super.setEnabled(true);
  }

  public AppEntity() {
    super.setEnabled(true);
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
  public String getEmigoId() {
    return super.getAmigoId();
  }
  public void setEmigoId(String value) {
    super.setAmigoId(value);
  }

  @JsonIgnore
  public Boolean getEnabled() {
    return super.isEnabled();
  }
  public void setEnabled(Boolean value) {
    super.setEnabled(value);
  }

}  
