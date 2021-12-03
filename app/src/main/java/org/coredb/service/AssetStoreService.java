package org.coredb.service;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.time.Instant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.api.NotFoundException;
import javax.ws.rs.NotAcceptableException;
import org.springframework.dao.DataIntegrityViolationException;
import java.nio.file.AccessDeniedException;
import java.lang.IllegalStateException;

import org.coredb.service.util.SecurityUtil;

@Service
public class AssetStoreService {

  @Value("${assetstore.path}")
  private String path;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public String getPath() {
    return this.path;
  }

  public Long getFreeSpace() {
  
    File file = new File(path);
    return file.getFreeSpace();
  }

  public File set(String emigoId, String subjectId, String assetId, MultipartFile data) throws Exception {

    // extract account and subject id
    String directory = path + "/" + emigoId + "/" + subjectId;
    try {
      new File(directory).mkdirs();
    }
    catch(Exception e) {
      log.error("failed to create directory: " + directory);
    }    

    // create new file object
    File file = new File(directory + "/" + assetId);
    if(file.exists()) {
      throw new IllegalArgumentException("duplicate upload file");
    }
    data.transferTo(file);
    return file;
  }

  public InputStreamResource get(String emigoId, String subjectId, String assetId, Long offset, Integer length) throws NotFoundException {

    try {
      // read range into file
      byte[] bytes = new byte[length];
      FileInputStream file = new FileInputStream(path + "/" + emigoId + "/" + subjectId + "/" + assetId);
      file.skip(offset);
      file.read(bytes);
      InputStream stream = new ByteArrayInputStream(bytes);
      return new InputStreamResource(stream);
    }
    catch(SecurityException e) {
      throw new NotFoundException(404, "asset not available");
    } 
    catch(FileNotFoundException e) {
      System.out.println(path + "/" + emigoId + "/" + subjectId + "/" + assetId);
      throw new NotFoundException(404, "asset not found");
    }
    catch(IOException e) {
      throw new NotFoundException(404, "data not available");
    }
  }

  public InputStreamResource get(String emigoId, String subjectId, String assetId) throws NotFoundException {

    try {
      // load file object
      File file = new File(path + "/" + emigoId + "/" + subjectId + "/" + assetId);
      return new InputStreamResource(new FileInputStream(file));
    }
    catch(SecurityException e) {
      throw new NotFoundException(404, "asset not available");
    } 
    catch(FileNotFoundException e) {
      System.out.println(path + "/" + emigoId + "/" + subjectId + "/" + assetId);
      throw new NotFoundException(404, "asset not found");
    }
    catch(IOException e) {
      throw new NotFoundException(404, "data not available");
    }
  }

}
