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

  final String TOKEN_PREFIX= "Bearer ";


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
    final String requestHeader = request.getHeader("Authorization");
    String userName = null;
    String jwtToken = null;

    if(requestHeader != null && requestHeader.startsWith(TOKEN_PREFIX)){
      jwtToken = requestHeader.substring(7);
      System.out.println(jwtToken);

      try{
        userName = JWTTokenUtils.getUsernameFromToken(jwtToken);
      }catch(IllegalArgumentException e){
        logger.warn("unable to get JWT Token ");
      }catch(ExpiredJwtException e){
        logger.warn("JWT Token is expired");
      }

    }

    if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
      MyUserDetails userdetails = this.userDetailsService.loadUserByUsername(userName);

      if(JWTTokenUtils.validateToken(userdetails,jwtToken)){
        UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(userdetails, null, new ArrayList<>());

        userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
      }
    }
    chain.doFilter(request,response);

  }


}
