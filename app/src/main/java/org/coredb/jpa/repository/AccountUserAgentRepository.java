package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountUserAgent;
import org.coredb.jpa.entity.AccountUser;
import org.coredb.jpa.entity.Account;

public interface AccountUserAgentRepository 
      extends JpaRepository<AccountUserAgent, Integer>, JpaSpecificationExecutor<AccountUserAgent> {
  AccountUserAgent findOneByUser(AccountUser user);
  List<AccountUserAgent> findByUser(AccountUser user);
  Integer deleteByExpiresLessThan(Long expires);
  Integer deleteByUserIn(List<AccountUser> users);
}

