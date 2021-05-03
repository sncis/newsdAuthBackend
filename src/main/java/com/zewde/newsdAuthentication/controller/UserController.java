package com.zewde.newsdAuthentication.controller;


import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.EmailService;
import com.zewde.newsdAuthentication.service.RegistrationConfirmationTokenService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.CookiesUtils;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(value="/auth",consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.TEXT_PLAIN_VALUE})

public class UserController {

  private final static Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserDetailsServiceImplementation userService;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JWTTokenUtils jwtTokenUtils;

  @Autowired
  private CookiesUtils cookiesUtils;

  @Autowired
  private RegistrationConfirmationTokenService registrationConfirmationTokenService;


  @Autowired
  public SimpleMailMessage mailMessage;


  @Autowired
  private EmailService emailService;

  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody User user) throws UserNameAlreadyExistException, EmailAlreadyExistException, IOException, MessagingException {
    RegistrationConfirmationToken token;
    String textMail;

    try{
      token = userService.registerUserAndReturnToken(user);
      textMail = String.format(Objects.requireNonNull(mailMessage.getText()), token.getToken());

    }catch(EmailAlreadyExistException e){
      System.out.println(e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists", e);
    } catch(UserNameAlreadyExistException e){
      logger.info("UsernameAlreadyExist Exception was thrown");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists", e);
    }catch(Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sorry something went wrong from Backend",e);
    }

    emailService.sendEmail(user.getEmail(),"Confirm newsdMe registration", textMail);
    return new ResponseEntity<>("",HttpStatus.CREATED);
  }

  @GetMapping("/login")
  public ResponseEntity<?>getLogin(){
    return new ResponseEntity<>(HttpStatus.OK);
  }


  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response){

    Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
    String token = jwtTokenUtils.generateToken(auth.getName());
    Cookie cookie = cookiesUtils.createCookie("jwtToken", token);
    response.addCookie(cookie);

    return new ResponseEntity<>(HttpStatus.OK);
  }


  @GetMapping("/confirm")
  public ResponseEntity<?> confirmUser(@Valid @RequestParam("token") String token){
    userService.confirmUser(token);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/resendConfirmationToken")
  public ResponseEntity<?> resendConfirmationToken(@Valid @RequestBody String email){
    RegistrationConfirmationToken token = userService.findTokenByUserEmail(email);

    String textMail = String.format(Objects.requireNonNull(mailMessage.getText()), token.getToken());
    emailService.sendEmail(email,"Confirm newsdMe registration", textMail);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}