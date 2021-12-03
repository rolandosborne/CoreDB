package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;

public interface AccountLabelRepository 
      extends JpaRepository<AccountLabel, Integer>, JpaSpecificationExecutor<AccountLabel> {
  List<AccountLabel> findByAccount(Account account);
  List<AccountLabel> findByAccountOrderByName(Account account);
  AccountLabel findOneByAccountAndLabelId(Account account, String labelId);
  List<AccountLabel> findByAccountAndLabelIdIn(Account account, List<String> labelIds);
  Integer deleteByAccount(Account account);
}


