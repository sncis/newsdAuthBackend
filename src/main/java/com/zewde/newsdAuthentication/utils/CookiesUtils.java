package com.zewde.newsdAuthentication.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

@Component
public class CookiesUtils {

  @Autowired
  private JWTTokenUtils jwtTokenUtils;

  public CookiesUtils(){}

  public String getTokenFromCookies(String tokenName, Cookie[] cookies){
    String token = null;
    for(Cookie c : cookies) {
      if(c.getName().equals(tokenName)){
        token = c.getValue();
      }
    }
      return token;
    }

   public Cookie createCookie(String name, String value, int maxAge){
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge((maxAge));
    cookie.setSecure(true);
     cookie.setHttpOnly(true);
     cookie.setPath("/");
    return cookie;
   }

  public Cookie createCookie(String name, String value){
    Cookie cookie = new Cookie(name, value);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    return cookie;
  }

   public String getUsernameFromCookies(Cookie[] cookies){
    String jwtToken = getTokenFromCookies("jwtToken", cookies);
    String username="";
    try{
      username = jwtTokenUtils.getUsernameFromToken(jwtToken);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
    return username;
   }

   public ArrayList<Cookie> deleteCookies(Cookie[] cookies){
    ArrayList<Cookie> cookiesToDelete = new ArrayList<>();
    for(Cookie c : cookies){
      String name = c.getName();
      Cookie newCookie = createCookie(name, null,0);
      cookiesToDelete.add(newCookie);
     }

    return cookiesToDelete;
   }

}
