package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountConfig;

public interface AccountConfigRepository 
      extends JpaRepository<AccountConfig, Integer>, JpaSpecificationExecutor<AccountConfig> {
  List<AccountConfig> findAll();
  List<AccountConfig> findByAccount(Account account);
  List<AccountConfig> findByAccountAndConfigIdIn(Account account, Set<String> ids);
  AccountConfig findOneByAccountAndConfigId(Account account, String id);
  Integer deleteByAccountAndConfigId(Account account, String id);
  Integer deleteByAccount(Account account);
}


