package org.coredb.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountAnswer;
import org.coredb.jpa.entity.AccountPrompt;

import org.coredb.model.PromptEntry;
import org.coredb.model.PromptQuestion;
import org.coredb.model.PromptAnswer;

import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountPromptRepository;
import org.coredb.jpa.repository.AccountAnswerRepository;

@Service
public class PromptService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountPromptRepository accountPromptRepository;
  
  @Autowired
  private AccountAnswerRepository accountAnswerRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private AccountPrompt getPrompt(Account account, String id) throws NotFoundException {
    
    // retrieve specified prompt
    AccountPrompt prompt = accountPromptRepository.findOneByAccountAndPromptId(account, id);
    if(prompt == null) {
      throw new NotFoundException(404, "account prompt not found");
    }
    return prompt;
  }

  @Transactional
  public PromptEntry addQuestion(Account account, PromptQuestion data) {

    // increment revision
    account.setPromptRevision(account.getPromptRevision() + 1);
    accountRepository.save(account);

    // create and save entry
    AccountPrompt prompt = new AccountPrompt(account, data);
    return accountPromptRepository.save(prompt);
  }

  @Transactional
  public void deleteQuestion(Account account, String promptId) throws NotFoundException {

    // increment revision
    account.setPromptRevision(account.getPromptRevision() + 1);
    accountRepository.save(account);

    // delete specified prompt
    accountPromptRepository.deleteByAccountAndPromptId(account, promptId);
  }

  public List<PromptEntry> getQuestions(Account account) {

    // get all questions from account
    List<AccountPrompt> prompts = accountPromptRepository.findByAccount(account);

    // return list as base class
    @SuppressWarnings("unchecked")
    List<PromptEntry> entries = (List<PromptEntry>)(List<?>)prompts;
    return entries;
  }

  @Transactional
  public PromptEntry updateQuestion(Account account, String promptId, PromptQuestion data) throws NotFoundException {

    // increment revision
    account.setPromptRevision(account.getPromptRevision() + 1);
    accountRepository.save(account);

    // retrieve specified entry return updated entry
    AccountPrompt prompt = getPrompt(account, promptId);
    prompt.setData(data);
    return accountPromptRepository.save(prompt);
  }

  @Transactional
  public PromptEntry addAnswer(Account account, String promptId, String value) throws NotFoundException {

    // increment revision
    account.setPromptRevision(account.getPromptRevision() + 1);
    accountRepository.save(account);

    // create and return saved result
    AccountPrompt prompt = getPrompt(account, promptId);
    AccountAnswer answer = new AccountAnswer(prompt, value);
    accountAnswerRepository.save(answer);

    return getPrompt(account, promptId);
  }

  @Transactional
  public PromptEntry deleteAnswer(Account account, String promptId, String answerId) throws NotFoundException {

    // increment revision
    account.setPromptRevision(account.getPromptRevision() + 1);
    accountRepository.save(account);
  
    // retrieve prompt and delete associated answer
    AccountPrompt prompt = getPrompt(account, promptId);
    accountAnswerRepository.deleteByPromptAndAnswerId(prompt, answerId);

    return getPrompt(account, promptId);
  }

}
