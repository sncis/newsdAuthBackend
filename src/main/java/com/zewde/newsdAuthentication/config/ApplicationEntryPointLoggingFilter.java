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
public class ApplicationEntryPointLoggingFilter extends OncePerRequestFilter {
  private final static Logger logger = LoggerFactory.getLogger(ApplicationEntryPointLoggingFilter.class);

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{

    String ipAddress = request.getRemoteAddr()  != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");
    System.out.println("################ Initialising ApplicationEntryPointLoggingFilter ##################");

    logger.info("csrf token ");
    logger.info("xsrf token with x : {}", request.getHeader("X-XSRF-TOKEN"));
    logger.info("xsrf token with x : {}", request.getHeader("X-XSRF-TOKEN"));


    logger.info("******************");
    logger.info("******************");
    logger.info("******************");
    logger.info("******************");
    logger.info("******************");

    logger.info("Logging Request from IpAddress: {}", ipAddress);
    logger.info("For request method: {} and uri : {}", request.getMethod(), request.getRequestURI());

    if(request.getContentLength() > 5000) {
      logger.warn("Request rejected due to too long content! Content length : {} " ,request.getContentLength());
      response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,"To long content");
    }

    logger.info("Request Length {} :  with content type {}", request.getContentLength(), request.getContentType());
    System.out.println("################ End of LoggingFilter ##################");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy(){
    logger.warn("Destroying Filter : {}", this);

  }
}
