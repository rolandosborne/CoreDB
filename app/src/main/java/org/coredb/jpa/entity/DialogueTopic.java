package org.coredb.jpa.entity;
  
import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.coredb.model.Topic;
import org.coredb.model.Blurb;

@Entity
@Table(name = "topic", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class DialogueTopic extends Topic implements Serializable {
  private Integer id;
  private AccountDialogue dialogue;
  private List<TopicBlurb> topicBlurbs;  

  public DialogueTopic() {
    super.setTopicId(UUID.randomUUID().toString().replace("-", ""));
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
  public String getTopicId() {
    return super.getTopicId();
  }
  public void setTopicId(String value) {
    super.setTopicId(value);
  }

  @JsonIgnore
  public Integer getRevision() {
    return super.getRevision();
  }
  public void setRevision(Integer value) {
    super.setRevision(value);
  }

  @JsonIgnore
  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name = "topic_id")
  public List<TopicBlurb> getTopicBlurbs() {
    return this.topicBlurbs;
  }
  public void setTopicBlurbs(List<TopicBlurb> value) {
    @SuppressWarnings("unchecked")
    List<Blurb> blurbs = (List<Blurb>)(List<?>)value;
    super.setBlurbs(blurbs);
    this.topicBlurbs = value;
  }

}
