package com.zewde.newsdAuthentication.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CookiesUtilsTest {

  @Autowired
  private CookiesUtils cookiesUtils;

  @Autowired
  private JWTTokenUtils jwtTokenUtils;


  @Test
  public void getTokenFromCookies(){
    Cookie[] cookies = {new Cookie("jwtToken", "someToken"), new Cookie("someOther", "some other value")};
    assertEquals(cookiesUtils.getTokenFromCookies("jwtToken", cookies), "someToken");
  }
  @Test
  public void ShouldReturnNull_WhenNoCookie(){
    Cookie[] cookies = {new Cookie("cookie1", "someToken"), new Cookie("someOther", "some other value")};
    assertEquals(cookiesUtils.getTokenFromCookies("jwtToken", cookies), null);

  }

  @Test
  public void createCookieWithMaxAge(){
    Cookie createdCookie = cookiesUtils.createCookie("cookieName", "some value", 1);

    assertEquals(createdCookie.getName(), "cookieName");
    assertEquals(createdCookie.getValue(), "some value");
    assertEquals(createdCookie.getMaxAge(), 1);
    assertEquals(createdCookie.isHttpOnly(), true);
    assertEquals(createdCookie.getPath(), "/");
  }

  @Test
  public void createCookie(){
    Cookie createdCookie = cookiesUtils.createCookie("cookieName", "some value", 1);

    assertEquals(createdCookie.getName(), "cookieName");
    assertEquals(createdCookie.getValue(), "some value");
    assertEquals(createdCookie.getSecure(), true);
    assertEquals(createdCookie.isHttpOnly(), true);
    assertEquals(createdCookie.getPath(), "/");
  }

  @Test
  public void getUsernameFromCookie(){
    String token = jwtTokenUtils.generateToken("someUser") ;
    Cookie[] cookies = {new Cookie("jwtToken", token), new Cookie("someOther", "some other value")};

    assertEquals(cookiesUtils.getUsernameFromCookies(cookies), "someUser");
  }
}
