package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountAlert;

public interface AccountAlertRepository 
      extends JpaRepository<AccountAlert, Integer>, JpaSpecificationExecutor<AccountAlert> {
  List<AccountAlert> findByAccount(Account account);
  AccountAlert findOneByAccountAndAlertId(Account account, String id);
  Integer deleteByAccountAndAlertId(Account account, String id);
  Integer deleteByAccount(Account account);
}


