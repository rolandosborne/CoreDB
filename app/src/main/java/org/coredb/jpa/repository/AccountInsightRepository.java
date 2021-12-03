package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountInsight;
import org.coredb.jpa.entity.EmigoEntity;

public interface AccountInsightRepository 
      extends JpaRepository<AccountInsight, Integer>, JpaSpecificationExecutor<AccountInsight> {
  List<AccountInsight> findByAccount(Account account);
  AccountInsight findOneByAccountAndEmigoAndDialogueId(Account account, EmigoEntity amigo, String dialogueId);
  Integer deleteByAccountAndDialogueId(Account account, String dialogueId);
  Long countByAccount(Account account);
  Integer deleteByAccount(Account account);
}


