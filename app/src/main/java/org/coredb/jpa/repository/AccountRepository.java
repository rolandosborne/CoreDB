package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.EmigoEntity;

public interface AccountRepository 
      extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
  List<Account> findAll();
  Account findOneByEmigoId(String emigoId);
  long count();
}

