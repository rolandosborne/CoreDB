package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Stat;

public interface StatRepository 
      extends JpaRepository<Stat, Integer>, JpaSpecificationExecutor<Stat> {
  List<Stat> findAllByOrderByTimestampDesc();
  Long deleteByTimestampLessThanEqual(Integer ts);
}

