package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountMessage;

public interface AccountMessageRepository extends JpaRepository<AccountMessage, Integer> {
  List<AccountMessage> findAll();
  AccountMessage findOneByAccount(Account account);
  Integer deleteByAccount(Account act);
}

