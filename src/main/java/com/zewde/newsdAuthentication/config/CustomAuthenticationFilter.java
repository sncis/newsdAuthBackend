package com.zewde.newsdAuthentication.config;


import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.CookiesUtils;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  UserDetailsServiceImplementation userDetailsService;

  @Autowired
  JWTTokenUtils JWTTokenUtils;

  @Autowired
  CookiesUtils cookiesUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
    logger.info("Filtering request for path: " + request.getRequestURI());
    String username = null;
    String token = null;
      try{
        token = cookiesUtils.getTokenFromCookies("jwtToken",request.getCookies());
        username = JWTTokenUtils.getUsernameFromToken(token);

      }catch(Exception ex){
        logger.warn("Exception occurred " + ex.getClass() + " because of " + ex.getMessage());
      }

      if(SecurityContextHolder.getContext().getAuthentication() == null && JWTTokenUtils.validateToken(username, token)){
        User user = userDetailsService.loadUserByUsername(username);
        setSecurityContext(user,request);
      }

    chain.doFilter(request,response);
  }


  private void setSecurityContext(User user, HttpServletRequest request){
    UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>());

    userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);

  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request)
      throws ServletException {
    String path = request.getRequestURI();
    List<String> urls = Arrays.asList("/login", "/register", "confirmeUser", "/logout" );
    return urls.contains(path);
  }

}
