package com.zewde.newsdAuthentication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Username already exists")
public class UserNameAlreadyExistException extends RuntimeException {

}
