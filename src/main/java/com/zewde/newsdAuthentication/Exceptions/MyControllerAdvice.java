package com.zewde.newsdAuthentication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> UsernameNotFound(UsernameNotFoundException usernameNotFoundException){
    return new ResponseEntity<>("Their is no such user are you sure you have an account?", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Object> BadCredentials(BadCredentialsException ex){
    return new ResponseEntity<>("Wrong username or password", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserLoginBlockedException.class)
  public ResponseEntity<String> userLoginBlockedException(UserLoginBlockedException userLoginBlockedException){
    return new ResponseEntity<>("Your account is blocked due to too many failed login attempts. You can try it again in 5 minutes", HttpStatus.TOO_MANY_REQUESTS);

  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<String> accountDisabled(DisabledException accountDisabled){
    return new ResponseEntity<>("Your account is disabled.", HttpStatus.UNAUTHORIZED);
  }


  @ExceptionHandler(RegistrationConfirmationTokenNotFoundException .class)
  public ResponseEntity<String> registrationConfirmationTokenNot(RegistrationConfirmationTokenNotFoundException  accountDisabled){
    return new ResponseEntity<>("Your account is disabled. Please confirm your Registration.", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> accessdenied(AccessDeniedException ex){
    return new ResponseEntity<>("Sorry you are not allowed to access these resource", HttpStatus.FORBIDDEN);

  }
//


}
