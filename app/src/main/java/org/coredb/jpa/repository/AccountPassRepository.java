package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountPass;

public interface AccountPassRepository extends JpaRepository<AccountPass, Integer> {
  List<AccountPass> findByPass(String pass);
  List<AccountPass> findByAccountAndPass(Account act, String pass);
  Integer deleteByAccount(Account act);
}


