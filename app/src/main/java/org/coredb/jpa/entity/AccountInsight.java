package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.coredb.model.Insight;

@Entity
@Table(name = "insight", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountInsight extends Insight implements Serializable {
  private Integer id;
  private Account account;
  private EmigoEntity amigo;
  
  public AccountInsight() {
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
      super.setAmigoRegistry(null);
    }
    else {
      super.setAmigoId(value.getEmigoId());
      super.setAmigoRegistry(value.getRegistry());
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

}
