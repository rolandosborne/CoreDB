package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountSubjectAsset;

public interface AccountSubjectAssetRepository 
      extends JpaRepository<AccountSubjectAsset, Integer>, JpaSpecificationExecutor<AccountSubjectAsset> {
  AccountSubjectAsset findOneBySubjectAndAssetId(AccountSubject subject, String assetId);
  Long countBySubject(AccountSubject subject);
  Integer deleteBySubject(AccountSubject subject);
  Integer deleteBySubjectIn(List<AccountSubject> subjects);
}


