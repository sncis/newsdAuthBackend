package com.zewde.newsdAuthentication;

import com.zewde.newsdAuthentication.service.LoginFailureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
  private final static Logger logger = LoggerFactory.getLogger(AuthFailureListener.class);

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginFailureService loginFailureService;


  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authFailureEvent){

    String ipAddress = loginFailureService.getIpAddress();
    logger.info("login failed for IP Address : {}", ipAddress);

    loginFailureService.loginFailed(ipAddress);

  }

}
