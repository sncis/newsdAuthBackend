package com.zewde.newsdAuthentication.entities;

import java.io.Serializable;

public class JWTRequestUser implements Serializable {
  private static final long serialVersionUID = 7519288840064088841L;
  private String userName;
  private String password;

  public JWTRequestUser(){};

  public JWTRequestUser(String userName, String password){
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
