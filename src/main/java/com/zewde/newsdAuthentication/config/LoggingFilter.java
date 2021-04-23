package com.zewde.newsdAuthentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//@Order(1)
public class LoggingFilter extends OncePerRequestFilter {
  private final static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
    System.out.println("################ Initialising LoggingFilter ##################");
    logger.info("Logging Request {} : {}", request.getMethod(), request.getRequestURI());
    logger.info("for Content path : {}", request.getContextPath());
    logger.info("with headers: {}", request.getHeaderNames());
    logger.info(" Request Length {} :  with contnet type {}", request.getContentLength(), request.getContentType());

  chain.doFilter(request, response);
  }

  @Override
  public void destroy(){
    logger.warn("Destroying Filter : {}", this);

  }
}
