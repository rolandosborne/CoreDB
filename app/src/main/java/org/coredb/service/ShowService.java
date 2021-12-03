package org.coredb.service;

import java.security.*;
import java.security.spec.*;

import java.time.Instant;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.coredb.NameRegistry.*;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;

import org.coredb.jpa.entity.EmigoEntity;
import org.coredb.jpa.entity.Tagged;
import org.coredb.jpa.entity.Account;
import org.coredb.jpa.entity.AccountLabel;
import org.coredb.jpa.entity.AccountSubject;
import org.coredb.jpa.entity.AccountSubjectAsset;
import org.coredb.jpa.entity.AccountSubjectLabelId;
import org.coredb.jpa.entity.OriginalSubjectAsset;

import org.coredb.jpa.repository.EmigoEntityRepository;
import org.coredb.jpa.repository.AccountRepository;
import org.coredb.jpa.repository.AccountSubjectRepository;
import org.coredb.jpa.repository.AccountLabelRepository;
import org.coredb.jpa.repository.AccountSubjectLabelIdRepository;
import org.coredb.jpa.repository.AccountSubjectAssetRepository;
import org.coredb.jpa.repository.OriginalSubjectAssetRepository;
import org.coredb.jpa.repository.TaggedRepository;

import org.coredb.service.specification.AccountSubjectSpecification;

import org.coredb.model.Asset;
import org.coredb.model.LabelEntry;
import org.coredb.model.Subject;
import org.coredb.model.Tag;
import org.coredb.model.SubjectTag;
import org.coredb.model.SubjectEntry;
import org.coredb.model.SubjectView;
import org.coredb.model.SubjectAsset;
import org.coredb.model.SubjectAsset.StateEnum;
import org.coredb.model.OriginalAsset;
import org.coredb.model.AssetData;
import org.coredb.model.StoreStatus;

import org.coredb.service.util.SecurityUtil;

