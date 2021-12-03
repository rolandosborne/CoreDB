package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.ServerConfig;

public interface ServerConfigRepository 
      extends JpaRepository<ServerConfig, Integer>, JpaSpecificationExecutor<ServerConfig> {
  List<ServerConfig> findAll();
  ServerConfig findOneByConfigId(String id);
  Long deleteByConfigId(String id);
}

