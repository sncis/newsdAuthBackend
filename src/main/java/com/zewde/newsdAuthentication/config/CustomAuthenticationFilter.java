package com.zewde.newsdAuthentication.config;


import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  UserDetailsServiceImplementation userDetailsService;

  @Autowired
  JWTTokenUtils JWTTokenUtils;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
    String token= null;
    String userName = null;
    Cookie[] cookies =  request.getCookies();
    if(cookies != null){
      for(Cookie c : cookies){
        System.out.println(c.getName());
        System.out.println(c.getValue());

        if(c.getName().equals(("jwtToken"))){
          token = c.getValue();
          try{
            userName = JWTTokenUtils.getUsernameFromToken(token);
          }catch(IllegalArgumentException e){
            logger.warn("unable to get JWT Token ");
          }catch(ExpiredJwtException e){
            logger.warn("JWT Token is expired");
          }
          Cookie cookie = new Cookie("jwtToken", token);
          cookie.setSecure(true);
          cookie.setHttpOnly(true);
          cookie.setPath("/");
          response.addCookie(cookie);
        }
      }
    }


    if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
      MyUserDetails userdetails = this.userDetailsService.loadUserByUsername(userName);

      if(JWTTokenUtils.validateToken(userdetails,token)){
        UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(userdetails, null, new ArrayList<>());

        userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
      }
    }
    chain.doFilter(request,response);

  }


}
