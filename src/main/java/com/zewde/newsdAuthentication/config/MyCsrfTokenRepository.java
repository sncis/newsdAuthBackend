package com.zewde.newsdAuthentication.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class MyCsrfTokenRepository implements CsrfTokenRepository {
  static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
  static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
  static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
  private String parameterName = "_csrf";
  private String headerName = "X-XSRF-TOKEN";
  private String cookieName = "XSRF-TOKEN";
  private boolean cookieHttpOnly = true;
  private String cookiePath;
  private String cookieDomain;
  private Boolean secure;

  private final static Logger logger = LoggerFactory.getLogger(MyCsrfTokenRepository.class);


  public MyCsrfTokenRepository() {
  }


  public CsrfToken generateToken(HttpServletRequest request) {
    return new DefaultCsrfToken(this.headerName, this.parameterName, this.createNewToken());
  }

  public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
    String tokenValue = token != null ? token.getToken() : "";
    Cookie cookie = new Cookie(this.cookieName, tokenValue);
    logger.info("***************************CSRF Repo*********");
//    cookie.setSecure(this.secure != null ? this.secure : request.isSecure());
    cookie.setPath(StringUtils.hasLength(this.cookiePath) ? this.cookiePath : this.getRequestContext(request));
    cookie.setMaxAge(token != null ? -1 : 0);
//    cookie.setHttpOnly(this.cookieHttpOnly);
    if (StringUtils.hasLength(this.cookieDomain)) {
      cookie.setDomain(this.cookieDomain);
    }

    logger.info("cookie name: {}", cookie.getName());
    logger.info("cookie value: {}", cookie.getValue());

    response.addHeader("XSRF-TOKEN",cookie.getValue());
    response.addCookie(cookie);
  }

  public CsrfToken loadToken(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, this.cookieName);
    String token = request.getHeader("X-XSRF-TOKEN");
    logger.info("*************load method      \n");
    logger.info("cookie name: {}", cookie.getName());
    logger.info("cookie value: {}", cookie.getValue());
    logger.info("token: {}", token);

    if (token == null) {
      return null;
    } else {
//      String token = cookie.getValue();
      return !StringUtils.hasLength(token) ? null : new DefaultCsrfToken(this.headerName, this.parameterName, token);
    }
  }

  public void setParameterName(String parameterName) {
    Assert.notNull(parameterName, "parameterName is not null");
    this.parameterName = parameterName;
  }

  public void setHeaderName(String headerName) {
    Assert.notNull(headerName, "headerName is not null");
    this.headerName = headerName;
  }

  public void setCookieName(String cookieName) {
    Assert.notNull(cookieName, "cookieName is not null");
    this.cookieName = cookieName;
  }

  public void setCookieHttpOnly(boolean cookieHttpOnly) {
    this.cookieHttpOnly = cookieHttpOnly;
  }

  private String getRequestContext(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    return contextPath.length() > 0 ? contextPath : "/";
  }

  public static MyCsrfTokenRepository withHttpOnlyFalse() {
    MyCsrfTokenRepository result = new MyCsrfTokenRepository();
    result.setCookieHttpOnly(false);
    return result;
  }

  private String createNewToken() {
    return UUID.randomUUID().toString();
  }

  public void setCookiePath(String path) {
    this.cookiePath = path;
  }

  public String getCookiePath() {
    return this.cookiePath;
  }

  public void setCookieDomain(String cookieDomain) {
    this.cookieDomain = cookieDomain;
  }

  public void setSecure(Boolean secure) {
    this.secure = secure;
  }
}
