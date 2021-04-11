package com.zewde.newsdAuthentication.controller;


import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.RegistrationConfirmationTokenNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.RegistrationConfirmationTokenService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.CookiesUtils;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//@RequestMapping(value="/", consumes="text/plain; charset: utf-8;application/json")

@RestController
@RequestMapping(value="/")
public class UserController {


  @Autowired
  private UserDetailsServiceImplementation userService;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JWTTokenUtils jwtTokenUtils;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private CookiesUtils cookiesUtils;

  @Autowired
  private RegistrationConfirmationTokenService registrationConfirmationTokenService;


  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody User user) throws UserNameAlreadyExistException, EmailAlreadyExistException {
    User u;
    System.out.println("user form fronted");
    System.out.println(user);
    try{
      u = userService.registerUser(user);

//      userService.registerUser(user);
    }catch(EmailAlreadyExistException e){
      System.out.println(e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists", e);
    } catch(UserNameAlreadyExistException e){
      System.out.println(e);

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists", e);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) throws BadCredentialsException, DisabledException{
    System.out.println("user login");
    System.out.println(user);
    try{
      authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
    }catch(BadCredentialsException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong username or password", e);
    }catch(DisabledException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is disabled", e);
    }

    MyUserDetails u = userService.loginUser(user);
    String token = jwtTokenUtils.generateToken(u.getUsername());

    Cookie cookie = cookiesUtils.createCookie("jwtToken", token);
    System.out.println(cookie);
    response.addCookie(cookie);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    System.out.println("logout Called");
    Cookie cookie = cookiesUtils.createCookie("jwtToken", "",0);
    response.addCookie(cookie);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/confirmeuser")
  public ResponseEntity<?> confirmUser(@RequestParam("confirmationToken") String token){
    try{
      registrationConfirmationTokenService.findToken(token);

    }catch(RegistrationConfirmationTokenNotFoundException ex){
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}