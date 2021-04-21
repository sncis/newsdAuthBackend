package com.zewde.newsdAuthentication.config;


import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.CookiesUtils;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
//@Order(2)
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  UserDetailsServiceImplementation userDetailsService;

  @Autowired
  JWTTokenUtils JWTTokenUtils;

  @Autowired
  CookiesUtils cookiesUtils;

  @Autowired
  CustomeJWTEntryPoint authEntrypoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
    System.out.println("################ Initialising CustomAuthenticationFilter ##################");
    logger.info("Filtering request for path: " + request.getRequestURI());

    String username = null;
    String token = null;
      try{
        token = cookiesUtils.getTokenFromCookies("jwtToken",request.getCookies());
        username = JWTTokenUtils.getUsernameFromToken(token);
        if(SecurityContextHolder.getContext().getAuthentication() == null && JWTTokenUtils.validateToken(username, token)){
          User user = userDetailsService.loadUserByUsername(username);
          setSecurityContext(user,request);
        }
      }catch(AuthenticationException | ExpiredJwtException authex ){
        authEntrypoint.commence(request,response, (AuthenticationException) authex);
      } catch(Exception ex){
        logger.warn("Exception occurred " + ex.getClass() + " because of " + ex.getMessage());
//checking why 500 is returned when token is not valid
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not authorized");
    }

      logger.info("Filtering request for path: " + request.getRequestURI());
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
    List<String> urls = Arrays.asList("/auth/register","/auth/login","/auth/logout","/auth/confirmUser","/", "/error", "/favicon.ico");
    System.out.println("***********************");
    System.out.println(urls.contains(path));
    System.out.println("***********************");
    return urls.contains(path);
  }

}
