package org.coredb.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.ConfigEntry;
import org.coredb.model.Config;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "config", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class ServerConfig extends ConfigEntry implements Serializable {
  private Integer id;

  public ServerConfig() {
    super.setConfig(new Config());
  }

  public ServerConfig(Config config) {
    super.setConfig(config);
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
  public String getConfigId() {
    return super.getConfigId();
  }
  public void setConfigId(String value) {
    super.setConfigId(value);
  }

  @JsonIgnore
  public String getStrValue() {
    return super.getConfig().getStrValue();
  }
  public void setStrValue(String value) {
    super.getConfig().setStrValue(value);
  }

  @JsonIgnore
  public Long getNumValue() {
    return super.getConfig().getNumValue();
  }
  public void setNumValue(Long value) {
    super.getConfig().setNumValue(value);
  }

  @JsonIgnore
  public Boolean getBoolValue() {
    return super.getConfig().isBoolValue();
  }
  public void setBoolValue(Boolean value) {
    super.getConfig().setBoolValue(value);
  }

}

