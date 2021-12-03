package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.coredb.model.Blurb;

@Entity
@Table(name = "blurb", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class TopicBlurb extends Blurb implements Serializable {
  private Integer id;
  private AccountDialogue dialogue;
  private DialogueTopic topic;
  
  public TopicBlurb() {
    super.setBlurbId(UUID.randomUUID().toString().replace("-", ""));
    Long cur = Instant.now().getEpochSecond();
    super.setCreated(cur.intValue());
    super.setUpdated(cur.intValue());
    super.setRevision(1);
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
  @JoinColumn(name = "dialogue_id")
  public AccountDialogue getDialogue() {
    return this.dialogue;
  }
  public void setDialogue(AccountDialogue value) {
    this.dialogue = value;
  }

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "topic_id")
  public DialogueTopic getTopic() {
    return this.topic;
  }
  public void setTopic(DialogueTopic value) {
    this.topic = value;
  }

  @JsonIgnore
  @Column(name = "emigo_id")
  public String getAmigoId() {
    return super.getAmigoId();
  }
  public void setAmigoId(String value) {
    super.setAmigoId(value);
  }

  @JsonIgnore
  public String getBlurbId() {
    return super.getBlurbId();
  }
  public void setBlurbId(String value) {
    super.setBlurbId(value);
  }

  @JsonIgnore
  @Column(name = "schema_id")
  public String getSchema() {
    return super.getSchema();
  }
  public void setSchema(String value) {
    super.setSchema(value);
  }

  @JsonIgnore
  @Column(name = "value")
  public String getData() {
    return super.getData();
  }
  public void setData(String value) {
    super.setData(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  @Column(name = "modified")
  public Integer getUpdated() {
    return super.getUpdated();
  }
  public void setUpdated(Integer value) {
    super.setUpdated(value);
  }

  @JsonIgnore
  public Integer getCreated() {
    return super.getCreated();
  }
  public void setCreated(Integer value) {
    super.setCreated(value);
  }

}
