package com.zewde.newsdAuthentication.config;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomeJWTEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
      throws IOException {
    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized User");

  }

}
