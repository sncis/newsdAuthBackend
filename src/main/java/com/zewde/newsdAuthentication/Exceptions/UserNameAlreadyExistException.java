package com.zewde.newsdAuthentication.Exceptions;

public class UserNameAlreadyExistException extends RuntimeException {
  public UserNameAlreadyExistException(String msg){
    super(msg);
  }
}
