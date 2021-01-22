package com.zewde.newsdAuthentication.controller;


import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.JWTResponseUser;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
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

import java.util.ArrayList;


@CrossOrigin(origins = { "http://localhost:3000" ,"http://192.168.2.105:3000"})
@RestController
@RequestMapping(value="/", consumes = "application/json")
public class UserController {


  @Autowired
  private UserDetailsServiceImplementation userService;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JWTTokenUtils jwtTokenUtils;

  @Autowired
  private ArticleService articleService;


  @GetMapping("/home")
  public @ResponseBody String home() {
    return "hello from backend home";
  }

  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) throws UserNameAlreadyExistException, EmailAlreadyExistException {
    User u;
    System.out.println("user form fronted");
    System.out.println(user);
    try{
      u = userService.registerUser(user);
    }catch(EmailAlreadyExistException e){
      System.out.println(e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists", e);
    } catch(UserNameAlreadyExistException e){
      System.out.println(e);

      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists", e);
    }
    return new ResponseEntity<>(u, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody User user) throws BadCredentialsException, DisabledException{
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
    return new ResponseEntity<>(new JWTResponseUser(token), HttpStatus.OK);

  }

//  @GetMapping("/dashboard")
//  public ResponseEntity<?> getUserInfos(@RequestParam String user){
//
//    ArrayList<Article> articles= articleService.getArticlesByUsername(user);
//
//    return new ResponseEntity<>(articles, HttpStatus.OK);
//
//  }


}
