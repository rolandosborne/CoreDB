package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountEmigo;

public interface AccountEmigoRepository 
      extends JpaRepository<AccountEmigo, Integer>, JpaSpecificationExecutor<AccountEmigo> {
  List<AccountEmigo> findByAccount(Account account);
  AccountEmigo findOneByAccountAndEmigoId(Account account, String emigoId);
  Integer deleteByAccountAndEmigoId(Account account, String emigoId);
  Integer deleteByAccount(Account account);
}


