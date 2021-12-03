package org.coredb.jpa.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountRejectEmigo;
import org.coredb.jpa.entity.Account;

public interface AccountRejectEmigoRepository extends JpaRepository<AccountRejectEmigo, Integer> {
  AccountRejectEmigo findOneByAccountAndEmigoId(Account account, String emigoId);
  List<AccountRejectEmigo> findByAccount(Account account);
  Integer deleteByAccountAndEmigoId(Account account, String emigoId);
}

