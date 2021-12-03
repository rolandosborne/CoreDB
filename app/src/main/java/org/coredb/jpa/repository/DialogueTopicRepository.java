package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.jpa.entity.AccountDialogue;
import org.coredb.jpa.entity.DialogueTopic;

public interface DialogueTopicRepository 
      extends JpaRepository<DialogueTopic, Integer>, JpaSpecificationExecutor<DialogueTopic> {
  List<DialogueTopic> findByDialogueOrderByIdDesc(AccountDialogue dialogue);
  DialogueTopic findTopByDialogueOrderByIdDesc(AccountDialogue dialogue);
  DialogueTopic findOneByDialogueAndTopicId(AccountDialogue dialogue, String topicId);
  Integer deleteByDialogue(AccountDialogue dialogue);
  Long countByDialogue(AccountDialogue dialogue);
}


