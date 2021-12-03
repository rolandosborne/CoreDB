package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.AccountAppAgent;
import org.coredb.jpa.entity.AccountApp;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AppEntity;

public interface AccountAppAgentRepository 
      extends JpaRepository<AccountAppAgent, Integer>, JpaSpecificationExecutor<AccountAppAgent> {
  List<AccountAppAgent> findByEmigoAndToken(EmigoEntity emigo, String token);
  List<AccountAppAgent> findByToken(String token);
  Long countByEmigo(EmigoEntity emigo);
  Integer deleteByApp(AppEntity app);
  Integer deleteByEmigo(EmigoEntity emigo);
  Integer deleteByEmigoIn(List<EmigoEntity> emigos);
  Integer deleteByExpiresLessThan(Long expires);
}

