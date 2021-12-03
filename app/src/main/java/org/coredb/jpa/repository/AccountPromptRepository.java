package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountPrompt;

public interface AccountPromptRepository 
      extends JpaRepository<AccountPrompt, Integer>, JpaSpecificationExecutor<AccountPrompt> {
  List<AccountPrompt> findByAccount(Account account);
  AccountPrompt findOneByAccountAndPromptId(Account account, String id);
  Integer deleteByAccountAndPromptId(Account account, String id);
  Integer deleteByAccount(Account account);
}


