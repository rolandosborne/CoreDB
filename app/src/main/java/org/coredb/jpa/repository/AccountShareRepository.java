package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.jpa.entity.Account;

public interface AccountShareRepository 
      extends JpaRepository<AccountShare, Integer>, JpaSpecificationExecutor<AccountShare> {
  List<AccountShare> findByAccount(Account account);
  AccountShare findOneByAccountAndEmigo(Account account, AccountEmigo emigo);
  AccountShare findOneByAccountAndShareId(Account account, String id);
  AccountShare findOneByInToken(String token);
  Long countByAccount(Account account);
  Integer deleteByEmigo(AccountEmigo emigo);
  Integer deleteByAccountAndShareId(Account account, String id);
  Integer deleteByAccount(Account account);
}

