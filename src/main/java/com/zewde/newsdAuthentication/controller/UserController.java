package com.zewde.newsdAuthentication.controller;


import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" ,"http://192.168.2.105:3000"})
@RestController
@RequestMapping("/")
public class UserController {

  @Autowired
  private UserDetailsServiceImplementation userService;


  @GetMapping("/home")
  public @ResponseBody String home() {
    return "hello at home";
  }

  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) throws UserNameAlreadyExistException, EmailAlreadyExistException {
    User u;
    try{
      u = userService.registerUser(user);
    }catch(EmailAlreadyExistException e){
      throw new EmailAlreadyExistException();
    } catch(UserNameAlreadyExistException e){
      throw new UserNameAlreadyExistException();
    }
    return new ResponseEntity<>(u, HttpStatus.CREATED);
  }

}
