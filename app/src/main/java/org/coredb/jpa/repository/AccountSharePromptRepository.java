package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountSharePrompt;
import org.coredb.jpa.entity.Account;

public interface AccountSharePromptRepository extends JpaRepository<AccountSharePrompt, Integer> {
  List<AccountSharePrompt> findByAccount(Account account);
  AccountSharePrompt findOneByPromptToken(String token);

  Long countByAccount(Account account);

  Integer deleteByAccountAndExpiresLessThan(Account account, Long expire);
  Integer deleteByAccount(Account account);
}

