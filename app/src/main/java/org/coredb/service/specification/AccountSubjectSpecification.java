package org.coredb.service.specification;

import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.Timestamp;

import org.coredb.jpa.entity.AccountSubject;
import javax.persistence.criteria.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class AccountSubjectSpecification implements Specification<AccountSubject> {
  private Long expire;

  public AccountSubjectSpecification(Long expire) {
    this.expire = expire;
  }

  @Override
  public Predicate toPredicate (Root<AccountSubject> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    Predicate unset = builder.isNull(root.<Long>get("expires"));
    Predicate less = builder.greaterThan(root.<Long>get("expires"), this.expire);
    return builder.or(unset, less);
  }
}

