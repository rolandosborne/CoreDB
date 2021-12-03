package org.coredb.service.util;

import org.coredb.model.Amigo;
import org.coredb.model.Share;
import org.coredb.model.AmigoMessage;

public class ShareData {
  private Amigo emigo;
  private Share share;
  private AmigoMessage emigoMessage;

  public ShareData(Share s, Amigo e, AmigoMessage em) {
    this.emigo = e;
    this.emigoMessage = em;
    this.share = s;
  }

  public Amigo getEmigo() {
    return this.emigo;
  }
  public void setEmigo(Amigo value) {
    this.emigo = value;
  }

  public Share getShare() {
    return this.share;
  }
  public void setShare(Share value) {
    this.share = value;
  }

  public AmigoMessage getAmigoMessage() {
    return this.emigoMessage;
  }
  public void setAmigoMessage(AmigoMessage value) {
    this.emigoMessage = value;
  }
}


