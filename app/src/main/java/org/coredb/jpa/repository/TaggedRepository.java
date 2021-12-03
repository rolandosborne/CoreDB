package org.coredb.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Tagged;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.EmigoEntity;

public interface TaggedRepository 
      extends JpaRepository<Tagged, Integer>, JpaSpecificationExecutor<Tagged> {
  List<Tagged> findBySubjectAndSchemaIdOrderByCreatedDesc(AccountSubject subject, String schema);
  List<Tagged> findBySubjectAndSchemaIdOrderByCreatedAsc(AccountSubject subject, String schema);
  Integer deleteBySubjectAndEmigoAndTagId(AccountSubject subject, EmigoEntity emgio, String tagId);
  Integer deleteBySubjectAndTagId(AccountSubject subject, String tagId);
}


