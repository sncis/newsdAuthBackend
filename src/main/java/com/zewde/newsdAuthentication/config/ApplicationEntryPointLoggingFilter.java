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
public class ApplicationEntryPointLoggingFilter extends OncePerRequestFilter {
  private final static Logger logger = LoggerFactory.getLogger(ApplicationEntryPointLoggingFilter.class);

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{

    String ipAddress = request.getRemoteAddr()  != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");
    System.out.println("################ Initialising ApplicationEntryPointLoggingFilter ##################");

    logger.info("Logging Request from ipAdresse: {}", ipAddress);
    logger.info("For request method: {}   and path : {}", request.getMethod(), request.getContextPath());
    logger.info("Request Length {} :  with contnet type {}", request.getContentLength(), request.getContentType());

    System.out.println("################ End of LoggingFilter ##################");

    chain.doFilter(request, response);
  }

  @Override
  public void destroy(){
    logger.warn("Destroying Filter : {}", this);

  }
}
