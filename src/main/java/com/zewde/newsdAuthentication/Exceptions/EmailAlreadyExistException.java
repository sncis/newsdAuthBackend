package com.zewde.newsdAuthentication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email already exists")
public class EmailAlreadyExistException extends RuntimeException {

}
