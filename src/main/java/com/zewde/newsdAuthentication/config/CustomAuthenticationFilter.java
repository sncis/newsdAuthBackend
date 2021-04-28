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
  CustomJwtExceptionHandlerForEntryPoint authEntrypoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
    System.out.println("################ Initialising CustomAuthenticationFilter ##################");
    logger.info("Filtering request for uri:" + request.getRequestURI());

    String username = null;
    String token = null;

      try{
        token = cookiesUtils.getTokenFromCookies("jwtToken",request.getCookies());
        username = JWTTokenUtils.getUsernameFromToken(token);

        if(SecurityContextHolder.getContext().getAuthentication() == null && JWTTokenUtils.validateToken(username, token)){
          User user = userDetailsService.loadUserByUsername(username);
          setSecurityContext(user,request);
          chain.doFilter(request,response);

        }
      }catch(AuthenticationException authex ){
        logger.warn("error in authex");
        System.out.println(authex.getMessage());
        SecurityContextHolder.clearContext();
        authEntrypoint.commence(request,response, authex);

      }
      catch(ExpiredJwtException jwtexc){
        logger.warn("JWT Exception cause by : {}", jwtexc.getCause());
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "please Login or register");
      }
      catch(Exception ex){
        logger.warn("Exception occurred " + ex.getClass() + " because of " + ex.getMessage());
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden request");

      }

    logger.info("Request filtered successfully: " + request.getRequestURI());
    System.out.println("################ End of  CustomAuthenticationFilter ##################");


  }


  private void setSecurityContext(User user, HttpServletRequest request){
    UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request)
      throws ServletException {
    String path = request.getRequestURI();
    List<String> urls = Arrays.asList("/auth/register","/auth/login","/auth/confirm","/auth/logout","/", "/error", "/favicon.ico*");
    logger.info("Url is contained in notFilter Urls : {}");
    logger.info(urls.contains(path));
    logger.info("Request filtered successfully: " + request.getRequestURI());
    return urls.contains(path);
  }
}
