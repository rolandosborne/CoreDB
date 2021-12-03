package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountUser;
import org.coredb.jpa.entity.Account;

public interface AccountUserRepository 
      extends JpaRepository<AccountUser, Integer>, JpaSpecificationExecutor<AccountUser> {
  List<AccountUser> findAll();
  List<AccountUser> findByAccount(Account account);
  AccountUser findOneByServiceToken(String token);
  AccountUser findOneByAccountAndEmigoId(Account account, String emigoId);
  Long countByAccount(Account account);
  Integer deleteByAccount(Account account);
}

