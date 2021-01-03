package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:3000" ,"http://192.168.2.105:3000"})
@RestController
@RequestMapping("/")
public class UserController {


  @GetMapping("/home")
  public @ResponseBody String home() {
    return "hello at home";
  }

  @GetMapping("/register")
  public @ResponseBody String getRegister(){
    return "please register";
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user){
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

}
