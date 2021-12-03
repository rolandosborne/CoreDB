package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AppEntity;
import org.coredb.jpa.entity.AccountApp;

public interface AccountAppRepository 
      extends JpaRepository<AccountApp, Integer>, JpaSpecificationExecutor<AccountApp> {
  List<AccountApp> findByAccount(Account account);
  AccountApp findOneByToken(String token);
  AccountApp findOneByAccountAndToken(Account account, String token);
  AccountApp findOneByAccountAndAppEntity(Account account, AppEntity entity);
  Long countByAccount(Account account);
  Integer deleteByAppEntity(AppEntity app);
  Integer deleteByAccount(Account account);
}


