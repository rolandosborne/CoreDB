package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.jpa.entity.Account;

@Entity
@Table(name = "reject_emigo", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountRejectEmigo implements Serializable {
  private Integer id;
  private Account account;
  private String emigoId;

  public AccountRejectEmigo() { }

  public AccountRejectEmigo(Account act, String id) {
    this.account = act;
    this.emigoId = id;
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
  public String getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(String value) {
    this.emigoId = value;
  }

}
