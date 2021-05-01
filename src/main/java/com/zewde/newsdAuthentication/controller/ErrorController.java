package com.zewde.newsdAuthentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

  @RestController
  public class ErrorController {
    @RequestMapping("/error")
    public String name(Throwable e) {
      return "some error occured";
    }
  }
