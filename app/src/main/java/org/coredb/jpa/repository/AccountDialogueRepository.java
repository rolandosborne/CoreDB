package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountDialogue;

public interface AccountDialogueRepository 
      extends JpaRepository<AccountDialogue, Integer>, JpaSpecificationExecutor<AccountDialogue> {
  List<AccountDialogue> findByAccount(Account account);
  AccountDialogue findOneByAccountAndDialogueId(Account account, String dialogueId);
  Integer deleteByAccountAndDialogueId(Account account, String dialogueId);
  Long countByAccount(Account account);
  Integer deleteByAccount(Account account);
}


