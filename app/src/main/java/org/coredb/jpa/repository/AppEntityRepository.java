package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AppEntity;

public interface AppEntityRepository 
    extends JpaRepository<AppEntity, Integer>, JpaSpecificationExecutor<AppEntity> {
  List<AppEntity> findAll();
  AppEntity findOneByEmigoId(String emigoId);
  Long deleteByEmigoId(String emigoId);
  long count();
}

