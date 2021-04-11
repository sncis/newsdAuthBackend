package com.zewde.newsdAuthentication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "RegistrationConfirmationToken doesn't exists")
public class RegistrationConfirmationTokenNotFoundException extends RuntimeException {
}
