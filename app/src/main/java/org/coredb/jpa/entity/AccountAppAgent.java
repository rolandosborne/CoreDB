package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.ServiceAccess;
import org.coredb.model.ServiceEntry;
import org.coredb.model.Amigo;

import org.coredb.jpa.entity.AccountAppAgent;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "service_agent", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountAppAgent  implements Serializable {
  private Integer id;
  private EmigoEntity emigo;
  private AppEntity app;
  private Long issued;
  private Long expires;
  private String token;

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
  @JoinColumn(name = "emigo_id")
  public EmigoEntity getEmigo() {
    return this.emigo;
  }
  public void setEmigo(EmigoEntity value) {
    this.emigo = value;
  }

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "service_id")
  public AppEntity getApp() {
    return this.app;
  }
  public void setApp(AppEntity value) {
    this.app = value;
  }

  @JsonIgnore
  public Long getIssued() {
    return this.issued;
  }
  public void setIssued(Long value) {
    this.issued = value;
  }

  @JsonIgnore
  public Long getExpires() {
    return this.expires;
  }
  public void setExpires(Long value) {
    this.expires = value;
  }

  @JsonIgnore
  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
  }
}  
