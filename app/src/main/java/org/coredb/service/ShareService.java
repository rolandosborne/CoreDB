package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.util.*;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.lang.IllegalStateException;

import org.coredb.NameRegistry;
import static org.coredb.NameRegistry.*;

import org.coredb.model.ShareStatus;
import org.coredb.model.ShareStatus.ShareStatusEnum;
import org.coredb.model.ShareEntry;
import org.coredb.model.ShareView;
import org.coredb.model.ShareEntry.StatusEnum;
import org.coredb.model.SharePrompt;
import org.coredb.model.PromptAnswer;
import org.coredb.model.ShareMessage;
import org.coredb.model.Share;
import org.coredb.model.Share.ActionEnum;
import org.coredb.model.Amigo;
import org.coredb.model.AmigoMessage;
import org.coredb.model.PendingAmigo;

import org.coredb.service.util.ShareStatusResponse;
import org.coredb.service.util.EmigoUtil;
import org.coredb.service.util.SecurityUtil;
import org.coredb.service.util.ShareUtil;
import org.coredb.service.util.ShareData;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountEmigoRepository;
import org.coredb.jpa.repository.AccountPromptRepository;
import org.coredb.jpa.repository.AccountMessageRepository;
import org.coredb.jpa.repository.AccountRejectEmigoRepository;
import org.coredb.jpa.repository.AccountShareRepository;
import org.coredb.jpa.repository.AccountSharePromptRepository;
import org.coredb.jpa.repository.AccountSharePendingRepository;
import org.coredb.jpa.entity.AccountShare;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.AccountEmigo;
import org.coredb.jpa.entity.AccountPrompt;
import org.coredb.jpa.entity.AccountAnswer;
import org.coredb.jpa.entity.AccountMessage;
import org.coredb.jpa.entity.AccountRejectEmigo;
import org.coredb.jpa.entity.AccountSharePrompt;
import org.coredb.jpa.entity.AccountSharePending;

@Service
public class ShareService {

  @Autowired
  private AccountEmigoRepository emigoRepository;

  @Autowired
  private AccountMessageRepository messageRepository;

  @Autowired
  private AccountRejectEmigoRepository rejectEmigoRepository;

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AccountSharePromptRepository sharePromptRepository;

  @Autowired
  private AccountSharePendingRepository sharePendingRepository;  

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountPromptRepository promptRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private IndexService indexService;

  @Autowired
  private KeyStoreService keyStoreService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private Map<String, List<Long>> throttle = new HashMap<String, List<Long>>();

