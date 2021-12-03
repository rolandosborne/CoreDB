package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLogin;

public interface AccountLoginRepository 
      extends JpaRepository<AccountLogin, Integer>, JpaSpecificationExecutor<AccountLogin> {
  AccountLogin findOneByUsername(String username);
  AccountLogin findOneByAccount(Account account);
  Long deleteByAccount(Account account);
}

