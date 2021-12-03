package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.EmigoEntity;

public interface EmigoEntityRepository 
    extends JpaRepository<EmigoEntity, Integer>, JpaSpecificationExecutor<EmigoEntity> {
  List<EmigoEntity> findAll();
  List<EmigoEntity> findByEmigoId(String emigoId);
  EmigoEntity findOneByEmigoId(String emigoId);
  List<EmigoEntity> findByHandle(String handle);
  Long deleteByEmigoId(String emigoId);
  long count();
}

