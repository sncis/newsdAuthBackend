package com.zewde.newsdAuthentication.utils;

import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class JWTTokenUtilsTest {

  @InjectMocks
  private JWTTokenUtils jwtTokenUtils;

  private User u = createUser("someUser");
  private MyUserDetails userDetails = new MyUserDetails(u);

  private User createUser(String userName){
    User u = new User();
    u.setUserName(userName);
    u.setPassword("pass");
    u.setActive(true);

    return u;
  }

  @Before
  public void setUp(){
    Long jwtValidity = 6000L;

    ReflectionTestUtils.setField(jwtTokenUtils, "tokenValidity", jwtValidity);
    ReflectionTestUtils.setField(jwtTokenUtils, "secret", "some");
  }

  @Test
  public void getUsernameFromToken(){
    String token = jwtTokenUtils.generateToken("someUser");

    String username = jwtTokenUtils.getUsernameFromToken(token);

    System.out.println(username);
    assertEquals(username, "someUser");
  }

  @Test
  public void validateToken(){
   String token = jwtTokenUtils.generateToken(u.getUserName());
   boolean isValide = jwtTokenUtils.validateToken(userDetails, token);

   assertTrue(isValide);

  }

  @Test
  public void isTokenExpired(){
    String token = jwtTokenUtils.generateToken(u.getUserName());
    boolean isExipred =jwtTokenUtils.isTokenExpired(token);

    assertFalse(isExipred);
  }

  @Test
  public void shouldReturnTrueIfTokenIsExpired() throws Exception{
    boolean tokenIsExpired;
    Long jwtTokenValidity = 2000l;
    ReflectionTestUtils.setField(jwtTokenUtils, "tokenValidity", jwtTokenValidity);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm");


    String token = jwtTokenUtils.generateToken(u.getUserName());

    Date expirationDate= jwtTokenUtils.getExpirationDateFromToken(token);
    System.out.println(expirationDate);

    Thread.sleep(6000);

    try{
      tokenIsExpired = jwtTokenUtils.isTokenExpired(token);
    }catch(ExpiredJwtException e){
      tokenIsExpired= true;
    }
    assertThat(tokenIsExpired).isEqualTo(true);
    assertThat(expirationDate.before(new Date(System.currentTimeMillis()))).isEqualTo(true);

  }

  @Test
  public void shouldReturnFalseIfValidateTokenTokenIsExpired()throws InterruptedException{
    boolean valid;
    Long jwtTokenValidity = 2000l;
    ReflectionTestUtils.setField(jwtTokenUtils, "tokenValidity", jwtTokenValidity);

    MyUserDetails uDetails = new MyUserDetails(createUser("someUser"));
    String token = jwtTokenUtils.generateToken(u.getUserName());
    Date expirationDate= jwtTokenUtils.getExpirationDateFromToken(token);
    Thread.sleep(6000);

    try{
      jwtTokenUtils.validateToken(userDetails,token);
      valid = true;
    }catch(ExpiredJwtException e){
      valid = false;
    }

    assertThat(valid).isEqualTo(false);
    assertThat(expirationDate.before(new Date(System.currentTimeMillis()))).isEqualTo(true);

  }
  @Test
  public void shouldReturnFalseIfUserNameIsFalseInValidateToken(){
    MyUserDetails fakeUserDetails = new MyUserDetails(createUser("fakeUser"));
    String token = jwtTokenUtils.generateToken(u.getUserName());
    Date exDate = jwtTokenUtils.getExpirationDateFromToken(token);
    boolean valid;
    try{
      valid = jwtTokenUtils.validateToken(fakeUserDetails,token);
    }catch(ExpiredJwtException e){
      valid = true;
    }

    System.out.println(exDate);
    assertThat(valid).isEqualTo(false);
    assertThat(jwtTokenUtils.getUsernameFromToken(token)).isEqualTo(u.getUserName());
  }

}