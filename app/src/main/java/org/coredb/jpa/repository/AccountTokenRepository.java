package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountToken;

public interface AccountTokenRepository 
      extends JpaRepository<AccountToken, Integer>, JpaSpecificationExecutor<AccountToken> {
  AccountToken findOneByToken(String token);
  Long deleteByAccount(Account account);
}

