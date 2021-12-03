package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountSharePending;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.Account;

public interface AccountSharePendingRepository extends JpaRepository<AccountSharePending, Integer> {
  List<AccountSharePending> findByAccount(Account account);
  List<AccountSharePending> findByAccountAndEmigoIsNotNull(Account act);

  AccountSharePending findOneByAccountAndEmigoId(Account account, String emigoId);
  AccountSharePending findOneByAccountAndShareId(Account account, String shareId);

  Long countByAccount(Account account);

  Integer deleteByEmigo(AccountEmigo emigo);
  Integer deleteByAccountAndEmigoId(Account account, String emigoId);
  Integer deleteByAccountAndShareId(Account account, String shareId);
  Integer deleteByAccount(Account account);
}

