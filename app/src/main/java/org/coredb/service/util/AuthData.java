package org.coredb.service.util;

import org.coredb.model.Amigo;
import org.coredb.model.Auth;

public class AuthData {
  private Amigo emigo;
  private Auth auth;

  public AuthData(Amigo e, Auth a) {
    this.emigo = e;
    this.auth = a;
  }

  public Amigo getEmigo() {
    return this.emigo;
  }
  public void setEmigo(Amigo value) {
    this.emigo = value;
  }

  public Auth getAuth() {
    return this.auth;
  }
  public void setAuth(Auth value) {
    this.auth = value;
  }
}


