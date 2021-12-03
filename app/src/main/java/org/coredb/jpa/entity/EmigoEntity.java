package org.coredb.jpa.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.Amigo;

@Entity
@Table(name = "emigo_registry", uniqueConstraints = @UniqueConstraint(columnNames = { "emigo_id" }))
public class EmigoEntity extends Amigo implements Serializable {

  public EmigoEntity() {
  }

  public EmigoEntity(String emigoId) {
    super.setAmigoId(emigoId);
  }

  public EmigoEntity(Amigo emigo) {
    setEmigo(emigo);
  }

  @Transient
  public void setEmigo(Amigo emigo) {
    super.setAmigoId(emigo.getAmigoId());
    super.setName(emigo.getName());
    super.setDescription(emigo.getDescription());
    super.setLocation(emigo.getLocation());
    super.setLogo(emigo.getLogo());
    super.setRevision(emigo.getRevision());
    super.setVersion(emigo.getVersion());
    super.setHandle(emigo.getHandle());
    super.setRegistry(emigo.getRegistry());
    super.setNode(emigo.getNode());
  }

  @Id
  @Column(name = "emigo_id", updatable = false, nullable = false)
  @JsonIgnore
  public String getEmigoId() {
    return super.getAmigoId();
  }
  public void setEmigoId(String value) {
    super.setAmigoId(value);
  }

  @JsonIgnore
  public String getName() {
    return super.getName();
  }
  public void setName(String value) {
    super.setName(value);
  }

  @JsonIgnore
  public String getDescription() {
    return super.getDescription();
  }
  public void setDescription(String value) {
    super.setDescription(value);
  }

  @JsonIgnore
  public String getLocation() {
    return super.getLocation();
  }
  public void setLocation(String value) {
    super.setLocation(value);
  }

  @JsonIgnore
  public String getLogo() {
    return super.getLogo();
  }
  public void setLogo(String value) {
    super.setLogo(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  public String getVersion() {
    return super.getVersion();
  }
  public void setVersion(String value) {
    super.setVersion(value);
  }

  @JsonIgnore
  public String getHandle() {
    return super.getHandle();
  }
  public void setHandle(String value) {
    super.setHandle(value);
  }

  @JsonIgnore
  public String getNode() {
    return super.getNode();
  }
  public void setNode(String value) {
    super.setNode(value);
  }

  @JsonIgnore
  public String getRegistry() {
    return super.getRegistry();
  }
  public void setRegistry(String value) {
    super.setRegistry(value);
  }

}

