package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountSubjectLabelId;

public interface AccountSubjectLabelIdRepository extends JpaRepository<AccountSubjectLabelId, Integer> {
  Integer deleteByLabelId(Integer id);
  Integer deleteBySubjectId(Integer id);
  Integer deleteBySubjectIdAndLabelId(Integer subjectId, Integer labelId);
  List<AccountSubjectLabelId> findByLabelIdIn(List<Integer> ids);
}
 

