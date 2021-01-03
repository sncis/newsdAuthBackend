package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" ,"http://192.168.2.105:3000"})
@RestController
@RequestMapping("/")
public class UserController {

  @Autowired
  UserDetailsServiceImplementation userService;


  @GetMapping("/home")
  public @ResponseBody String home() {
    return "hello at home";
  }

  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception{
//    User u;
//    u = userService.registerUser(user);
//
//    return new ResponseEntity<>(u,HttpStatus.CREATED);
//  }
    User u;
    try{
      u = userService.registerUser(user);
    }catch(Exception e){
      throw new Exception("something went wrong");
    }
    return new ResponseEntity<>(u, HttpStatus.CREATED);
  }

}
