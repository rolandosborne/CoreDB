package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.jpa.entity.TopicBlurb;
import org.coredb.jpa.entity.DialogueTopic;
import org.coredb.jpa.entity.AccountDialogue;

public interface TopicBlurbRepository 
      extends JpaRepository<TopicBlurb, Integer>, JpaSpecificationExecutor<TopicBlurb> {
  List<TopicBlurb> findByTopicOrderByIdDesc(DialogueTopic topic);
  List<TopicBlurb> findByTopicAndSchemaOrderByIdDesc(DialogueTopic topic, String schema);
  TopicBlurb findOneByTopicAndBlurbId(DialogueTopic topic, String blurbId);
  TopicBlurb findOneByDialogueAndBlurbId(AccountDialogue dialogue, String blurbId);
  Integer deleteByDialogue(AccountDialogue dialogue);
  Integer countByTopic(DialogueTopic topic);
  Integer countByDialogue(AccountDialogue dialogue);
}