  private AccountEmigo getAccountEmigo(Account account, String emigoId) throws NotFoundException {

    // find the referenced account emigo
    AccountEmigo emigo = emigoRepository.findOneByAccountAndEmigoId(account, emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "account emigo not found");
    }
    return emigo;
  }

  private Account getAccount(String emigoId) throws NotFoundException {
  
    // find the referecned account
    Account account = accountRepository.findOneByEmigoId(emigoId);
    if(account == null) {
      throw new NotFoundException(404, "account not found");
    }
    return account;
  }

  private Boolean isMessageReady(String emigoId) {

    // get current time
    Long cur = Instant.now().getEpochSecond();

    // add a new list if not present
    if(throttle.get(emigoId) == null) {
      throttle.put(emigoId, new ArrayList<Long>());
      throttle.get(emigoId).add(cur);
      return true;
    }

    // add to list of not full
    List<Long> entries = throttle.get(emigoId);
    Long interval = configService.getAccountNumValue(emigoId, AC_SHARE_MESSAGE_MAX_RATE_INTERVAL, (long)900);
    Long count = configService.getAccountNumValue(emigoId, AC_SHARE_MESSAGE_MAX_RATE_COUNT, (long)60);
    if(entries.size() < count) {
      entries.add(cur);
      return true;
    }

    // add to list if first is outside of window
    if(entries.get(0) + interval < cur) {
      entries.remove(0);
      entries.add(cur);
      return true;
    }

    return false;
  }

  public List<ShareView> getConnectionViews(Account account) {

    List<AccountShare> shares = shareRepository.findByAccount(account);
    
    List<ShareView> views = new ArrayList<ShareView>();
    for(AccountShare share: shares) {
      ShareView view = new ShareView();
      view.setShareId(share.getShareId());
      view.setRevision(share.getRevision());
      views.add(view);
    }
    return views;    
  }

  public List<ShareEntry> getConnections(Account account) {

    // retrieve entries for account
    List<AccountShare> shares = shareRepository.findByAccount(account);

    // return list as base class
    @SuppressWarnings("unchecked")
    List<ShareEntry> entries = (List<ShareEntry>)(List<?>)shares;
    return entries;
  }

  @Transactional
  public void deleteConnection(Account act, String shareId) {

    // update module revision
    act.setShareRevision(act.getShareRevision() + 1);
    accountRepository.save(act);

    // delete specified connection
    shareRepository.deleteByAccountAndShareId(act, shareId);

    // transfer any completed pending
    updatePending(act);
  }

  public ShareEntry getConnection(Account act, String shareId) 
        throws NotFoundException {
  
    // retreieve matching connections
    AccountShare share = shareRepository.findOneByAccountAndShareId(act, shareId);
    if(share == null) {
      throw new NotFoundException(404, "share entry not found");
    }
    return share;
  }

  @Transactional 
  public ShareEntry addConnectionById(Account act, String emigoId) 
        throws NotFoundException, Exception {

    // retrieve referenced emigo
    AccountEmigo emigo = getAccountEmigo(act, emigoId);

    // update module revision
    Integer revision = act.getShareRevision() + 1;
    act.setShareRevision(revision);
    accountRepository.save(act);

    // if share already exists
    AccountShare share = shareRepository.findOneByAccountAndEmigo(act, emigo);
    if(share == null) {
      share = new AccountShare(act, emigo);
      share.setState(StatusEnum.REQUESTING.toString());
      share.setInToken(SecurityUtil.token());
      share.setRevision(revision);
      share.setUpdated(Instant.now().getEpochSecond());
      share = shareRepository.save(share);
    }

    return share;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ShareEntry updateConnectionStatus(Account act, String shareId, String status, String token) 
        throws NotFoundException, Exception {

    Long cur = Instant.now().getEpochSecond();

    // retrieve specified share
    AccountShare share = shareRepository.findOneByAccountAndShareId(act, shareId);
    if(share == null) {
      throw new NotFoundException(404, "share not found");
    }

    // update module revision
    Integer revision = act.getShareRevision() + 1;
    act.setShareRevision(revision);
    accountRepository.save(act);

    if(status.equals(StatusEnum.REQUESTING.toString())) {
      // transition to requesting state
      if(!share.getState().equals(StatusEnum.REQUESTING.toString())) {
        share.setState(StatusEnum.REQUESTING.toString());
        share.setInToken(SecurityUtil.token());
        share.setUpdated(cur);
        share.setRevision(revision);
        share = shareRepository.save(share);
      }
    }
    else if(status.equals(StatusEnum.REQUESTED.toString())) {
      // transition to requested state
      if(share.getState().equals(StatusEnum.REQUESTING.toString())) {
        share.setState(StatusEnum.REQUESTED.toString());
        share.setUpdated(cur);
        share.setRevision(revision);
        share = shareRepository.save(share);
      }
      else if(!share.getState().equals(StatusEnum.CONNECTED.toString())) {
        throw new IllegalArgumentException("invalid status transition");
      }
    }
    else if(status.equals(StatusEnum.RECEIVED.toString())) {
      // transition to received state (nope)
      throw new IllegalArgumentException("invalid status transition");
    }
    else if(status.equals(StatusEnum.CONNECTED.toString())) {
      // transition to connected state
      if(share.getState().equals(StatusEnum.REQUESTING.toString())) {
        if(token == null) {
          throw new IllegalArgumentException("token must be set for connected status");
        }
        share.setOutToken(token);
        share.setState(StatusEnum.CONNECTED.toString());
        share.setUpdated(cur);
        share.setRevision(revision);
        share = shareRepository.save(share);
      }
      else {
        throw new IllegalArgumentException("invalid status transition");
      }
    }
    else if(status.equals(StatusEnum.CLOSING.toString())) {
      // transition to closing state
      share.setState(StatusEnum.CLOSING.toString());
      share.setUpdated(cur);
      share.setRevision(revision);
      share = shareRepository.save(share);
    }
    else if(status.equals(StatusEnum.CLOSED.toString())) {
      // transition to closed state
      if(share.getStatus() == StatusEnum.CLOSING) {
        share.setState(StatusEnum.CLOSED.toString());
        share.setUpdated(cur);
        share.setRevision(revision);
        share = shareRepository.save(share);
      }
      else {
        throw new IllegalArgumentException("invalid status transition");
      }
    }
    else {
      throw new IllegalArgumentException("unknown status specified");
    }

    return share;
  }

  @Transactional 
  public ShareStatus addAnswer(String token, String data) 
        throws NotFoundException, Exception {

    // retrieve specified prompt
    AccountSharePrompt share = sharePromptRepository.findOneByPromptToken(token);
    if(share == null) {
      throw new NotFoundException(404, "requested prompt not found");
    }
    
    // check if prompt has expired
    Long cur = Instant.now().getEpochSecond();
    if(share.getExpires() <= cur) {
      sharePromptRepository.delete(share);
      return new ShareStatusResponse(ShareStatusEnum.FAILED);
    }

    // extract referecnes
    Account act = share.getAccount();

    // get referenced prompt
    AccountPrompt prompt = promptRepository.findOneByAccountAndPromptId(act, share.getPromptId());
    if(prompt == null) {
      sharePromptRepository.delete(share);
      return new ShareStatusResponse(ShareStatusEnum.FAILED);
    }
    
    // compare answers
    for(AccountAnswer a: prompt.getResponses()) {
      if(a.getAnswer().equals(data)) {
        sharePromptRepository.delete(share);

        // create emigo message from entry
        AmigoMessage emigo = new AmigoMessage();
        emigo.setKey(share.getEmigoMessageKey());
        emigo.setKeyType(share.getEmigoMessageKeyType());
        emigo.setSignature(share.getEmigoMessageSignature());
        emigo.setData(share.getEmigoMessageData());

        // create share object from entry
        ShareStatus status = setOpenMessage(act, share.getEmigoId(), emigo, share.getShareToken(), true);

        // update module revision
        act.setShareRevision(act.getShareRevision() + 1);
        act.setIndexRevision(act.getIndexRevision() + 1);
        accountRepository.save(act);

        return status;
      }
    }

    // check if failed too many times
    share.setFailCount(share.getFailCount() + 1);
    if(share.getFailCount() >= configService.getAccountNumValue(act.getEmigoId(), AC_PROMPT_FAIL_MAX_COUNT, (long)3)) {
      sharePromptRepository.delete(share);
      return new ShareStatusResponse(ShareStatusEnum.FAILED);
    }
    sharePromptRepository.save(share);

    return new ShareStatusResponse(ShareStatusEnum.PENDING, token, prompt.getImage(), prompt.getQuestion());
  }
  
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ShareStatus setMessage(String emigoId, ShareMessage msg) 
        throws NotFoundException, IllegalStateException, IllegalArgumentException, Exception {
    
    // validate message rate
    if(!isMessageReady(emigoId)) {
      throw new IllegalStateException("message rate exceeded");
    }
    
    // decode share object
    ShareData data = ShareUtil.getObject(msg);

    // extract share parameters
    String token = data.getShare().getToken();
    AmigoMessage emigoMessage = data.getAmigoMessage();
    String eid = data.getEmigo().getAmigoId();

    // validate account
    Account act = getAccount(emigoId);

    // check if message has expired
    Long cur = Instant.now().getEpochSecond();
    Long skew = configService.getAccountNumValue(emigoId, AC_SHARE_MESSAGE_SKEW, (long)60);
    if(data.getShare().getIssued() > cur + skew || data.getShare().getExpires() + skew < cur) {
      throw new IllegalArgumentException("request not currently valid");
    }

    // check if emigo is allowed
    AccountRejectEmigo reject = rejectEmigoRepository.findOneByAccountAndEmigoId(act, eid);
    if(reject != null) {
      // silently ignore further rejected reqeusts
      return new ShareStatusResponse(ShareStatusEnum.RECEIVED);
    }

    // validate target of message
    if(!data.getShare().getAmigoId().equals(emigoId)) {
      throw new IllegalArgumentException("emigoId not target of message");
    }

    // invoke action
    if(data.getShare().getAction() == ActionEnum.OPEN) {
      ShareStatus status = setOpenMessage(act, eid, emigoMessage, token, false);

      // update module revision
      act.setShareRevision(act.getShareRevision() + 1);
      act.setIndexRevision(act.getIndexRevision() + 1);
      accountRepository.save(act);

      return status;

    }
    else if(data.getShare().getAction() == ActionEnum.CLOSE) {
      ShareStatus status = setCloseMessage(act, eid);

      // update module revision
      act.setShareRevision(act.getShareRevision() + 1);
      accountRepository.save(act);

      return status;
    }
    else {
      throw new IllegalArgumentException("unknown message action");
    }
  }

  private ShareStatus setCloseMessage(Account act, String emigoId) 
        throws NotFoundException {


    // retrieve share references
    AccountEmigo emigo = getAccountEmigo(act, emigoId);

    // set any matching connected share to closed
    AccountShare share = shareRepository.findOneByAccountAndEmigo(act, emigo);
    if(share != null) {
      if(share.getStatus() == StatusEnum.CONNECTED || share.getStatus() == StatusEnum.REQUESTING || share.getStatus() == StatusEnum.REQUESTED) {
        share.setStatus(StatusEnum.CLOSED);
        share.setUpdated(Instant.now().getEpochSecond());
        share.setRevision(act.getShareRevision() + 1);
        shareRepository.save(share);
      }
    }

    return new ShareStatusResponse(ShareStatusEnum.CLOSED);
  } 

  private AmigoMessage getAmigoMessage(Account act) throws NotFoundException {
    
    // retrieve emigo message
    AccountMessage message = messageRepository.findOneByAccount(act);
    if(message == null) {
      throw new NotFoundException(404, "emigo message not found for account");
    }
    return message;
  }

  public ShareMessage getMessage(Account act, String shareId) 
        throws NotFoundException, Exception {
  
    // retrieve account share
    AccountShare accountShare = shareRepository.findOneByAccountAndShareId(act, shareId);
    if(accountShare == null) {
      throw new NotFoundException(404, "share connection not found");
    }
  
    // extract id of emigo
    String eid = act.getEmigoId();

    // load key for signing message
    KeyPair key = keyStoreService.get(eid);

    // establish authorized window
    Long issued = Instant.now().getEpochSecond();
    Long expire = issued + configService.getAccountNumValue(eid, AC_SHARE_MESSAGE_EXPIRE, (long)300);

    // retrieve emigo message
    AmigoMessage emigoMessage = getAmigoMessage(act);

    // generate share object
    Share share = new Share();
    share.setAmigoId(accountShare.getEmigo().getAmigoId());
    share.setToken(accountShare.getInToken());
    share.setExpires(expire);
    share.setIssued(issued);

    if(accountShare.getStatus() == StatusEnum.REQUESTING) {
     
      // generate open message
      share.setAction(ActionEnum.OPEN);
      return ShareUtil.getMessage(key, share, emigoMessage);
    }
    else if(accountShare.getStatus() == StatusEnum.CLOSING) {
      
      // generate close message
      share.setAction(ActionEnum.CLOSE);
      return ShareUtil.getMessage(key, share, emigoMessage);
    }
    else {
      throw new IllegalArgumentException("connection state must be requesting or closing");
    }
  }

  private ShareStatus setOpenMessage(Account act, String emigoId, AmigoMessage msg, String token, Boolean confirmed) 
        throws NotFoundException, IllegalStateException, Exception {

    Integer shareRevision = act.getShareRevision() + 1;
    Long cur = Instant.now().getEpochSecond();

    // load share references
    String eid = act.getEmigoId();
    AccountEmigo emigo = emigoRepository.findOneByAccountAndEmigoId(act, emigoId);

    // auto add emigo if configured
    if(emigo == null && configService.getAccountBoolValue(eid, AC_AUTO_ADD_EMIGO, false)) {
      emigo = indexService.addAccountEmigo(act, msg);
    }

    if(emigo == null) {
     
      // check if already pending
      AccountSharePending pending = sharePendingRepository.findOneByAccountAndEmigoId(act, emigoId);
      if(pending != null) {
        return new ShareStatusResponse(ShareStatusEnum.RECEIVED);
      }

      // check if user should be prompted
      if(!confirmed && configService.getAccountBoolValue(eid, AC_PROMPT_UNKNOWN, false)) {

        List<AccountPrompt> prompts = promptRepository.findByAccount(act);
        if(!prompts.isEmpty()) {
        
          // delete any expired prompts
          sharePromptRepository.deleteByAccountAndExpiresLessThan(act, cur);

          // check if there is room on prompt queue
          Long promptCount = sharePromptRepository.countByAccount(act);
          if(promptCount >= configService.getAccountNumValue(eid, AC_PROMPT_REQUEST_MAX_COUNT, (long)128)) {
            throw new IllegalStateException("prompt queue is full");
          }

          // select prompt at random
          Integer idx = (int)(Math.random() % prompts.size());
          AccountPrompt entry = prompts.get(idx);
          
          // compute expiration
          Long expire = cur + configService.getAccountNumValue(eid, AC_PROMPT_REQUEST_EXPIRE, (long)300);

          // generate new prompt entry
          AccountSharePrompt prompt = new AccountSharePrompt(act);
          prompt.setPromptId(entry.getPromptId());
          prompt.setEmigoId(emigoId);
          prompt.setEmigoMessageKey(msg.getKey());
          prompt.setEmigoMessageKeyType(msg.getKeyType());
          prompt.setEmigoMessageSignature(msg.getSignature());
          prompt.setEmigoMessageData(msg.getData());
          prompt.setPromptToken(SecurityUtil.token());
          prompt.setShareToken(token);
          prompt.setFailCount(0);
          prompt.setExpires(expire);
          prompt = sharePromptRepository.save(prompt);

          // return pending status
          return new ShareStatusResponse(ShareStatusEnum.PENDING, prompt.getPromptToken(), entry.getImage(), entry.getQuestion());
        }
      }

      // check if there is room on pending list
      Long pendingCount = sharePendingRepository.countByAccount(act);
      if(pendingCount >= configService.getAccountNumValue(eid, AC_PENDING_REQUEST_MAX_COUNT, (long)128)) {
        throw new IllegalStateException("pending queue is full");
      }

      // add to pending list
      pending = new AccountSharePending(act);
      pending.setEmigoId(emigoId);
      pending.setEmigoMessageKey(msg.getKey());
      pending.setEmigoMessageKeyType(msg.getKeyType());
      pending.setEmigoMessageSignature(msg.getSignature());
      pending.setEmigoMessageData(msg.getData());
      pending.setUpdated(cur);
      pending.setRevision(shareRevision);
      pending = sharePendingRepository.save(pending);

      // return received status
      return new ShareStatusResponse(ShareStatusEnum.RECEIVED);
    }

    // check if already received
    AccountShare share = shareRepository.findOneByAccountAndEmigo(act, emigo);
    if(share != null) {

      // if requested set to connected
      if(share.getStatus() == StatusEnum.REQUESTING || share.getStatus() == StatusEnum.REQUESTED) {
        share.setStatus(StatusEnum.CONNECTED);
        share.setOutToken(token);
        share.setUpdated(cur);
        share.setRevision(shareRevision);
        share = shareRepository.save(share);
      }

      // return connected if status indicates
      if(share.getStatus() == StatusEnum.REQUESTING || share.getStatus() == StatusEnum.REQUESTED ||
            share.getStatus() == StatusEnum.CONNECTED) {
        return new ShareStatusResponse(ShareStatusEnum.CONNECTED, share.getInToken());
      }

      // otherwise just received
      share.setStatus(StatusEnum.RECEIVED);
      share.setOutToken(token);
      share.setUpdated(cur);
      share.setRevision(shareRevision);
      shareRepository.save(share);
      return new ShareStatusResponse(ShareStatusEnum.RECEIVED);
    }

    // check if there is room on share list
    Long count = shareRepository.countByAccount(act);
    if(count >= configService.getAccountNumValue(eid, AC_SHARE_CONNECTION_MAX_COUNT, (long)1048576)) {
      throw new IllegalStateException("share list is full");
    }

    // add new share entry
    share = new AccountShare(act, emigo);
    share.setInToken(SecurityUtil.token());
    share.setStatus(StatusEnum.RECEIVED);
    share.setUpdated(cur);
    share.setRevision(shareRevision);
    share = shareRepository.save(share);

    // return received statsu
    return new ShareStatusResponse(ShareStatusEnum.RECEIVED);
  }

  public AccountSharePending getPendingShare(Account act, String shareId) throws NotFoundException {
    
    // get pending request
    AccountSharePending share = sharePendingRepository.findOneByAccountAndShareId(act, shareId);
    if(share == null) {
      throw new NotFoundException(404, "pending share not found");
    }
    return share;
  }

  public List<PendingAmigo> getPendingAmigos(Account act) {

    List<AccountSharePending> shares = sharePendingRepository.findByAccount(act);

    // return list as base class
    @SuppressWarnings("unchecked")
    List<PendingAmigo> entries = (List<PendingAmigo>)(List<?>)shares;
    return entries;
  }

  @Transactional
  public void setPendingAmigo(Account act, AccountEmigo emigo) {

    // update module revision
    Integer revision = act.getShareRevision() + 1;
    act.setShareRevision(revision);
    accountRepository.save(act);

    AccountSharePending share = sharePendingRepository.findOneByAccountAndEmigoId(act, emigo.getEmigoId());
    if(share != null) {
      share.setEmigo(emigo);
      share.setUpdated(Instant.now().getEpochSecond());
      share.setRevision(revision);
      sharePendingRepository.save(share);
    }
    updatePending(act);
  }

  public ShareMessage getDenyMessage(Account act, String emigoId)
        throws NotFoundException, Exception {

    // extract id of emigo
    String eid = act.getEmigoId();

    // load key for signing message
    KeyPair key = keyStoreService.get(eid);

    // establish authorized window
    Long issued = Instant.now().getEpochSecond();
    Long expire = issued + configService.getAccountNumValue(eid, AC_SHARE_MESSAGE_EXPIRE, (long)300);

    // get emigo message of account
    AmigoMessage emigoMessage = getAmigoMessage(act);

    // generate share object
    Share share = new Share();
    share.setAmigoId(emigoId);
    share.setExpires(expire);
    share.setIssued(issued);
    share.setAction(ActionEnum.CLOSE);
    return ShareUtil.getMessage(key, share, emigoMessage);
  }

  @Transactional
  public void updatePending(Account act) {

    // update module revision
    Integer revision = act.getShareRevision() + 1;
    act.setShareRevision(revision);
    accountRepository.save(act);
    
    // get all completed pending
    List<AccountSharePending> pendings = sharePendingRepository.findByAccountAndEmigoIsNotNull(act);

    // move all completed pending
    for(AccountSharePending pending: pendings) {

      // cant transfer if full
      Long count = shareRepository.countByAccount(act);
      if(count >= configService.getAccountNumValue(act.getEmigoId(), AC_SHARE_CONNECTION_MAX_COUNT, (long)1048576)) {
        return;
      }

      // add new account share
      AccountShare share = new AccountShare(act, pending.getEmigo());
      share.setStatus(StatusEnum.RECEIVED);
      share.setUpdated(Instant.now().getEpochSecond());
      share.setRevision(revision);
      share = shareRepository.save(share);

      // delete share entry
      sharePendingRepository.delete(pending);
    }
  }
}

