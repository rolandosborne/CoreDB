package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.coredb.model.Dialogue;

@Entity
@Table(name = "dialogue", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountDialogue extends Dialogue implements Serializable {
  private Integer id;
  private Account account;
  private EmigoEntity amigo;
  
  public AccountDialogue() {
    super.setDialogueId(UUID.randomUUID().toString().replace("-", ""));
    super.setRevision(1);
    super.setSynced(true);
    super.setActive(true);
    super.setLinked(false);
    Long cur = Instant.now().getEpochSecond();
    super.setCreated(cur.intValue());
    super.setModified(cur.intValue());
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
  @JoinColumn(name = "account_id")
  public Account getAccount() {
    return this.account;
  }
  public void setAccount(Account value) {
    this.account = value;
  }

  @JsonIgnore
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name = "emigo_id")
  public EmigoEntity getEmigo() {
    return this.amigo;
  }
  public void setEmigo(EmigoEntity value) {
    if(value == null) {
      super.setAmigoId(null);
    }
    else {
      super.setAmigoId(value.getEmigoId());
    }
    this.amigo = value;
  }

  @JsonIgnore
  public String getDialogueId() {
    return super.getDialogueId();
  }
  public void setDialogueId(String value) {
    super.setDialogueId(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  public Boolean getLinked() {
    return super.isLinked();
  }
  public void setLinked(Boolean value) {
    super.setLinked(value);
  }

  @JsonIgnore
  public Boolean getSynced() {
    return super.isSynced();
  }
  public void setSynced(Boolean value) {
    super.setSynced(value);
  }

  @JsonIgnore
  public Boolean getActive() {
    return super.isActive();
  }
  public void setActive(Boolean value) {
    super.setActive(value);
  }

  @JsonIgnore
  public Integer getModified() {
    return super.getModified();
  }
  public void setModified(Integer value) {
    super.setModified(value);
  }

  @JsonIgnore
  public Integer getCreated() {
    return super.getCreated();
  }
  public void setCreated(Integer value) {
    super.setCreated(value);
  }

}
