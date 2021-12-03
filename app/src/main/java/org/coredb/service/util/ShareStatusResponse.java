package org.coredb.service.util;

import org.coredb.model.ShareStatus;
import org.coredb.model.SharePrompt;

public class ShareStatusResponse extends ShareStatus {

  public ShareStatusResponse(ShareStatusEnum status) {
    super.setShareStatus(status);
  }

  public ShareStatusResponse(ShareStatusEnum status, String token) {
    super.setShareStatus(status);
    super.setConnected(token);
  }

  public ShareStatusResponse(ShareStatusEnum status, String token, String image, String question) {
    SharePrompt pending = new SharePrompt();
    pending.setToken(token);
    pending.setText(question);
    pending.setImage(image);
    super.setShareStatus(status);
    super.setPending(pending);
  }

}
    
