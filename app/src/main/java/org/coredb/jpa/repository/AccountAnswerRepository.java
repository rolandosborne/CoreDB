package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountPrompt;
import org.coredb.jpa.entity.AccountAnswer;

public interface AccountAnswerRepository 
      extends JpaRepository<AccountAnswer, Integer>, JpaSpecificationExecutor<AccountAnswer> {
  List<AccountAnswer> findByPrompt(AccountPrompt prompt);
  AccountAnswer findByPromptAndAnswerId(AccountPrompt prompt, String id);
  Integer deleteByPromptIn(List<AccountPrompt> prompt);
  Integer deleteByPromptAndAnswerId(AccountPrompt prompt, String id);
}


