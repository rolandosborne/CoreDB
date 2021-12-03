package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountAttributeLabelId;

public interface AccountAttributeLabelIdRepository extends JpaRepository<AccountAttributeLabelId, Integer> {
  List<AccountAttributeLabelId> findByLabelIdIn(List<Integer> ids);
  Integer deleteByAttributeId(Integer attribute);
  Integer deleteByLabelId(Integer label);
  Integer deleteByAttributeIdAndLabelId(Integer attribute, Integer label);
}
 

