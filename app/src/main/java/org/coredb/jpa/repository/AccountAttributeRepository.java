package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountAttribute;

public interface AccountAttributeRepository 
      extends JpaRepository<AccountAttribute, Integer>, JpaSpecificationExecutor<AccountAttribute> {

  List<AccountAttribute> findByAccount(Account account);

  List<AccountAttribute> findDistinctByAccountAndSchemaIdInAndAccountLabelsIn(Account account, List<String> schemas, List<AccountLabel> labels);
  List<AccountAttribute> findDistinctByAccountAndSchemaIdIn(Account account, List<String> ids);

  AccountAttribute findOneByAccountAndAccountLabelsInAndAttributeId(Account account, List<AccountLabel> labels, String attributeId);
  AccountAttribute findOneByAccountAndAttributeId(Account account, String id);
  
  Integer deleteByAccountAndAttributeId(Account account, String id);
  Integer deleteByAccount(Account account);
}


