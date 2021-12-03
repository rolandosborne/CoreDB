package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.OriginalSubjectAsset;

public interface OriginalSubjectAssetRepository 
      extends JpaRepository<OriginalSubjectAsset, Integer>, JpaSpecificationExecutor<OriginalSubjectAsset> {
  Long countBySubject(AccountSubject subject);
  Integer deleteBySubject(AccountSubject subject);
  Integer deleteBySubjectIn(List<AccountSubject> subjects);
}


