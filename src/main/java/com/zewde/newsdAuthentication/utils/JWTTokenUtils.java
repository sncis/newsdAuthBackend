package com.zewde.newsdAuthentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTTokenUtils implements Serializable {
  private static final long serialVersionUID = -2550185165626007488L;

  @Value("${jwt.TOKEN_VALIDITY}")
  private Long tokenValidity;

  @Value("${jwt.TOKEN_SECRET}")
  private String secret;

  public JWTTokenUtils(){
  }

  public String generateToken(String user){
    Map<String, Object> claims = new HashMap<>();
    String token = Jwts.builder().setClaims(claims).setSubject(user)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
        .signWith(SignatureAlgorithm.HS512, secret).compact();

    return token;

  }

  public boolean validateToken(String username, String token){
    String user = getUsernameFromToken(token);
    boolean isTokenValid = user.equals(username) && !isTokenExpired(token);

    return isTokenValid;
  }


  public String getUsernameFromToken(String token){
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    String username = claims.getSubject();

    return username;
  }

  public Date getExpirationDateFromToken(String token){
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    Date expirationDate = claims.getExpiration();

    return expirationDate;
  }

  public boolean isTokenExpired(String token){
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    Date expirationDate = claims.getExpiration();
    return expirationDate.before(new Date());
  }

}
