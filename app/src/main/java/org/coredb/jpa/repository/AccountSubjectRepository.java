package org.coredb.jpa.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Set;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountSubjectAsset;

public interface AccountSubjectRepository 
      extends JpaRepository<AccountSubject, Integer>, JpaSpecificationExecutor<AccountSubject> {

  List<AccountSubject> findByAccount(Account account);
  AccountSubject findOneByAccountAndSubjectId(Account account, String subjectId);
  List<AccountSubject> findByAccountAndSubjectId(Account account, String subjectId);

  AccountSubject findOneBySubjectId(String subjectid);

  AccountSubject findOneByShareAndSubjectIdAndAccountLabelsInAndPendingIsNull(Boolean share, String subjectId, List<AccountLabel> labels);

  List<AccountSubject> findDistinctByAccountAndShareAndSchemaIdInAndAccountLabelsInAndExpiresGreaterThanAndPendingIsNullOrderByModifiedDesc(Account account, Boolean share, List<String> schemas, List<AccountLabel> labels, Long expire);
  AccountSubject findOneByAccountAndShareAndAccountLabelsInAndSubjectIdAndExpiresGreaterThanAndPendingIsNullOrderByModifiedDesc(Account account, Boolean share, List<AccountLabel> labels, String subjectId, Long expire);

  AccountSubject findOneByAccountAndShareAndAccountLabelsInAndSubjectIdAndExpiresGreaterThanAndPendingIsNull(Account account, Boolean share, List<AccountLabel> labels, String subjectId, Long expires);

  List<AccountSubject> findDistinctByAccountAndShareAndSchemaIdInAndAccountLabelsInAndPendingIsNullOrderByModifiedDesc(Account account, Boolean share, List<String> schemas, List<AccountLabel> labels, Specification spec);
  AccountSubject findOneByAccountAndShareAndAccountLabelsInAndSubjectIdAndPendingIsNullOrderByModifiedDesc(Account account, Boolean share, List<AccountLabel> labels, String subjectId, Specification spec);

  List<AccountSubject> findByAccountAndShareAndSchemaIdInAndPendingIsNullOrderByModifiedDesc(Account account, Boolean share, List<String> schemas);
  List<AccountSubject> findByAccountAndShareAndSchemaIdInAndPendingIsNotNullOrderByModifiedDesc(Account account, Boolean share, List<String> schemas);
  List<AccountSubject> findByAccountAndSchemaIdInAndPendingIsNullOrderByModifiedDesc(Account account, List<String> schemas);
  List<AccountSubject> findByAccountAndSchemaIdInAndPendingIsNotNullOrderByModifiedDesc(Account account, List<String> schemas);
  List<AccountSubject> findByAccountAndShareAndSchemaIdInOrderByModifiedDesc(Account account, Boolean share, List<String> schemas);
  List<AccountSubject> findByAccountAndSchemaIdInOrderByModifiedDesc(Account account, List<String> schemas);

  List<AccountSubject> findDistinctByShareAndSchemaIdInAndAccountLabelsInAndPendingIsNullOrderByModifiedDesc(Boolean share, List<String> schemas, List<AccountLabel> labels);
  List<AccountSubject> findDistinctByShareAndAccountLabelsInAndPendingIsNullOrderByModifiedDesc(Boolean share, List<AccountLabel> labels);

  List<AccountSubject> findByAccountAndSchemaIdIn(Account account, List<String> schemas);

  List<AccountSubject> findByAccountOrderByModifiedDesc(Account account);
  List<AccountSubject> findByAccountAndPendingIsNullOrderByModifiedDesc(Account account);
  List<AccountSubject> findByAccountAndPendingIsNotNullOrderByModifiedDesc(Account account);

  Long countByAccount(Account account);
  Integer deleteByAccount(Account account);

  List<AccountSubject> findAll();
}


