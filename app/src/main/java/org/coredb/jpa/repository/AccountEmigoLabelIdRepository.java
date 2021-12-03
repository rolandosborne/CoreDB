package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountEmigoLabelId;

public interface AccountEmigoLabelIdRepository extends JpaRepository<AccountEmigoLabelId, Integer> {
  Integer deleteByEmigoId(Integer emigo);
  Integer deleteByLabelId(Integer label);
  Integer deleteByEmigoIdAndLabelId(Integer emigo, Integer label);
}
 

