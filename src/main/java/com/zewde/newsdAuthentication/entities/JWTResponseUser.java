package com.zewde.newsdAuthentication.entities;

import java.io.Serializable;

public class JWTResponseUser implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  private final String jwtToken;

  public JWTResponseUser(String token){
    this.jwtToken = token;
  }

  public String getJwtToken() {
    return jwtToken;
  }

}
