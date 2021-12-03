package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.coredb.model.Subject;
import org.coredb.model.SubjectAsset;
import org.coredb.model.OriginalAsset;
import org.coredb.model.SubjectEntry;
import org.coredb.model.LabelEntry;

@Entity
@Table(name = "subject", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class AccountSubject extends SubjectEntry implements Serializable {
  private Integer id;
  private Account account;
  private List<AccountLabel> accountLabels;
  private Set<AccountSubjectAsset> assets;
  private Set<AccountSubjectAsset> pending;
  private Set<OriginalSubjectAsset> originals;
  private Set<Tagged> tags;
  private Boolean ready;
  private Integer tagRevision;
  
  public AccountSubject() {
    super.setSubject(new Subject());
    super.getSubject().setSubjectId(UUID.randomUUID().toString().replace("-", ""));
    super.setLabels(new ArrayList<String>());
    this.accountLabels = new ArrayList<AccountLabel>();
    this.assets = new HashSet<AccountSubjectAsset>(); 
    this.originals = new HashSet<OriginalSubjectAsset>(); 
    this.tags = new HashSet<Tagged>(); 
  }

  public AccountSubject(Account act) {
    Long cur = Instant.now().getEpochSecond();
    this.account = act;
    super.setSubject(new Subject());
    super.setShare(false);
    super.getSubject().setRevision(0);
    this.tagRevision = 0;
    super.getSubject().setCreated(cur);
    super.getSubject().setModified(cur);
    super.getSubject().setSubjectId(UUID.randomUUID().toString().replace("-", ""));
    super.setLabels(new ArrayList<String>());
    this.accountLabels = new ArrayList<AccountLabel>();
    this.assets = new HashSet<AccountSubjectAsset>(); 
    this.originals = new HashSet<OriginalSubjectAsset>();
    this.tags = new HashSet<Tagged>(); 
    super.getSubject().setExpires(cur + (long)315360000); //default 10y expire
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
  public String getSubjectId() {
    return super.getSubject().getSubjectId();
  }
  public void setSubjectId(String value) {
    super.getSubject().setSubjectId(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getSubject().getRevision();
  }
  public void setRevision(Integer value) {
    super.getSubject().setRevision(value);
  }

  @JsonIgnore
  public Integer getTagRevision() {
    return this.tagRevision;
  }
  public void setTagRevision(Integer value) {
    this.tagRevision = value;
  }

  @JsonIgnore
  public Long getCreated() {
    return super.getSubject().getCreated();
  }
  public void setCreated(Long value) {
    super.getSubject().setCreated(value);
  }

  @JsonIgnore
  public Long getModified() {
    return super.getSubject().getModified();
  }
  public void setModified(Long value) {
    super.getSubject().setModified(value);
  }

  @JsonIgnore
  public Long getExpires() {
    return super.getSubject().getExpires();
  }
  public void setExpires(Long value) {
    if(value == null) {
      Long cur = Instant.now().getEpochSecond();
      super.getSubject().setExpires(cur + (long)315360000); //default 10y expire
    }
    else {
      super.getSubject().setExpires(value);
    }
  }

  @JsonIgnore
  @Column(name = "viewable")
  public Boolean getShare() {
    return super.isShare();
  }
  public void setShare(Boolean value) {
    super.setShare(value);
  }

  @JsonIgnore
  public String getSchemaId() {
    return super.getSubject().getSchema();
  }
  public void setSchemaId(String value) {
    super.getSubject().setSchema(value);
  }

  @JsonIgnore
  public String getValue() {
    return super.getSubject().getData();
  }
  public void setValue(String value) {
    super.getSubject().setData(value);
  }

  @JsonIgnore
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name = "subject_label", joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
  public List<AccountLabel> getAccountLabels() {
    return this.accountLabels;
  }
  public void setAccountLabels(List<AccountLabel> value) {
    this.accountLabels = value;
  }

  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "subject_id")
  public Set<AccountSubjectAsset> getSubjectAssets() {
    return this.assets;
  }
  public void setSubjectAssets(Set<AccountSubjectAsset> value) {
    @SuppressWarnings("unchecked")
    Set<SubjectAsset> entry = (Set<SubjectAsset>)(Set<?>)value;
    super.setAssets(new ArrayList<SubjectAsset>(entry));
    this.assets = value;
  } 

  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "pending_id")
  public Set<AccountSubjectAsset> getPending() {
    return this.pending;
  }
  public void setPending(Set<AccountSubjectAsset> value) {
    this.pending = value;
  }
 
  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "subject_id")
  public Set<OriginalSubjectAsset> getOriginalAssets() {
    return this.originals;
  }
  public void setOriginalAssets(Set<OriginalSubjectAsset> value) {
    @SuppressWarnings("unchecked")
    Set<OriginalAsset> entry = (Set<OriginalAsset>)(Set<?>)value;
    super.setOriginals(new ArrayList<OriginalAsset>(entry));
    this.originals = value;
  }
 
  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "subject_id")
  public Set<Tagged> getTags() {
    return this.tags;
  }
  public void setTags(Set<Tagged> value) {
    this.tags = value;
  }


}