@Service
public class ShowService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountLabelRepository labelRepository;

  @Autowired
  private AccountSubjectRepository subjectRepository;

  @Autowired
  private AccountSubjectLabelIdRepository subjectLabelRepository;

  @Autowired
  private AccountSubjectAssetRepository subjectAssetRepository;

  @Autowired
  private OriginalSubjectAssetRepository originalAssetRepository;  

  @Autowired
  private TaggedRepository tagRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private EmigoEntityRepository emigoRepository;

  @Autowired
  private AssetStoreService assetStoreService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private SubjectEntry setSubjectEntry(AccountSubject subject) {
    for(AccountLabel label: subject.getAccountLabels()) {
      subject.addLabelsItem(label.getLabelId());
    }
    return subject;
  }

  private List<SubjectEntry> setSubjectEntries(List<AccountSubject> subjects) {
    
    List<SubjectEntry> entries = new ArrayList<SubjectEntry>();
    for(AccountSubject subject: subjects) {
      entries.add(setSubjectEntry(subject));
    }
    return entries;
  }

  private List<SubjectView> setSubjectView(List<AccountSubject> subjects) {
    
    List<SubjectView> views = new ArrayList<SubjectView>();
    for(AccountSubject subject: subjects) {
      SubjectView view = new SubjectView();
      view.setSubjectId(subject.getSubjectId());
      view.setRevision(subject.getRevision());
      view.setTagRevision(subject.getTagRevision());
      views.add(view);
    }
    return views;
  }

  private AccountSubject getAccountSubject(Account account, String subjectId) throws NotFoundException {

    // retrieve subjects
    AccountSubject subject = subjectRepository.findOneByAccountAndSubjectId(account, subjectId);
    if(subject == null) {
      throw new NotFoundException(404, "subject entry not found");
    }

    // determine status
    Boolean ready = true;
    for(AccountSubjectAsset asset: subject.getSubjectAssets()) {
      if(!asset.getStatus().equals(SubjectAsset.StateEnum.READY.toString())) {
        ready = false; 
      }
    }
    subject.setReady(ready);
    return subject;
  }
  
  private AccountLabel getAccountLabel(Account account, String labelId) throws NotFoundException {
  
    // retrieve labels
    AccountLabel label = labelRepository.findOneByAccountAndLabelId(account, labelId);
    if(label == null) {
      throw new NotFoundException(404, "label entry not found");
    }
    return label;
  }

  public StoreStatus getStatus(Account account) throws NotFoundException {

    // retrieve limits
    Long limit = configService.getAccountNumValue(account.getEmigoId(), AC_STORE_SIZE, (long)134217728);
    Long free = assetStoreService.getFreeSpace();

    // compute used space
    Long used = (long)0;
    List<AccountSubject> subjects = subjectRepository.findByAccount(account);
    for(AccountSubject subject: subjects) {
      for(OriginalSubjectAsset asset: subject.getOriginalAssets()) {
        Long size = asset.getSize();
        if(size != null) {
          used += size;
        }
      }
      for(AccountSubjectAsset asset: subject.getSubjectAssets()) {
        Long size = asset.getAssetSize();
        if(size != null) {
          used += size;
        }
      }
    }

    // compute available space
    Long available = limit - used;
    if(available > free) {
      available = free;
    }
    if(available < (long)0) {
      available = (long)0;
    }

    StoreStatus status = new StoreStatus();
    status.setAvailable(available);
    status.setUsed(used);
    return status;
  }

  @Transactional
  public SubjectEntry addSubject(Account act, String schema) {

    // limit max number of subjects
    String eid = act.getEmigoId();
    Long count = subjectRepository.countByAccount(act);
    if(count >= configService.getAccountNumValue(eid, AC_SUBJECT_COUNT_MAX, (long)1048576)) {
      throw new IllegalStateException("subject limit reached");
    }

    // increment revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // return new entry
    AccountSubject subject = new AccountSubject(act);
    subject.setShare(false);
    subject.setReady(false);
    subject.setRevision(revision);
    subject.setSchemaId(schema);
    subject = subjectRepository.save(subject);
    return setSubjectEntry(subject);
  }

  public SubjectEntry getSubject(Account act, String subjectId) throws NotFoundException {

    // retrieve casted subject
    AccountSubject subject = getAccountSubject(act, subjectId);
    return setSubjectEntry(subject);
  }

  public List<SubjectEntry> getSubjects(Account act) {

    List<AccountSubject> subjects = subjectRepository.findByAccount(act);
    return setSubjectEntries(subjects);
  }

  @Transactional
  public void delSubject(Account act, String subjectId) throws NotFoundException {
  
    // retrieve subject
    AccountSubject subject = getAccountSubject(act, subjectId);

    // delete aassociated entities
    subjectAssetRepository.deleteBySubject(subject);
    originalAssetRepository.deleteBySubject(subject);
    //subjectLabelRepository.deleteBySubjectId(subject.getId());

    // delete specified subject
    subjectRepository.delete(subject);

    // increment revision
    act.setShowRevision(act.getShowRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);
  
    // deleting subject folder will be handled by garbage collection script  
  }

  public Asset addSubjectAsset(Account act, String subjectId, List<String> transforms, MultipartFile data)
        throws NotFoundException, Exception {

    // add asset in transactional scope
    Asset asset = setSubjectAsset(act, subjectId, transforms, data);

    // invoke transcoder
    String[] cmd = { "bash", "/opt/diatum/transcode.sh", assetStoreService.getPath() };
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(cmd);
    Process process = processBuilder.start();

    return asset;
  }

  @Transactional
  private Asset setSubjectAsset(Account act, String subjectId, List<String> transforms, MultipartFile data)
        throws NotFoundException, Exception {
    
    // load reference
    String eid = act.getEmigoId();
    AccountSubject subject = getAccountSubject(act, subjectId);
    
    // limit max number of assets
    Long count = subjectAssetRepository.countBySubject(subject);
    if(count >= configService.getAccountNumValue(eid, AC_SUBJECT_ASSET_MAX, (long)4096)) {
      throw new IllegalStateException("subject asset limit reached");
    }
 
    // store asset
    String assetId = "_" + UUID.randomUUID().toString().replace("-", "");
    File file = assetStoreService.set(subject.getAccount().getEmigoId(), subject.getSubjectId(), assetId, data);
    Long cur = Instant.now().getEpochSecond();
    OriginalSubjectAsset original = new OriginalSubjectAsset(subject);
    original.setAssetId(assetId);
    original.setOriginalName(data.getOriginalFilename());
    original.setSize(file.length());
    original.setCreated(cur);
    original.setStatus("uploaded");
    original.setHash(SecurityUtil.hash(file));
    original = originalAssetRepository.save(original);

    // create new asset for each transform
    List<AccountSubjectAsset> assets = new ArrayList<AccountSubjectAsset>();
    for(String transform: transforms) {
      AccountSubjectAsset asset = new AccountSubjectAsset(subject);
      asset.setPending(subject);
      asset.setOriginalId(assetId);
      asset.setTransform(transform);
      asset.setStatus(SubjectAsset.StateEnum.PENDING.toString());
      assets.add(asset);
    }
    assets = subjectAssetRepository.save(assets);

    // increment revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // increment subject
    subject.setModified(cur);
    subject.setRevision(revision);
    subjectRepository.save(subject);

    // create response
    @SuppressWarnings("unchecked")
    List<SubjectAsset> subjectAssets = (List<SubjectAsset>)(List<?>)assets;
    Asset asset = new Asset();
    asset.setAssets(subjectAssets);
    asset.setOriginal(original);
    return asset;
  }

  public SubjectEntry delSubjectAsset(Account act, String subjectId, String assetId) throws NotFoundException, IllegalStateException {
    
    // remove asset
    clearSubjectAsset(act, subjectId, assetId);
    return getAccountSubject(act, subjectId);
  }

  @Transactional  
  private void clearSubjectAsset(Account act, String subjectId, String assetId) throws NotFoundException, IllegalStateException {

    // load reference
    AccountSubject subject = getAccountSubject(act, subjectId);
 
    // check if asset is with subject
    Set<AccountSubjectAsset> assets = subject.getSubjectAssets();
    for(AccountSubjectAsset asset: assets) {
      if(asset.getAssetId().equals(assetId)) {

        // increment revision
        Integer revision = act.getShowRevision() + 1;
        act.setShowRevision(revision);
        act.setViewRevision(act.getViewRevision() + 1);
        accountRepository.save(act);

        // increment subject revision
        Long cur = Instant.now().getEpochSecond();
        subject.setModified(cur);
        subject.setRevision(revision);
        subjectRepository.save(subject);

        // delete asset from database and filesystem
        asset.setPending(null);
        asset.setStatus(SubjectAsset.StateEnum.DELETED.toString());
        subjectAssetRepository.save(asset);
        return;
      }
    }
   
    // check if asset is original
    Set<OriginalSubjectAsset> originals = subject.getOriginalAssets();
    for(OriginalSubjectAsset asset: originals) {
      if(asset.getAssetId().equals(assetId)) {

        // increment revision
        Integer revision = act.getShowRevision() + 1;
        act.setShowRevision(revision);
        act.setViewRevision(act.getViewRevision() + 1);
        accountRepository.save(act);

        // increment subject revision
        Long cur = Instant.now().getEpochSecond();
        subject.setModified(cur);
        subject.setRevision(revision);
        subjectRepository.save(subject);

        // delete asset from database and filesystem
        asset.setStatus(SubjectAsset.StateEnum.DELETED.toString());
        originalAssetRepository.save(asset);
        return;
      }
    }
 
    // if asset not found
    throw new NotFoundException(404, "asset not found");
  }

  private AccountSubject getSubjectLabel(Account act, List<AccountLabel> labels, String subjectId) throws NotFoundException {

    // retrieve account subject
    Long cur = Instant.now().getEpochSecond();
    AccountSubject subject = subjectRepository.findOneByAccountAndShareAndAccountLabelsInAndSubjectIdAndExpiresGreaterThanAndPendingIsNull(act, true, labels, subjectId, cur);
    if(subject == null) {
      throw new NotFoundException(404, "subject not available");
    }
    return subject;
  }

  private SubjectTag getTags(AccountSubject subject, String schema, Boolean descending) {

    List<Tagged> tagged;
    if(descending == null || descending) {
      tagged = tagRepository.findBySubjectAndSchemaIdOrderByCreatedDesc(subject, schema);
    }
    else {
      tagged = tagRepository.findBySubjectAndSchemaIdOrderByCreatedAsc(subject, schema);
    }
    
    // can't the entity do this?
    for(Tagged tag: tagged) {
      EmigoEntity emigo = tag.getEmigo();
      tag.setAmigoId(emigo.getEmigoId());
      tag.setAmigoName(emigo.getName()); // possible privacy issue
      tag.setAmigoRegistry(emigo.getRegistry());
    }

    @SuppressWarnings("unchecked")
    List<Tag> tags = (List<Tag>)(List<?>)tagged;
    
    SubjectTag tag = new SubjectTag();
    tag.setRevision(subject.getTagRevision());
    tag.setTags(tags);
    return tag;
  }

  public SubjectTag getSubjectLabelTag(Account act, List<AccountLabel> labels, String subjectId, String schema, Boolean descending) 
        throws NotFoundException {

    AccountSubject subject = getSubjectLabel(act, labels, subjectId);
    return getTags(subject, schema, descending);
  }

  public SubjectTag getAccountSubjectTag(Account act, String subjectId, String schema, Boolean descending)
        throws NotFoundException {

    AccountSubject subject = getAccountSubject(act, subjectId);
    return getTags(subject, schema, descending);
  }

  private SubjectTag addSubjectTag(Account act, AccountSubject subject, String schema, Boolean descending, String data, String emigoId)
        throws NotFoundException, IllegalStateException {

    Long limit = configService.getAccountNumValue(act.getEmigoId(), AC_SUBJECT_TAG_LIMIT, (long)256); 
    Set<Tagged> tags = subject.getTags();
    if(tags != null && tags.size() >= limit) {
      throw new IllegalStateException("too many tags");
    }
    
    // get parameters for tag
    EmigoEntity emigo = emigoRepository.findOneByEmigoId(emigoId);
    if(emigo == null) {
      throw new NotFoundException(404, "emigo nnot found");
    }   
    Long cur = Instant.now().getEpochSecond();

    // update subject with new tag
    subject.setTagRevision(subject.getTagRevision() + 1);
    subject = subjectRepository.save(subject);
    
    // create new tag 
    Tagged tag = new Tagged(subject);
    tag.setEmigo(emigo);
    tag.setSchemaId(schema);
    tag.setData(data);
    tag.setCreated(cur);
    tagRepository.save(tag); 

    // update module revision 
    act.setShowRevision(act.getShowRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // return resulting tag set
    return getTags(subject, schema, descending);
  }

  @Transactional
  public SubjectTag addSubjectLabelTag(Account act, List<AccountLabel> labels, String subjectId, String schema, Boolean descending, String data, String emigoId)
        throws NotFoundException, IllegalStateException {
  
    AccountSubject subject = getSubjectLabel(act, labels, subjectId);
    return addSubjectTag(act, subject, schema, descending, data, emigoId);
  }

  @Transactional
  public SubjectTag addAccountSubjectTag(Account act, String subjectId, String schema, Boolean descending, String data)
        throws NotFoundException, IllegalStateException {

    AccountSubject subject = getAccountSubject(act, subjectId);
    return addSubjectTag(act, subject, schema, descending, data, act.getEmigoId());
  }

  private SubjectTag removeSubjectTag(Account act, AccountSubject subject, String tagId, String schema, Boolean descending, String emigoId)
        throws NotFoundException, NotAcceptableException {

    // update module revision 
    act.setShowRevision(act.getShowRevision() + 1);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // update subject with new tag set
    subject.setTagRevision(subject.getTagRevision() + 1);
    subject = subjectRepository.save(subject);

    // remove tag
    if(act.getEmigoId().equals(emigoId)) {
      tagRepository.deleteBySubjectAndTagId(subject, tagId);
    }
    else {
 
      // get parameters for tag
      EmigoEntity emigo = emigoRepository.findOneByEmigoId(emigoId);
      if(emigo == null) {
        throw new NotFoundException(404, "emigo nnot found");
      }   
      tagRepository.deleteBySubjectAndEmigoAndTagId(subject, emigo, tagId);
    }

    // return result tag set
    return getTags(subject, schema, descending);
  }

  @Transactional
  public SubjectTag removeSubjectLabelTag(Account act, List<AccountLabel> labels, String subjectId, String tagId, String schema, Boolean descending, String emigoId)
        throws NotFoundException, NotAcceptableException {
  
    AccountSubject subject = getSubjectLabel(act, labels, subjectId);
    return removeSubjectTag(act, subject, tagId, schema, descending, emigoId);
  }

  @Transactional
  public SubjectTag removeAccountSubjectTag(Account act, String subjectId, String tagId, String schema, Boolean descending)
        throws NotFoundException, NotAcceptableException {

    AccountSubject subject = getAccountSubject(act, subjectId);
    return removeSubjectTag(act, subject, tagId, schema, descending, act.getEmigoId());
  }

  public AssetData getSubjectLabelAsset(Account act, List<AccountLabel> labels, String subjectId, String assetId, Long min, Long max) throws NotFoundException, AccessDeniedException {

    AccountSubject subject = getSubjectLabel(act, labels, subjectId);

    // retrieve data
    return getSubjectData(subject, assetId, min, max, false);    
  }

  public AssetData getSubjectLabelAsset(Account act, List<AccountLabel> labels, String subjectId, String assetId) throws NotFoundException, AccessDeniedException {

    AccountSubject subject = getSubjectLabel(act, labels, subjectId);

    // retrieve data
    return getSubjectData(subject, assetId, false);    
  }

  public AssetData getSubjectAsset(Account act, String subjectId, String assetId, Long min, Long max)
        throws NotFoundException, AccessDeniedException {

    // retrieve account subject
    AccountSubject subject = getAccountSubject(act, subjectId);

    // retrieve asset data
    return getSubjectData(subject, assetId, min, max, true);
  }

  private AssetData getSubjectData(AccountSubject subject, String assetId, Long min, Long max, Boolean originals) throws NotFoundException {

    // check if file asset
    for(AccountSubjectAsset asset: subject.getSubjectAssets()) {
      if(asset.getStatus().equals("ready") && asset.getAssetId().equals(assetId)) {

        // validate byte range
        if(min < (long)0) {
          min = (long)0;
        }
        if(max < (long)0) {
          max = asset.getAssetSize() - 1;
        }
        if(min > max || max >= asset.getAssetSize()) {
          throw new NotFoundException(404, "invalid byte range");
        }

        // return asset data
        Long len = (max + 1) - min;
        InputStreamResource stream = assetStoreService.get(subject.getAccount().getEmigoId(), subject.getSubjectId(), assetId, min, len.intValue());
        return new AssetData(stream, asset.getTransform(), min, max, asset.getAssetSize());
      }
    }

    // check if original file asset
    if(originals) {
      for(OriginalSubjectAsset asset: subject.getOriginalAssets()) {
        if(asset.getAssetId().equals(assetId)) {

          // validate byte range
          if(min < (long)0) {
            min = (long)0;
          }
          if(max < (long)0) {
            max = asset.getSize() - 1;
          }
          if(min > max || max >= asset.getSize()) {
            throw new NotFoundException(404, "invalid byte range");
          }

          // return asset data
          Long len = (max + 1) - min;
          InputStreamResource stream = assetStoreService.get(subject.getAccount().getEmigoId(), subject.getSubjectId(), assetId, min, len.intValue());
          return new AssetData(stream, null, min, max, asset.getSize());
        }
      }
    }

    throw new NotFoundException(404, "asset not contained in subject");
  }

  public AssetData getSubjectAsset(Account act, String subjectId, String assetId) throws NotFoundException {
    
    // load reference
    AccountSubject subject = getAccountSubject(act, subjectId);

    // retrieve asset
    return getSubjectData(subject, assetId, true);
  }

  private AssetData getSubjectData(AccountSubject subject, String assetId, Boolean originals) throws NotFoundException {

    // check if file asset
    for(AccountSubjectAsset asset: subject.getSubjectAssets()) {
      if(asset.getStatus().equals("ready") && asset.getAssetId().equals(assetId)) {

        // return asset data
        InputStreamResource stream = assetStoreService.get(subject.getAccount().getEmigoId(), subject.getSubjectId(), assetId);
        return new AssetData(stream, asset.getTransform(), asset.getAssetSize());
      }
    }

    // check if original asset
    for(OriginalSubjectAsset asset: subject.getOriginalAssets()) {
      // return asset data
      InputStreamResource stream = assetStoreService.get(subject.getAccount().getEmigoId(), subject.getSubjectId(), assetId);
      return new AssetData(stream, null, asset.getSize());
    }

    throw new NotFoundException(404, "asset not contained in subject");
  }

  public SubjectEntry addSubjectLabel(Account act, String subjectId, String labelId) throws NotFoundException {
  
    AccountSubject subject = setSubjectLabel(act, subjectId, labelId);
    return setSubjectEntry(subject);
  }    

  @Transactional
  public SubjectEntry setSubjectLabels(Account act, String subjectId, List<String> labelIds) throws NotFoundException {

    // retrieve entities to associate
    AccountSubject subject = getAccountSubject(act, subjectId);
    List<AccountLabel> labels = labelRepository.findByAccountAndLabelIdIn(act, labelIds);

    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // update entity
    subject.setRevision(revision);
    subject.setAccountLabels(labels);
    subject = subjectRepository.save(subject);
 
    // return base class
    return setSubjectEntry(subject);
  }

  @Transactional
  public AccountSubject setSubjectLabel(Account act, String subjectId, String labelId) throws NotFoundException {

    // retrieve entities to associate
    AccountSubject subject = getAccountSubject(act, subjectId);
    AccountLabel label = getAccountLabel(act, labelId);

    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // update entity
    subject.setRevision(revision);
    subject.getAccountLabels().add(label);
    return subjectRepository.save(subject);
  }

  public SubjectEntry deleteSubjectLabel(Account act, String subjectId, String labelId) throws NotFoundException {
    AccountSubject subject = removeSubjectLabel(act, subjectId, labelId);
    return setSubjectEntry(subject);
  }

  @Transactional
  private AccountSubject removeSubjectLabel(Account act, String subjectId, String labelId) throws NotFoundException {
  
    // retrieve entities to associate
    AccountSubject subject = getAccountSubject(act, subjectId);
    AccountLabel label = getAccountLabel(act, labelId);

    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    accountRepository.save(act);

    // remove any occurence of label
    Iterator<AccountLabel> labels = subject.getAccountLabels().iterator();
    while(labels.hasNext()) {
      AccountLabel l = labels.next();
      if(l.getLabelId().equals(labelId)) {
        labels.remove();
      }
    }
    subject.setRevision(revision);

    return subjectRepository.save(subject);
  }

  @Transactional
  public SubjectEntry updateExpire(Account act, String subjectId, Long expire) throws NotFoundException, IllegalStateException {

    // retrieve referenced subject
    AccountSubject subject = getAccountSubject(act, subjectId);
 
    // update subject
    subject.setExpires(expire);
    
    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);

    // increment subject revision
    subject.setRevision(revision);
    subjectRepository.save(subject);

    subject = subjectRepository.save(subject);
    return setSubjectEntry(subject);
  }

  @Transactional
  public SubjectEntry updateSubject(Account act, String subjectId, String schema, String data) throws NotFoundException, IllegalStateException {

    // retrieve referenced subject
    AccountSubject subject = getAccountSubject(act, subjectId);
 
    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);

    // update subject
    subject.setSchemaId(schema);
    subject.setValue(data);
    Long cur = Instant.now().getEpochSecond();
    subject.setModified(cur);
    subject.setRevision(revision);
    subjectRepository.save(subject);

    subject = subjectRepository.save(subject);
    return setSubjectEntry(subject);
  }

  @Transactional
  public SubjectEntry updateSubjectAccess(Account act, String subjectId, Boolean viewable) throws NotFoundException {

    // retrieve referenced subject
    AccountSubject subject = getAccountSubject(act, subjectId);

    // increment profile revision
    Integer revision = act.getShowRevision() + 1;
    act.setShowRevision(revision);
    act.setViewRevision(act.getViewRevision() + 1);
    
    // update subject
    if(viewable != null) {
      subject.setShare(viewable);
    }
    subject.setRevision(revision);

    subject = subjectRepository.save(subject);
    return setSubjectEntry(subject);
  }

  public List<String> getSubjectIds(Account act, Boolean pending) {
    
    // retrieve subjects
    List<AccountSubject> subjects;
    if(pending == null) {
      subjects = subjectRepository.findByAccountOrderByModifiedDesc(act);
    }
    else if(pending == true) {
      subjects = subjectRepository.findByAccountAndPendingIsNotNullOrderByModifiedDesc(act);
    }
    else {
      subjects = subjectRepository.findByAccountAndPendingIsNullOrderByModifiedDesc(act);
    }

    // return just the ids
    List<String> ids = new ArrayList<String>();
    for(AccountSubject subject: subjects) {
      ids.add(subject.getSubjectId());
    }
    return ids;
  }      

  public List<SubjectEntry> getSubjectSet(Account act, List<String> schemas, Boolean editing, Boolean pending) {

    List<AccountSubject> view = null;
    if(editing == null && pending == null) {
      view = subjectRepository.findByAccountAndSchemaIdInOrderByModifiedDesc(act, schemas);
    }
    else if(editing == null && pending == false) {
      view = subjectRepository.findByAccountAndSchemaIdInAndPendingIsNullOrderByModifiedDesc(act, schemas);
    }
    else if(editing == null && pending == true) {
      view = subjectRepository.findByAccountAndSchemaIdInAndPendingIsNotNullOrderByModifiedDesc(act, schemas);
    }
    else if(pending == null) {
      view = subjectRepository.findByAccountAndShareAndSchemaIdInOrderByModifiedDesc(act, editing, schemas);
    }
    else if(pending == false) {
      view = subjectRepository.findByAccountAndShareAndSchemaIdInAndPendingIsNullOrderByModifiedDesc(act, editing, schemas);
    }
    else if(pending == true) {
      view = subjectRepository.findByAccountAndShareAndSchemaIdInAndPendingIsNotNullOrderByModifiedDesc(act, editing, schemas);
    }
    else {
      view = new ArrayList<AccountSubject>();
    }

    return setSubjectEntries(view);  
  }

  public List<SubjectView> getSubjectViewLabelSet(Account act, List<String> schemas, List<AccountLabel> labels) {
   
    Long cur = Instant.now().getEpochSecond();
    List<AccountSubject> subjects = subjectRepository.findDistinctByAccountAndShareAndSchemaIdInAndAccountLabelsInAndExpiresGreaterThanAndPendingIsNullOrderByModifiedDesc(act, true, schemas, labels, cur);
    return setSubjectView(subjects);
  }

  public List<Subject> getSubjectLabelSet(Account act, List<String> schemas, List<AccountLabel> labels) {
   
    Long cur = Instant.now().getEpochSecond();
    List<AccountSubject> accountSubjects = subjectRepository.findDistinctByAccountAndShareAndSchemaIdInAndAccountLabelsInAndExpiresGreaterThanAndPendingIsNullOrderByModifiedDesc(act, true, schemas, labels, cur);

    // construct list of data
    List<Subject> subjects = new ArrayList<Subject>();
    for(AccountSubject subject: accountSubjects) {
      subjects.add(subject.getSubject());
    }
    return subjects;
  }

  public Subject getSubjectLabelEntry(Account act, List<AccountLabel> labels, String subjectId) throws NotFoundException {

    Long cur = Instant.now().getEpochSecond();
    AccountSubject subject = subjectRepository.findOneByAccountAndShareAndAccountLabelsInAndSubjectIdAndExpiresGreaterThanAndPendingIsNullOrderByModifiedDesc(act, true, labels, subjectId, cur);
    if(subject == null) {
      throw new NotFoundException(404, "subject not available");
    }
    return subject.getSubject();
  }

  public List<SubjectView> getSubjectViews(Account act, List<String> schemas) {

    // retrieve subjects
    List<AccountSubject> subjects = subjectRepository.findByAccountAndSchemaIdIn(act, schemas);
    return setSubjectView(subjects); 
  }

}


