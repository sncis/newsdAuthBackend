package com.zewde.newsdAuthentication.utils;

import com.zewde.newsdAuthentication.AuthFailureListener;
import com.zewde.newsdAuthentication.service.LoginFailureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
  private final static Logger logger = LoggerFactory.getLogger(AuthFailureListener.class);


  @Autowired
  private LoginFailureService loginFailureService;


  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent authSuccessEvent){
    String ipAddress = loginFailureService.getIpAddress();
    logger.info("login succeeded for IP Address : {}", ipAddress);

    loginFailureService.loginSucceeded(ipAddress);

  }


}
