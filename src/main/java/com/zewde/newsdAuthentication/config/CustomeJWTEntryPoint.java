package com.zewde.newsdAuthentication.config;


import com.zewde.newsdAuthentication.utils.CookiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomeJWTEntryPoint implements AuthenticationEntryPoint {

  @Autowired
  CookiesUtils cookiesUtils;

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
      throws IOException {
      Cookie cookie = cookiesUtils.createCookie("jwtToken", "", 0);
      res.addCookie(cookie);
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorised user please login or register");
  }
}
