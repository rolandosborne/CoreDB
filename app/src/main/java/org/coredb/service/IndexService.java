package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.core.io.InputStreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ws.rs.ForbiddenException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.codec.DecoderException;
import java.nio.charset.StandardCharsets;

import java.lang.IllegalArgumentException;
import org.coredb.api.NotFoundException;

import org.coredb.model.*;
import org.coredb.jpa.entity.*;
import org.coredb.jpa.repository.*;

import org.coredb.service.util.EmigoUtil;

@Service
public class IndexService {

  @Autowired
  private EmigoEntityRepository emigoEntityRepository;

  @Autowired
  private AccountEmigoRepository accountEmigoRepository;

  @Autowired
  private AccountLabelRepository accountLabelRepository;

  @Autowired
  private AccountEmigoLabelIdRepository accountEmigoLabelIdRepository;

  @Autowired
  private AccountAppAgentRepository agentRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountShareRepository shareRepository;

  @Autowired
  private AccountRejectEmigoRepository rejectEmigoRepository;

  @Autowired
  private AccountSharePendingRepository sharePendingRepository;

  @Autowired
  private ShareService shareService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public AccountEmigo getAccountEmigo(Account account, String emigoId) throws NotFoundException {

    // find the referenced account emigo
    AccountEmigo emigo = accountEmigoRepository.findOneByAccountAndEmigoId(account, emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "account emigo not found");
    }
    return emigo;
  }

  private AccountLabel getAccountLabel(Account account, String labelId) throws NotFoundException {

    // find the referecnced account laebel
    AccountLabel label = accountLabelRepository.findOneByAccountAndLabelId(account, labelId);
    if(label == null) {
      throw new NotFoundException(404, "account label not found");
    }
    return label;
  }

  private AmigoEntry setAmigoEntry(AccountEmigo emigo) {

    // convert accountlabel list to base class list of ids
    for(AccountLabel l: emigo.getAccountLabels()) {
      emigo.addLabelsItem(l.getLabelId());
    } 
    return emigo;
  }

  private List<AmigoEntry> setEmigoEntries(List<AccountEmigo> emigos) {

    // return list as base class
    List<AmigoEntry> entries = new ArrayList<AmigoEntry>();
    for(AccountEmigo emigo: emigos) {
      entries.add(setAmigoEntry(emigo));
    }
    return entries;
  }

  public Integer getRevision(String emigoId) throws NotFoundException {
    
    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(entity == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    return entity.getRevision();
  }

  public Amigo getIdentity(String emigoId) throws NotFoundException {
    
    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(entity == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    return entity;
  }

  public InputStream getLogo(String emigoId) throws NotFoundException {
 
    EmigoEntity entity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(entity == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    if(entity.getLogo() == null) {
      throw new NotFoundException(404, "logo not set");
    }
    try {
      byte[] bytes = Base64.getDecoder().decode(entity.getLogo());
      return new ByteArrayInputStream(bytes);
    }
    catch(Exception e) {
      throw new NotFoundException(404, "invalid logo image");
    }
  }

  public AmigoEntry addEmigo(Account act, AmigoMessage msg) 
        throws IllegalArgumentException, Exception {
    AccountEmigo emigo = addAccountEmigo(act, msg);
    return setAmigoEntry(emigo);
  }

  @Transactional
  public AccountEmigo addAccountEmigo(Account act, AmigoMessage msg) 
        throws IllegalArgumentException, Exception {

    // validate emigo
    Amigo emigo = EmigoUtil.getObject(msg);
    String emigoId = emigo.getAmigoId();

    // increment revision
    Integer revision = act.getIndexRevision() + 1;
    act.setIndexRevision(revision);
    accountRepository.save(act);
    
    // retrieve emigo and insert if new
    EmigoEntity emigoEntity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(emigoEntity == null) {
      emigoEntity = new EmigoEntity(emigo);
      emigoEntity = emigoEntityRepository.save(emigoEntity);
    }
    else if(emigo.getRevision() > emigoEntity.getRevision()) {
      emigoEntity.setEmigo(emigo);
      emigoEntity = emigoEntityRepository.save(emigoEntity);
    }

    // retrieve account emigo and insert if new
    AccountEmigo accountEmigo = accountEmigoRepository.findOneByAccountAndEmigoId(act, emigoId);
    if(accountEmigo == null) {
      accountEmigo = new AccountEmigo(act, emigoId);
      accountEmigo.setRevision(revision);
      accountEmigo = accountEmigoRepository.save(accountEmigo);
    }

    // update pending list
    shareService.setPendingAmigo(act, accountEmigo);;

    return accountEmigo;
  }

  public Amigo setEmigo(Account act, AmigoMessage msg) 
        throws NotFoundException, IllegalArgumentException, Exception {

    // validate emigo
    Amigo emigo = EmigoUtil.getObject(msg);
    String emigoId = emigo.getAmigoId();

    // retrieve emigo and insert if new
    EmigoEntity emigoEntity = emigoEntityRepository.findOneByEmigoId(emigoId);
    if(emigoEntity == null) {
      throw new NotFoundException(404, "emigo not found");
    }

    if(emigo.getRevision() > emigoEntity.getRevision()) {
      emigoEntity.setEmigo(emigo);
      emigoEntity = emigoEntityRepository.save(emigoEntity);
    }
    return emigoEntity;
  }

  @Transactional
  public AmigoEntry setEmigoLabels(Account act, String emigoId, List<String> labelIds) throws NotFoundException {

    // get revision incremented module revision
    Integer revision = act.getIndexRevision() + 1;

    // retrieve emigo to associate
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    List<AccountLabel> labels = accountLabelRepository.findByAccountAndLabelIdIn(act, labelIds);

    emigo.setAccountLabels(labels);
    emigo.setRevision(revision);
    emigo = accountEmigoRepository.save(emigo); 

    // increment index revision
    act.setIndexRevision(revision);
    act.setContactRevision(act.getContactRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // return base class
    return setAmigoEntry(emigo);
  }

  @Transactional
  public AmigoEntry addEmigoLabel(Account act, String emigoId, String labelId) throws NotFoundException {

    // get revision incremented module revision
    Integer revision = act.getIndexRevision() + 1;

    // retrieve emigo to associate
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    AccountLabel label = getAccountLabel(act, labelId);
    emigo.getAccountLabels().add(label);
    emigo.setRevision(revision);
    emigo = accountEmigoRepository.save(emigo); 

    // increment index revision
    act.setIndexRevision(revision);
    act.setContactRevision(act.getContactRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // return base class
    return setAmigoEntry(emigo);
  }

  @Transactional
  public void addEmigoReject(Account act, String emigoId) throws NotFoundException {
    
    // check if already rejected
    AccountRejectEmigo emigo = rejectEmigoRepository.findOneByAccountAndEmigoId(act, emigoId);
    if(emigo == null) {
      AccountRejectEmigo reject = new AccountRejectEmigo(act, emigoId);
      rejectEmigoRepository.save(reject);
    }
    
    // increment index revision
    act.setIndexRevision(act.getIndexRevision() + 1);
    accountRepository.save(act);
  }

  public AmigoEntry getEmigo(Account act, String emigoId) throws NotFoundException {
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    return setAmigoEntry(emigo);
  }

  public List<String> getEmigoRejects(Account act) {
    
    List<AccountRejectEmigo> emigos = rejectEmigoRepository.findByAccount(act);
    List<String> ids = new ArrayList<String>();
    for(AccountRejectEmigo emigo: emigos) {
      ids.add(emigo.getEmigoId());
    }
    return ids;
  }

  public List<PendingAmigoView> getEmigoRequests(Account act) {

    List<AccountSharePending> emigos = sharePendingRepository.findByAccount(act);
    List<PendingAmigoView> ids = new ArrayList<PendingAmigoView>();
    for(AccountSharePending emigo: emigos) {
      PendingAmigoView view = new PendingAmigoView();
      view.setShareId(emigo.getShareId());
      view.setRevision(emigo.getRevision());
      ids.add(view);
    }
    return ids;    
  }

  public PendingAmigo getPendingAmigo(Account act, String shareId) throws NotFoundException {

    AccountSharePending emigo = sharePendingRepository.findOneByAccountAndShareId(act, shareId);
    if(emigo == null) {
      throw new NotFoundException(404, "pending emigo not found");
    }
    return emigo;
  }

  public List<AmigoEntry> getEmigos(Account act) {

    List<AccountEmigo> emigos = accountEmigoRepository.findByAccount(act);
    return setEmigoEntries(emigos);
  }

  @Transactional
  public void deleteEmigo(Account act, String emigoId) throws NotFoundException {

    AccountEmigo emigo = accountEmigoRepository.findOneByAccountAndEmigoId(act, emigoId);
    if(emigo != null) {

      // remove labels
      //accountEmigoLabelIdRepository.deleteByEmigoId(emigo.getId());

      // remove from pending list
      sharePendingRepository.deleteByEmigo(emigo);

      // remove from share
      shareRepository.deleteByEmigo(emigo);
     
      // delete emigo
      accountEmigoRepository.deleteByAccountAndEmigoId(act, emigoId);

      act.setIndexRevision(act.getIndexRevision() + 1);
      accountRepository.save(act);
    }
    else {
      throw new NotFoundException(404, "emigo not found");
    }
  }

  @Transactional
  public AmigoEntry deleteEmigoLabel(Account act, String emigoId, String labelId) throws NotFoundException {
    
    // retrieve emigo to associate
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    AccountLabel label = getAccountLabel(act, labelId);

    // increment module revision
    Integer revision = act.getIndexRevision() + 1;

    // remove any occurence of label
    Iterator<AccountLabel> labels = emigo.getAccountLabels().iterator();
    while(labels.hasNext()) {
      AccountLabel l = labels.next();
      if(l.getLabelId().equals(labelId)) {
        labels.remove();
      }
    }
    emigo.setRevision(revision);
    emigo = accountEmigoRepository.save(emigo);

    // increment index revision
    act.setIndexRevision(revision);
    act.setContactRevision(act.getContactRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    return setAmigoEntry(emigo);
  }

  @Transactional
  public AmigoEntry clearEmigoNotes(Account act, String emigoId) throws NotFoundException {

    // increment index revision
    Integer revision = act.getIndexRevision() + 1;
    act.setIndexRevision(revision);
    accountRepository.save(act);
    
    // retrieve emigo entry
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    emigo.setNotes(null);
    emigo.setRevision(revision);
    emigo = accountEmigoRepository.save(emigo);
    return setAmigoEntry(emigo);
  }

  @Transactional
  public void deleteEmigoReject(Account act, String emigoId) throws NotFoundException {

    AccountRejectEmigo emigo = rejectEmigoRepository.findOneByAccountAndEmigoId(act, emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "reject emigo not found");
    }
    rejectEmigoRepository.delete(emigo);

    // increment index revision
    act.setIndexRevision(act.getIndexRevision() + 1);
    accountRepository.save(act);
  }

  @Transactional
  public ShareMessage deleteEmigoRequest(Account act, String shareId, Boolean reject) 
        throws NotFoundException, Exception {

    AccountSharePending share = sharePendingRepository.findOneByAccountAndShareId(act, shareId);
    if(share == null) {
      throw new NotFoundException(404, "share request not found");
    }

    // get share message
    ShareMessage message = shareService.getDenyMessage(act, share.getEmigoId());

    // add to reject list
    if(reject != null && reject) {
      AccountRejectEmigo emigo = new AccountRejectEmigo(act, share.getEmigoId());
      rejectEmigoRepository.save(emigo);
    }

    // increment index revision
    act.setIndexRevision(act.getIndexRevision() + 1);
    accountRepository.save(act);
 
    sharePendingRepository.delete(share);
    return message;
  }

  @Transactional
  public AmigoEntry setEmigoNotes(Account act, String emigoId, String notes) throws NotFoundException {

    // increment index revision
    Integer revision = act.getIndexRevision() + 1;
    act.setIndexRevision(revision);
    accountRepository.save(act);
    
    // retrieve emigo entry
    AccountEmigo emigo = getAccountEmigo(act, emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "emigo not found");
    }
    emigo.setNotes(notes);
    emigo.setRevision(revision);
    emigo = accountEmigoRepository.save(emigo);
    return setAmigoEntry(emigo);
  }

  public List<AmigoView> getEmigoViews(Account act) throws NotFoundException {
    
    // retrieve emigos
    List<AmigoView> views = new ArrayList<AmigoView>();
    List<AccountEmigo> emigos = accountEmigoRepository.findByAccount(act);
    for(AccountEmigo emigo: emigos) {
      AmigoView view = new AmigoView();
      view.setAmigoId(emigo.getEmigoId());
      view.setRevision(emigo.getRevision());
      views.add(view);
    }
    return views;
  }
  
}

